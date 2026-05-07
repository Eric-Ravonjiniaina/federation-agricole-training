package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.controller.dto.CollectivityLocalStatistics;
import org.hei_school.federation_agricole.controller.dto.MemberDescription;
import org.hei_school.federation_agricole.datasource.DataSource;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {
    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityLocalStatistics> getStatistics(
            String collectivityId, LocalDate from, LocalDate to) {

        String sql = "SELECT " +
                "m.id, m.first_name, m.last_name, m.email, m.occupation, " +
                "COALESCE(SUM(CASE WHEN mp.creation_date BETWEEN ? AND ? THEN mp.amount ELSE 0 END), 0) AS earned, " +
                "CASE " +
                "    WHEN COALESCE(SUM(CASE WHEN mp.creation_date BETWEEN ? AND ? THEN mp.amount ELSE 0 END), 0) < " +
                "         (SELECT COALESCE(SUM( " +
                "             CASE mf.frequency " +
                "                 WHEN 'PUNCTUALLY' THEN CASE WHEN mf.eligible_from BETWEEN ? AND ? THEN mf.amount ELSE 0 END " +
                "                 WHEN 'MONTHLY' THEN mf.amount * (EXTRACT(YEAR FROM AGE(?, mf.eligible_from)) * 12 + EXTRACT(MONTH FROM AGE(?, mf.eligible_from)) + 1) " +
                "                 WHEN 'ANNUALLY' THEN mf.amount * (EXTRACT(YEAR FROM AGE(?, mf.eligible_from)) + 1) " +
                "                 ELSE 0 " +
                "             END), 0) " +
                "          FROM membership_fee mf " +
                "          WHERE mf.collectivity_id = ? AND mf.status = 'ACTIVE') " +
                "    THEN " +
                "         (SELECT COALESCE(SUM( " +
                "             CASE mf.frequency " +
                "                 WHEN 'PUNCTUALLY' THEN CASE WHEN mf.eligible_from BETWEEN ? AND ? THEN mf.amount ELSE 0 END " +
                "                 WHEN 'MONTHLY' THEN mf.amount * (EXTRACT(YEAR FROM AGE(?, mf.eligible_from)) * 12 + EXTRACT(MONTH FROM AGE(?, mf.eligible_from)) + 1) " +
                "                 WHEN 'ANNUALLY' THEN mf.amount * (EXTRACT(YEAR FROM AGE(?, mf.eligible_from)) + 1) " +
                "                 ELSE 0 " +
                "             END), 0) " +
                "          FROM membership_fee mf " +
                "          WHERE mf.collectivity_id = ? AND mf.status = 'ACTIVE') " +
                "         - COALESCE(SUM(CASE WHEN mp.creation_date BETWEEN ? AND ? THEN mp.amount ELSE 0 END), 0) " +
                "    ELSE 0 " +
                "END AS unpaid " +
                "FROM member m " +
                "JOIN collectivity_member cm ON m.id = cm.member_id " +
                "LEFT JOIN member_payment mp ON m.id = mp.member_debited_id " +
                "WHERE cm.collectivity_id = ? " +
                "GROUP BY m.id, m.first_name, m.last_name, m.email, m.occupation";

        List<CollectivityLocalStatistics> stats = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            stmt.setDate(3, Date.valueOf(from));
            stmt.setDate(4, Date.valueOf(to));
            stmt.setDate(5, Date.valueOf(from));
            stmt.setDate(6, Date.valueOf(to));
            stmt.setDate(7, Date.valueOf(to));
            stmt.setDate(8, Date.valueOf(to));
            stmt.setDate(9, Date.valueOf(to));
            stmt.setString(10, collectivityId);
            stmt.setDate(11, Date.valueOf(from));
            stmt.setDate(12, Date.valueOf(to));
            stmt.setDate(13, Date.valueOf(to));
            stmt.setDate(14, Date.valueOf(to));
            stmt.setDate(15, Date.valueOf(to));
            stmt.setString(16, collectivityId);
            stmt.setDate(17, Date.valueOf(from));
            stmt.setDate(18, Date.valueOf(to));
            stmt.setString(19, collectivityId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MemberDescription member = new MemberDescription(
                            rs.getString("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("occupation")
                    );
                    stats.add(new CollectivityLocalStatistics(
                            member,
                            rs.getDouble("earned"),
                            rs.getDouble("unpaid")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stats;
    }
}