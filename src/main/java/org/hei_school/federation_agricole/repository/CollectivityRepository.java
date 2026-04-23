package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.CollectivityStructure;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectivityRepository {

    private final DataSource dataSource;

    public CollectivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Collectivity c) {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // 1
                PreparedStatement ps1 = conn.prepareStatement(
                        "INSERT INTO collectivities(id, location) VALUES (?, ?)");
                ps1.setString(1, c.getId());
                ps1.setString(2, c.getLocation());
                ps1.executeUpdate();

                // 2
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO collectivity_members VALUES (?, ?)");
                for (MemberEntity m : c.getMembers()) {
                    ps2.setString(1, c.getId());
                    ps2.setString(2, m.getId());
                    ps2.addBatch();
                }
                ps2.executeBatch();

                // 3
                PreparedStatement ps3 = conn.prepareStatement(
                        "INSERT INTO collectivity_structure VALUES (?, ?, ?, ?, ?)");
                ps3.setString(1, c.getId());
                ps3.setString(2, c.getStructure().getPresident().getId());
                ps3.setString(3, c.getStructure().getVicePresident().getId());
                ps3.setString(4, c.getStructure().getTreasurer().getId());
                ps3.setString(5, c.getStructure().getSecretary().getId());
                ps3.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Collectivity> findById(String id) {

        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM collectivities WHERE id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Collectivity c = new Collectivity();
                c.setId(rs.getString("id"));
                c.setLocation(rs.getString("location"));
                c.setNumber(rs.getString("number"));
                c.setName(rs.getString("name"));
                return Optional.of(c);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByName(String name) {
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM collectivities WHERE name = ?");
            ps.setString(1, name);

            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByNumber(String number) {
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM collectivities WHERE number = ?");
            ps.setString(1, number);

            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIdentity(String id, String number, String name) {

        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE collectivities SET number = ?, name = ? WHERE id = ?");

            ps.setString(1, number);
            ps.setString(2, name);
            ps.setString(3, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Collectivity findByIdWithDetails(String id) {

        try (Connection conn = dataSource.getConnection()) {

            // 1. collectivity
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM collectivities WHERE id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Collectivity c = new Collectivity();
            c.setId(rs.getString("id"));
            c.setLocation(rs.getString("location"));
            c.setName(rs.getString("name"));
            c.setNumber(rs.getString("number"));

            // 2. récupérer les membres (IDS uniquement)
            PreparedStatement psMembers = conn.prepareStatement(
                    "SELECT member_id FROM collectivity_members WHERE collectivity_id = ?");
            psMembers.setString(1, id);

            ResultSet rsMembers = psMembers.executeQuery();

            List<MemberEntity> members = new ArrayList<>();

            while (rsMembers.next()) {
                MemberEntity m = new MemberEntity();
                m.setId(rsMembers.getString("member_id"));
                members.add(m);
            }

            c.setMembers(members);

            // 3. structure
            PreparedStatement psStruct = conn.prepareStatement(
                    "SELECT * FROM collectivity_structure WHERE collectivity_id = ?");
            psStruct.setString(1, id);

            ResultSet rsStruct = psStruct.executeQuery();

            if (rsStruct.next()) {
                CollectivityStructure s = new CollectivityStructure();

                MemberEntity president = new MemberEntity();
                president.setId(rsStruct.getString("president"));
                s.setPresident(president);

                MemberEntity vp = new MemberEntity();
                vp.setId(rsStruct.getString("vice_president"));
                s.setVicePresident(vp);

                MemberEntity treasurer = new MemberEntity();
                treasurer.setId(rsStruct.getString("treasurer"));
                s.setTreasurer(treasurer);

                MemberEntity secretary = new MemberEntity();
                secretary.setId(rsStruct.getString("secretary"));
                s.setSecretary(secretary);

                c.setStructure(s);
            }

            return c;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
