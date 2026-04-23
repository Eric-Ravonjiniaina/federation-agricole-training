package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.MemberPayment;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class MemberPaymentRepository {
    private final DataSource dataSource;
    public MemberPaymentRepository(DataSource dataSource) { this.dataSource = dataSource; }

    public MemberPayment save(MemberPayment p) {
        String sql = "INSERT INTO member_payments (id, amount, payment_mode, member_id, account_id, creation_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setDouble(2, p.getAmount());
            ps.setString(3, p.getPayment().name());
            ps.setString(4, p.getMember().getId());
            ps.setString(5, p.getAccountCredited().getId());
            ps.setDate(6, java.sql.Date.valueOf(p.getCreationDate()));

            ps.executeUpdate();
            return p;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du paiement : " + p.getId(), e);
        }
    }
}
