package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class HeartbeatResponse {
    @SerializedName("heart_rate")
    private int heartRate;

    @SerializedName("prediction")
    private String prediction;

    @SerializedName("error")
    private String error;

    public int getHeartRate() {
        return heartRate;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getError() {
        return error;
    }
}