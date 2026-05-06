package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.dto.CollectivityLocalStatistics;
import org.hei_school.federation_agricole.dto.MemberDescription;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import javax.sql.DataSource;

public class StatisticsRepository {
    private final DataSource dataSource;

    public StatisticsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityLocalStatistics> getStatistics(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
        SELECT m.id, m.first_name, m.last_name, m.email, m.occupation,
               SUM(CASE WHEN p.status = 'PAID' THEN p.amount ELSE 0 END) as earned,
               SUM(CASE WHEN p.status = 'UNPAID' AND mf.activity_status = 'ACTIVE' THEN mf.amount ELSE 0 END) as unpaid
        FROM members m
        LEFT JOIN payments p ON m.id = p.member_id
        LEFT JOIN membership_fees mf ON p.fee_id = mf.id
        WHERE m.collectivity_id = ? 
          AND p.creation_date BETWEEN ? AND ?
        GROUP BY m.id, m.first_name, m.last_name, m.email, m.occupation
    """;

        List<CollectivityLocalStatistics> stats = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, collectivityId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MemberDescription member = new MemberDescription(
                            rs.getString("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"), rs.getString("occupation")
                    );
                    stats.add(new CollectivityLocalStatistics(member, rs.getDouble("earned"), rs.getDouble("unpaid")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur base de données", e);
        }
        return stats;
    }
}