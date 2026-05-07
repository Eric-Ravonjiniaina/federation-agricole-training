package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.datasource.DataSource;
import org.hei_school.federation_agricole.entity.ActivityDayOfWeek;
import org.hei_school.federation_agricole.entity.ActivityType;
import org.hei_school.federation_agricole.entity.CollectivityActivity;
import org.hei_school.federation_agricole.entity.MemberOccupation;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ActivityRepository {
    private final DataSource dataSource;

    public ActivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CollectivityActivity> getByCollectivityId(String collectivityId) {
        String sql = "SELECT ca.id, ca.label, ca.activity_type, ca.executive_date, " +
                "ca.week_ordinal, ca.day_of_week, " +
                "aoc.occupation " +
                "FROM collectivity_activity ca " +
                "LEFT JOIN activity_occupation_concerned aoc ON ca.id = aoc.activity_id " +
                "WHERE ca.collectivity_id = ? " +
                "ORDER BY ca.id";

        List<CollectivityActivity> activities = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, collectivityId);

            try (ResultSet rs = stmt.executeQuery()) {
                String currentId = null;
                CollectivityActivity current = null;

                while (rs.next()) {
                    String id = rs.getString("id");

                    if (!id.equals(currentId)) {
                        current = new CollectivityActivity();
                        current.setId(id);
                        current.setCollectivityId(collectivityId);
                        current.setLabel(rs.getString("label"));
                        current.setActivityType(ActivityType.valueOf(rs.getString("activity_type")));
                        current.setMemberOccupationConcerned(new ArrayList<>());

                        Date execDate = rs.getDate("executive_date");
                        if (execDate != null) current.setExecutiveDate(execDate.toLocalDate());

                        String weekOrdinal = rs.getString("week_ordinal");
                        if (weekOrdinal != null) current.setWeekOrdinal(Integer.parseInt(weekOrdinal));

                        String dayOfWeek = rs.getString("day_of_week");
                        if (dayOfWeek != null) current.setDayOfWeek(ActivityDayOfWeek.valueOf(dayOfWeek));

                        activities.add(current);
                        currentId = id;
                    }

                    String occupation = rs.getString("occupation");
                    if (occupation != null) {
                        current.getMemberOccupationConcerned().add(MemberOccupation.valueOf(occupation));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur base de données", e);
        }

        return activities;
    }

    public List<CollectivityActivity> save(String collectivityId, List<CollectivityActivity> activities) {
        String sqlActivity = "INSERT INTO collectivity_activity " +
                "(id, collectivity_id, label, activity_type, executive_date, week_ordinal, day_of_week) " +
                "VALUES (?, ?, ?, ?::activity_type, ?, ?, ?::activity_day_of_week)";

        String sqlOccupation = "INSERT INTO activity_occupation_concerned " +
                "(id, activity_id, occupation) VALUES (?, ?, ?::member_occupation)";

        List<CollectivityActivity> saved = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            for (CollectivityActivity activity : activities) {
                String id = UUID.randomUUID().toString();
                activity.setId(id);
                activity.setCollectivityId(collectivityId);

                try (PreparedStatement stmt = conn.prepareStatement(sqlActivity)) {
                    stmt.setString(1, id);
                    stmt.setString(2, collectivityId);
                    stmt.setString(3, activity.getLabel());
                    stmt.setString(4, activity.getActivityType().name());

                    if (activity.getExecutiveDate() != null) {
                        stmt.setDate(5, Date.valueOf(activity.getExecutiveDate()));
                    } else {
                        stmt.setNull(5, Types.DATE);
                    }

                    if (activity.getWeekOrdinal() != null) {
                        stmt.setInt(6, activity.getWeekOrdinal());
                    } else {
                        stmt.setNull(6, Types.INTEGER);
                    }

                    if (activity.getDayOfWeek() != null) {
                        stmt.setString(7, activity.getDayOfWeek().name());
                    } else {
                        stmt.setNull(7, Types.VARCHAR);
                    }

                    stmt.executeUpdate();
                }

                if (activity.getMemberOccupationConcerned() != null) {
                    for (MemberOccupation occ : activity.getMemberOccupationConcerned()) {
                        try (PreparedStatement stmt2 = conn.prepareStatement(sqlOccupation)) {
                            stmt2.setString(1, UUID.randomUUID().toString());
                            stmt2.setString(2, id);
                            stmt2.setString(3, occ.name());
                            stmt2.executeUpdate();
                        }
                    }
                }

                saved.add(activity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur base de données", e);
        }

        return saved;
    }
}