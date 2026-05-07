package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.controller.dto.Activity;
import org.hei_school.federation_agricole.datasource.DataSource;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ActivityRepository {
    private final DataSource dataSource;

    public ActivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Activity> saveAll(String collectivityId, List<Activity> activities) {
        String sql = "INSERT INTO \"activity\" (id, label, description, activity_date, collectivity_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Activity activity : activities) {
                pstmt.setString(1, activity.id());
                pstmt.setString(2, activity.label());
                pstmt.setString(3, activity.description());
                pstmt.setDate(4, Date.valueOf(activity.activityDate()));
                pstmt.setString(5, collectivityId);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            return activities;

        } catch (BadRequestException e) {
            throw new RuntimeException("Erreur lors de la création des activités", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}