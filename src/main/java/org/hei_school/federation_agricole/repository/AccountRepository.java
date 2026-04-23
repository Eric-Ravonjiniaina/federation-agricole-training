package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.BankAccount;
import org.hei_school.federation_agricole.entity.FinancialAccount;
import org.hei_school.federation_agricole.entity.CashAccount;
import org.hei_school.federation_agricole.entity.MobileBankingAccount;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private FinancialAccount mapToAccountType(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        FinancialAccount account = null;

        if ("BANK".equalsIgnoreCase(type)) {
            BankAccount bank = new BankAccount();

            bank.setBankCode(rs.getString("bank_code"));
            bank.setBranchCode(rs.getString("branch_code"));
            bank.setAccountNumber(rs.getString("account_number"));
            bank.setKeyRIB(rs.getString("key_rib"));

            account = bank;
        }
        else if ("MOBILE".equalsIgnoreCase(type)) {
            MobileBankingAccount mobile = new MobileBankingAccount();

            mobile.setMobileNumber(rs.getString("mobile_number"));
            mobile.setServiceName(rs.getString("service_name"));

            account = mobile;
        }
        else {
            account = new CashAccount();
        }
        account.setId(rs.getString("id"));
        account.setAmount(rs.getDouble("amount"));

        return account;
    }
    public List<FinancialAccount> findByCollectivityAtDate(String collectivityId, LocalDate at) {
        List<FinancialAccount> accounts = new ArrayList<>();
        String sqlAccounts = "SELECT id, amount, type FROM financial_accounts WHERE collectivity_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlAccounts)) {
                ps.setString(1, collectivityId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    FinancialAccount account = mapToAccountType(rs);
                    double adjustedBalance = calculateHistoricalBalance(conn, account.getId(), account.getAmount(), at);
                    account.setAmount(adjustedBalance);

                    accounts.add(account);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return accounts;
    }

    private double calculateHistoricalBalance(Connection conn, String accountId, double currentAmount, LocalDate at) throws SQLException {

        String sqlSum = "SELECT SUM(amount) FROM transactions WHERE account_id = ? AND creation_date > ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlSum)) {
            ps.setString(1, accountId);
            ps.setDate(2, java.sql.Date.valueOf(at));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return currentAmount - rs.getDouble(1);
            }
        }
        return currentAmount;
    }
}