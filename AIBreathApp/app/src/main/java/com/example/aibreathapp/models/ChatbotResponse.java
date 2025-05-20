package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class ChatbotResponse {
    @SerializedName("response")
    private String response;

    @SerializedName("error")
    private String error;

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}