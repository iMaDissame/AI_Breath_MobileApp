package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("description")
    private String description;

    @SerializedName("file")
    private String filePath;

    @SerializedName("image")
    private String imagePath;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("report_url")
    private String reportUrl;

    @SerializedName("image_url")
    private String imageUrl;

    // Getters existants
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Méthodes manquantes qu'il faut ajouter
    public String getDocumentType() {
        return "Medical Report";
    }

    public String getFileUrl() {
        return reportUrl;  // Use reportUrl as the file URL
    }

    public String getPatientName() {
        return firstName + " " + lastName;
    }

    public String getDoctorName() {
        return "AI Diagnosis System";
    }

    public String getCreatedDate() {
        return createdAt != null ? createdAt : "Unknown date";
    }

    // Pour l'affichage
    public String getPatientFullName() {
        return firstName + " " + lastName;
    }
}