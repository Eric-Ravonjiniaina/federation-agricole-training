package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.CollectivityTransaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private final DataSource dataSource;

    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityTransaction> findByCollectivityAndPeriod(
            String collectivityId,
            LocalDate from,
            LocalDate to
    ) {

        String sql = """
            SELECT id, collectivity_id, member_id, amount, payment_mode, creation_date FROM transactions
            WHERE collectivity_id = ?
            AND creation_date BETWEEN ? AND ?
        """;

        List<CollectivityTransaction> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(map(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CollectivityTransaction map(ResultSet rs) throws Exception {
        CollectivityTransaction t = new CollectivityTransaction();

        t.setId(rs.getString("id"));
        t.setCollectivityId(rs.getString("collectivity_id"));
        t.setMemberId(rs.getString("member_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMode(rs.getString("payment_mode"));
        t.setCreationDate(rs.getDate("creation_date").toLocalDate());

        return t;
    }
    public void save(CollectivityTransaction tx) {
        String sql = "INSERT INTO transactions (id, amount, account_id, member_id, payment_mode, creation_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tx.getId()); // N'oublie pas de générer l'ID avant
            ps.setDouble(2, tx.getAmount());
            ps.setString(3, tx.getAccountCredited().getId());
            ps.setString(4, tx.getMemberDebited().getId());
            ps.setString(5, tx.getPaymentMode());
            ps.setDate(6, java.sql.Date.valueOf(tx.getCreationDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
