package com.example.aibreathapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DoctorFilters {
    // Exactly match the field names in your Django API response
    @SerializedName("filtered_Specialiter")
    private List<String> specialties;

    @SerializedName("filtered_cities")
    private List<String> cities;

    public List<String> getSpecialties() {
        return specialties;
    }

    public List<String> getCities() {
        return cities;
    }
}