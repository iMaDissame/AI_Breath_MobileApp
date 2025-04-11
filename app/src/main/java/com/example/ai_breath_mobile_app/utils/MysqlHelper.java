package com.example.ai_breath_mobile_app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlHelper {

    private static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/aibreath";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Method to get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Connecting to database...");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            throw e; // Re-throw the exception
        }
    }

    // Method to register a new user
    public static String registerUser(String username, String password, String firstName, String lastName, String gender) {
        String query = "INSERT INTO users (username, password, first_name, last_name, gender) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, gender);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Registration successful!" : "Registration failed!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Method to authenticate a user
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a matching user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}