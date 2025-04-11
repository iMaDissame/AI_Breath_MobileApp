package com.example.ai_breath_mobile_app.services;

import com.example.ai_breath_mobile_app.beans.User;
import com.example.ai_breath_mobile_app.dao.UserDao;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    // Register a new user
    public String registerUser(User user) {
        return userDao.addUser(user);
    }

    // Authenticate a user
    public boolean authenticateUser(String username, String password) {
        return userDao.validateLogin(username, password);
    }

    // Get user details by username
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}