package com.example.ai_breath_mobile_app.dao;

import com.example.ai_breath_mobile_app.beans.User;
import com.example.ai_breath_mobile_app.utils.MysqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    // Add a new user
    public String addUser(User user) {
        return MysqlHelper.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender()
        );
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = MysqlHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("gender")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    // Validate login credentials
    public boolean validateLogin(String username, String password) {
        return MysqlHelper.authenticateUser(username, password);
    }
}