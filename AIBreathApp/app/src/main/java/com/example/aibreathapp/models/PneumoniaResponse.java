package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class PneumoniaResponse {
    @SerializedName("result")
    private String result;

    @SerializedName("confidence")
    private float confidence;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("error")
    private String error;

    public String getResult() {
        return result;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "PneumoniaResponse{" +
                "result='" + result + '\'' +
                ", confidence=" + confidence +
                ", imagePath='" + imagePath + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}