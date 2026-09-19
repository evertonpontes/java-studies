package com.schoolmanager.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        Properties props = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                    "database.properties not found"
                );
            }

            props.load(input);
            return props;
            
        } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to load database.properties",
                e
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }

}
