package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {

    private final DataSource dataSource;

    public TransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityTransaction> findByCollectivityAndPeriod(
            String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT t.id, t.collectivity_id, t.member_id, t.account_id,
                   t.amount, t.payment_mode, t.creation_date,
                   m.first_name, m.last_name, m.email, m.occupation,
                   m.gender, m.address, m.profession, m.phone_number, m.birth_date,
                   fa.id as fa_id, fa.amount as fa_amount, fa.type as fa_type,
                   fa.holder_name, fa.mobile_number, fa.service_name,
                   fa.bank_name, fa.bank_code, fa.branch_code, fa.account_number, fa.key_rib
            FROM transactions t
            LEFT JOIN members m ON m.id = t.member_id
            LEFT JOIN financial_accounts fa ON fa.id = t.account_id
            WHERE t.collectivity_id = ?
              AND t.creation_date BETWEEN ? AND ?
        """;
        List<CollectivityTransaction> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapFull(rs));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(CollectivityTransaction tx) {
        if (tx.getId() == null) tx.setId(UUID.randomUUID().toString());
        String sql = """
            INSERT INTO transactions (id, collectivity_id, amount, account_id, member_id, payment_mode, creation_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tx.getId());
            ps.setString(2, tx.getCollectivityId());
            ps.setDouble(3, tx.getAmount());
            ps.setString(4, tx.getAccountCredited() != null ? tx.getAccountCredited().getId() : null);
            ps.setString(5, tx.getMemberDebited() != null ? tx.getMemberDebited().getId() : null);
            ps.setString(6, tx.getPaymentMode());
            ps.setDate(7, Date.valueOf(tx.getCreationDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CollectivityTransaction mapFull(ResultSet rs) throws Exception {
        CollectivityTransaction t = new CollectivityTransaction();
        t.setId(rs.getString("id"));
        t.setCollectivityId(rs.getString("collectivity_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setPaymentMode(rs.getString("payment_mode"));
        t.setCreationDate(rs.getDate("creation_date").toLocalDate());

        // memberDebited
        MemberEntity m = new MemberEntity();
        m.setId(rs.getString("member_id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        m.setEmail(rs.getString("email"));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getInt("phone_number"));
        String occ = rs.getString("occupation");
        if (occ != null) m.setOccupation(org.hei_school.federation_agricole.entity.MemberOccupation.valueOf(occ));
        String gen = rs.getString("gender");
        if (gen != null) m.setGender(org.hei_school.federation_agricole.entity.GenderEnum.valueOf(gen));
        Date bd = rs.getDate("birth_date");
        if (bd != null) m.setBirthDate(bd.toLocalDate());
        t.setMemberDebited(m);

        // accountCredited — type discriminant
        String faType = rs.getString("fa_type");
        if (faType != null) {
            org.hei_school.federation_agricole.entity.FinancialAccount fa;
            if ("MOBILE".equalsIgnoreCase(faType)) {
                MobileBankingAccount mob = new MobileBankingAccount();
                mob.setHolderName(rs.getString("holder_name"));
                mob.setMobileBankingService(rs.getString("service_name"));
                mob.setMobileNumber(rs.getString("mobile_number"));
                fa = mob;
            } else if ("BANK".equalsIgnoreCase(faType)) {
                BankAccount bank = new BankAccount();
                bank.setHolderName(rs.getString("holder_name"));
                bank.setBankName(rs.getString("bank_name"));
                bank.setBankCode(rs.getString("bank_code"));
                fa = bank;
            } else {
                fa = new CashAccount();
            }
            fa.setId(rs.getString("fa_id"));
            fa.setAmount(rs.getDouble("fa_amount"));
            t.setAccountCredited(fa);
        }

        return t;
    }
}
