package com.schoolmanager;

import java.sql.Connection;

import com.schoolmanager.config.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        
        try (Connection connection = DatabaseConnection.getConnection()) {

            System.out.println("Database connection successful!");
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}