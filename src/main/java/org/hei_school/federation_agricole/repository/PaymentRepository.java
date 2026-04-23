package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.MemberPayment;

import java.sql.*;
import java.util.Optional;

public class PaymentRepository {
    private final DataSource dataSource;

    public PaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<MemberPayment> findById(String id) {
        String sql = "SELECT id, amount, payment_mode, creation_date FROM member_payments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MemberPayment p = new MemberPayment();
                    p.setId(rs.getString("id"));
                    p.setAmount(rs.getDouble("amount"));
                    p.setCreationDate(rs.getDate("creation_date").toLocalDate());
                    return Optional.of(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}