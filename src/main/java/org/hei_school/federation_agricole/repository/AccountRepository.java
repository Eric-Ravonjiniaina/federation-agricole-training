package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
        String sql = "SELECT * FROM financial_accounts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToAccountType(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById compte : " + id, e);
        }
        return Optional.empty();
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

    public List<FinancialAccount> findByCollectivityAtDate(String collectivityId, LocalDate at) {
        List<FinancialAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM financial_accounts WHERE collectivity_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FinancialAccount account = mapToAccountType(rs);
                double adjusted = calculateHistoricalBalance(conn, account.getId(), account.getAmount(), at);
                account.setAmount(adjusted);
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    private double calculateHistoricalBalance(Connection conn, String accountId, double current, LocalDate at) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE account_id = ? AND creation_date > ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setDate(2, Date.valueOf(at));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return current - rs.getDouble(1);
        }
        return current;
    }

    private FinancialAccount mapToAccountType(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        FinancialAccount account;

        if ("BANK".equalsIgnoreCase(type)) {
            BankAccount bank = new BankAccount();
            bank.setHolderName(rs.getString("holder_name"));
            bank.setBankName(rs.getString("bank_name"));
            bank.setBankCode(rs.getString("bank_code"));
            bank.setBankBranchCode(rs.getString("branch_code"));
            bank.setBankAccountNumber(rs.getString("account_number"));
            bank.setBankAccountKey(rs.getString("key_rib"));
            account = bank;
        } else if ("MOBILE".equalsIgnoreCase(type)) {
            MobileBankingAccount mobile = new MobileBankingAccount();
            mobile.setHolderName(rs.getString("holder_name"));
            mobile.setMobileBankingService(rs.getString("service_name"));
            mobile.setMobileNumber(rs.getString("mobile_number"));
            account = mobile;
        } else {
            account = new CashAccount();
        }

        account.setId(rs.getString("id"));
        account.setAmount(rs.getDouble("amount"));
        return account;
    }
}