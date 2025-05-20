package com.example.aibreathapp.models;

public class HeartbeatRequest {
    private String age;
    private String gender;
    private String heartRate; // Nouveau champ pour la fréquence cardiaque

    public HeartbeatRequest(String age, String gender, String heartRate) {
        this.age = age;
        this.gender = gender;
        this.heartRate = heartRate;
    }

    // Getters
    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHeartRate() {
        return heartRate;
    }
}