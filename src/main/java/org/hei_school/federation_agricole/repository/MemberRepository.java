package org.hei_school.federation_agricole.repository;

import lombok.RequiredArgsConstructor;
import org.hei_school.federation_agricole.controller.dto.CollectivityStatisticsResponse;
import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.Member;
import org.hei_school.federation_agricole.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final Connection connection;
    private final MemberMapper memberMapper;
    private final CollectivityMemberRepository collectivityMemberRepository;
    private final MemberRefereeRepository memberRefereeRepository;

    public List<Member> saveAll(List<Member> members) {
        List<Member> memberList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                """
                        insert into "member" (id, 
                                              first_name,
                                              last_name,
                                              birth_date,
                                              gender,
                                              address,
                                              profession,
                                              phone_number,
                                              email,
                                              occupation,
                                              registration_fee_paid,
                                              membership_dues_paid) 
                        values (?, ?, ?, ?, ?::gender, ?, ?, ?, ?, ?::member_occupation, ?, ?) 
                        on conflict (id) do update set first_name = excluded.first_name,
                                                       last_name = excluded.last_name,
                                                       birth_date = excluded.birth_date,
                                                       gender = excluded.gender,
                                                       phone_number = excluded.phone_number,
                                                       email = excluded.email,
                                                       address = excluded.address,
                                                       profession = excluded.profession,
                                                       occupation = excluded.occupation
                        returning id;
                        """)) {
            for (Member member : members) {
                preparedStatement.setString(1, member.getId());
                preparedStatement.setString(2, member.getFirstName());
                preparedStatement.setString(3, member.getLastName());
                preparedStatement.setDate(4, java.sql.Date.valueOf(member.getBirthDate()));
                preparedStatement.setObject(5, member.getGender().name());
                preparedStatement.setString(6, member.getAddress());
                preparedStatement.setString(7, member.getProfession());
                preparedStatement.setString(8, member.getPhoneNumber());
                preparedStatement.setString(9, member.getEmail());
                preparedStatement.setObject(10, member.getOccupation().name());
                preparedStatement.setBoolean(11, member.getRegistrationFeePaid());
                preparedStatement.setBoolean(12, member.getMembershipDuesPaid());
                preparedStatement.addBatch();
            }
            var executedRow = preparedStatement.executeBatch();
            for (int i = 0; i < executedRow.length; i++) {
                Member member = members.get(i);

                attachCollectivityMember(member);
                attachRefereeMember(member);

                memberList.add(findById(member.getId()).orElseThrow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberList;
    }

    private void attachRefereeMember(Member member) {
        List<Member> referees = member.getReferees();
        for (Member referee : referees) {
            memberRefereeRepository.attachMemberReferee(referee, member);
        }
    }

    private void attachCollectivityMember(Member member) {
        List<Collectivity> collectivities = member.getCollectivities();
        for (Collectivity collectivity : collectivities) {
            collectivityMemberRepository.attachMemberToCollectivity(collectivity, member);
        }
    }

    public Optional<Member> findById(String id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select member.id, first_name, last_name, birth_date, gender, phone_number, email, address, profession, occupation,registration_fee_paid, membership_dues_paid
                from "member"
                where id = ?
                """)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var member = memberMapper.mapFromResultSet(resultSet);
                member.setReferees(findRefereesByIdMember(member.getId()));
                return Optional.of(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Member> findAllByCollectivity(Collectivity collectivity) {
        List<Member> memberList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select member.id, first_name, last_name, birth_date, gender, phone_number, email, address, profession, occupation,registration_fee_paid, membership_dues_paid
                from "member"
                    join collectivity_member on member.id = collectivity_member.member_id
                    join collectivity on collectivity.id = collectivity_member.collectivity_id
                where collectivity_member.collectivity_id = ?
                """)) {
            preparedStatement.setString(1, collectivity.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var memberMapped = memberMapper.mapFromResultSet(resultSet);
                memberMapped.setReferees(findRefereesByIdMember(memberMapped.getId()));
                memberMapped.addCollectivity(collectivity);
                memberList.add(memberMapped);
            }
            return memberList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Member> findRefereesByIdMember(String idMember) {
        List<Member> memberList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select member.id, first_name, last_name, birth_date, gender, phone_number, email, address, profession, occupation,registration_fee_paid, membership_dues_paid
                from "member"
                    join member_referee on member.id = member_referee.member_referee_id
                where member_referee.member_refereed_id = ?
                """)) {
            preparedStatement.setString(1, idMember);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                memberList.add(memberMapper.mapFromResultSet(resultSet));
            }
            return memberList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CollectivityStatisticsResponse getStatistics(LocalDate from, LocalDate to) {

        String sql = """
        SELECT
            COUNT(DISTINCT m.id) AS total_members,

            COUNT(DISTINCT CASE
                WHEN mp.amount >= dues.total_due
                THEN m.id
            END) AS up_to_date_members

        FROM member m

        LEFT JOIN (
            SELECT
                cm.member_id,

                COALESCE(SUM(
                    CASE mf.frequency

                        WHEN 'PUNCTUALLY' THEN
                            CASE
                                WHEN mf.eligible_from BETWEEN ? AND ?
                                THEN mf.amount
                                ELSE 0
                            END

                        WHEN 'MONTHLY' THEN
                            mf.amount * (
                                EXTRACT(YEAR FROM AGE(?, mf.eligible_from)) * 12
                                + EXTRACT(MONTH FROM AGE(?, mf.eligible_from))
                                + 1
                            )

                        WHEN 'ANNUALLY' THEN
                            mf.amount * (
                                EXTRACT(YEAR FROM AGE(?, mf.eligible_from))
                                + 1
                            )

                        ELSE 0
                    END
                ), 0) AS total_due

            FROM collectivity_member cm

            JOIN membership_fee mf
                ON mf.collectivity_id = cm.collectivity_id

            WHERE mf.status = 'ACTIVE'

            GROUP BY cm.member_id
        ) dues
            ON dues.member_id = m.id

        LEFT JOIN (
            SELECT
                member_debited_id,
                SUM(amount) AS amount
            FROM member_payment
            WHERE creation_date BETWEEN ? AND ?
            GROUP BY member_debited_id
        ) mp
            ON mp.member_debited_id = m.id
    """;

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, Date.valueOf(from));
            preparedStatement.setDate(2, Date.valueOf(to));

            preparedStatement.setDate(3, Date.valueOf(to));
            preparedStatement.setDate(4, Date.valueOf(to));

            preparedStatement.setDate(5, Date.valueOf(to));

            preparedStatement.setDate(6, Date.valueOf(from));
            preparedStatement.setDate(7, Date.valueOf(to));

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                int totalMembers = rs.getInt("total_members");
                int upToDateMembers = rs.getInt("up_to_date_members");

                double percentage = 0;

                if (totalMembers > 0) {
                    percentage =
                            (upToDateMembers * 100.0) / totalMembers;
                }

                CollectivityStatisticsResponse response =
                        new CollectivityStatisticsResponse();

                response.setNewMembersCount(totalMembers);

                response.setUpToDateMembersPercentage(percentage);

                return response;
            }

            return new CollectivityStatisticsResponse();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

