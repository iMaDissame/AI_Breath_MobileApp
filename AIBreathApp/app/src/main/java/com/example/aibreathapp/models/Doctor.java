package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;

public class Doctor {
    @SerializedName("id")
    private int id;

    @SerializedName("DoctorName")
    private String doctorName;

    @SerializedName("Specialiter")
    private String speciality;

    @SerializedName("City")
    private String city;

    @SerializedName("Link")
    private String link;

    @SerializedName("Location")
    private String location;

    @SerializedName("image")
    private String image;

    // Getters
    public int getId() {
        return id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getCity() {
        return city;
    }

    public String getLink() {
        return link;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", doctorName='" + doctorName + '\'' +
                ", speciality='" + speciality + '\'' +
                ", city='" + city + '\'' +
                ", link='" + link + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}