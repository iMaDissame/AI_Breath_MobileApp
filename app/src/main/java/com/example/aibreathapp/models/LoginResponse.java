package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("auth_username")
    private String authUsername;

    @SerializedName("auth_password")
    private String authPassword;

    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public String getError() {
        return error;
    }
}