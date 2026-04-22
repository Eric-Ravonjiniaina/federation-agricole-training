package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CollectivityRepository {

    private final DataSource dataSource;

    public CollectivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Collectivity c) {

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try {

                // 1
                PreparedStatement ps1 = conn.prepareStatement(
                        "INSERT INTO collectivities(id, location) VALUES (?, ?)");
                ps1.setString(1, c.getId());
                ps1.setString(2, c.getLocation());
                ps1.executeUpdate();

                // 2
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO collectivity_members VALUES (?, ?)");
                for (MemberEntity m : c.getMembers()) {
                    ps2.setString(1, c.getId());
                    ps2.setString(2, m.getId());
                    ps2.addBatch();
                }
                ps2.executeBatch();

                // 3
                PreparedStatement ps3 = conn.prepareStatement(
                        "INSERT INTO collectivity_structure VALUES (?, ?, ?, ?, ?)");
                ps3.setString(1, c.getId());
                ps3.setString(2, c.getStructure().getPresident().getId());
                ps3.setString(3, c.getStructure().getVicePresident().getId());
                ps3.setString(4, c.getStructure().getTreasurer().getId());
                ps3.setString(5, c.getStructure().getSecretary().getId());
                ps3.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
