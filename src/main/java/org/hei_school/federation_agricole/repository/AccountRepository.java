package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.FinancialAccount;
import org.hei_school.federation_agricole.entity.CashAccount;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRepository {
    private final DataSource dataSource;

    public AccountRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public Optional<FinancialAccount> findById(String id) {
        // Liste explicite des colonnes pour la performance et la sécurité
        String sql = "SELECT id, amount, collectivity_id FROM financial_accounts WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FinancialAccount account = new CashAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("amount"));

                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du compte : " + id, e);
        }
        return Optional.empty();
    }
    public void updateAmount(FinancialAccount account) {
        String sql = "UPDATE financial_accounts SET amount = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, account.getAmount());
            ps.setString(2, account.getId());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Échec : compte introuvable.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du solde pour le compte : " + account.getId(), e);
        }
    }
    public void save(FinancialAccount account) {
        String sql = "UPDATE financial_accounts SET amount = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, account.getAmount());
            ps.setString(2, account.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}