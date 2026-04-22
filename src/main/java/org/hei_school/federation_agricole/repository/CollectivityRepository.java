package org.hei_school.federation_agricole.repository;

import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
                    ps2.setInt(2, m.getId());
                    ps2.addBatch();
                }
                ps2.executeBatch();

                // 3
                PreparedStatement ps3 = conn.prepareStatement(
                        "INSERT INTO collectivity_structure VALUES (?, ?, ?, ?, ?)");
                ps3.setString(1, c.getId());
                ps3.setInt(2, c.getStructure().getPresident().getId());
                ps3.setInt(3, c.getStructure().getVicePresident().getId());
                ps3.setInt(4, c.getStructure().getTreasurer().getId());
                ps3.setInt(5, c.getStructure().getSecretary().getId());
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

    public Optional<Collectivity> findById(String id) {

        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM collectivities WHERE id = ?");
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Collectivity c = new Collectivity();
                c.setId(rs.getString("id"));
                c.setLocation(rs.getString("location"));
                c.setNumber(rs.getString("number"));
                c.setName(rs.getString("name"));
                return Optional.of(c);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByName(String name) {
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM collectivities WHERE name = ?");
            ps.setString(1, name);

            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByNumber(String number) {
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM collectivities WHERE number = ?");
            ps.setString(1, number);

            return ps.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIdentity(String id, String number, String name) {

        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE collectivities SET number = ?, name = ? WHERE id = ?");

            ps.setString(1, number);
            ps.setString(2, name);
            ps.setString(3, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
