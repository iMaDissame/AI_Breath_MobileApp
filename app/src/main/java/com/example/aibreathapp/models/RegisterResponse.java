package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("username")
    private String username;

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

    public String getError() {
        return error;
    }
}