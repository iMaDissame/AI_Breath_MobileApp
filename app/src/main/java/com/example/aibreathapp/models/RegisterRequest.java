package com.example.aibreathapp.models;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String password2;

    public RegisterRequest(String username, String email, String password, String password2) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }
}