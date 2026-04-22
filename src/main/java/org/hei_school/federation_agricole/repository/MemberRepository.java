package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.entity.MemberEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MemberRepository {
    private final Connection connection;

    public MemberRepository(Connection connection) { this.connection = connection; }

    public boolean exists(String table, String id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String save(MemberEntity m, String collId) throws SQLException {
        String sql = "INSERT INTO membres (id, first_name, last_name, coll_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, m.getId());
            ps.setString(2, m.getFirstName());
            ps.setString(3, m.getLastName());
            ps.setString(4, collId);
            ps.executeUpdate();
        }
        return sql;
    }

    public Optional<MemberEntity> findById(String id) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM members WHERE id = ?")) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MemberEntity m = map(rs);
                return Optional.of(m);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
