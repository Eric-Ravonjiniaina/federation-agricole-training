package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.GenderEnum;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.hei_school.federation_agricole.entity.MemberOccupation;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final DataSource dataSource;

    public MemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean exists(String table, String id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String save(MemberEntity m) throws SQLException {
        String sql = """
            INSERT INTO members (id, first_name, last_name, birth_date, gender,
                address, profession, phone_number, email, occupation)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getId());
            ps.setString(2, m.getFirstName());
            ps.setString(3, m.getLastName());
            ps.setDate(4, m.getBirthDate() != null ? Date.valueOf(m.getBirthDate()) : null);
            ps.setString(5, m.getGender() != null ? m.getGender().name() : null);
            ps.setString(6, m.getAddress());
            ps.setString(7, m.getProfession());
            ps.setInt(8, m.getPhoneNumber());
            ps.setString(9, m.getEmail());
            ps.setString(10, m.getOccupation() != null ? m.getOccupation().name() : null);
            ps.executeUpdate();
            return m.getId();
        }
    }

    public void saveReferees(String memberId, List<String> refereeIds) throws SQLException {
        String sql = "INSERT INTO member_referees (member_id, referee_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String refId : refereeIds) {
                ps.setString(1, memberId);
                ps.setString(2, refId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public Optional<MemberEntity> findById(String id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberEntity> findRefereesByMemberId(String memberId) {
        String sql = """
            SELECT m.* FROM members m
            JOIN member_referees mr ON mr.referee_id = m.id
            WHERE mr.member_id = ?
        """;
        List<MemberEntity> referees = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                referees.add(mapRow(rs));
            }
            return referees;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MemberEntity mapRow(ResultSet rs) throws Exception {
        MemberEntity m = new MemberEntity();
        m.setId(rs.getString("id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        Date bd = rs.getDate("birth_date");
        if (bd != null) m.setBirthDate(bd.toLocalDate());
        String gender = rs.getString("gender");
        if (gender != null) m.setGender(GenderEnum.valueOf(gender));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getInt("phone_number"));
        m.setEmail(rs.getString("email"));
        String occ = rs.getString("occupation");
        if (occ != null) m.setOccupation(MemberOccupation.valueOf(occ));
        return m;
    }
}

