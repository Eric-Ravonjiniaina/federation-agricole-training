package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.entity.Frequency;
import org.hei_school.federation_agricole.entity.MembershipFee;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MembershipFeeRepository {

    private final DataSource dataSource;

    public MembershipFeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MembershipFee> findByCollectivity(String collectivityId) {

        String sql = "SELECT id, collectivity_id, eligible_from, frequency, amount, label, status " +
                "FROM membership_fees WHERE collectivity_id = ?";

        List<MembershipFee> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(map(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAll(List<MembershipFee> fees) {

        String sql = """
            INSERT INTO membership_fees
            (id, collectivity_id, eligible_from, frequency, amount, label, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (MembershipFee f : fees) {
                ps.setString(1, f.getId());
                ps.setString(2, f.getCollectivityId());
                ps.setDate(3, Date.valueOf(f.getEligibleFrom()));
                ps.setString(4, f.getFrequency().name());
                ps.setDouble(5, f.getAmount());
                ps.setString(6, f.getLabel());
                ps.setString(7, f.getStatus());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MembershipFee map(ResultSet rs) throws Exception {
        MembershipFee f = new MembershipFee();

        f.setId(rs.getString("id"));
        f.setCollectivityId(rs.getString("collectivity_id"));
        f.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
        f.setFrequency(Frequency.valueOf(rs.getString("frequency")));
        f.setAmount(rs.getDouble("amount"));
        f.setLabel(rs.getString("label"));
        f.setStatus(rs.getString("status"));

        return f;
    }
}
