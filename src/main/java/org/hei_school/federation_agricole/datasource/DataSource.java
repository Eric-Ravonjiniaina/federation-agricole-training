package org.hei_school.federation_agricole.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSource {

    @Bean
    public Connection getConnection() {
        try {
            String jdbcURl = "jdbc:postgresql://localhost:5432/federation_agricole_final";
            String user = "postgres";
            String password = "notYUSHIRO";
            return DriverManager.getConnection(jdbcURl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
