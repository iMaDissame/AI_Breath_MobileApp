package com.example.aibreathapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aibreathapp.R;
import com.example.aibreathapp.adapters.DoctorAdapter;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.Doctor;
import com.example.aibreathapp.models.DoctorFilters;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorSearchFragment extends Fragment {

    private static final String TAG = "DoctorSearchFragment";

    private Spinner specialtySpinner;
    private Spinner citySpinner;
    private Button searchButton;
    private RecyclerView doctorsRecyclerView;
    private ProgressBar progressBar;

    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorsList = new ArrayList<>();
    private ApiService apiService;

    private List<String> specialties = new ArrayList<>();
    private List<String> cities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_search, container, false);

        // Initialize UI components
        specialtySpinner = view.findViewById(R.id.specialtySpinner);
        citySpinner = view.findViewById(R.id.citySpinner);
        searchButton = view.findViewById(R.id.searchButton);
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        // Setup RecyclerView - make sure this is correct!
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorsList = new ArrayList<>();  // Make sure list is initialized
        doctorAdapter = new DoctorAdapter(getContext(), doctorsList);
        doctorsRecyclerView.setAdapter(doctorAdapter);

        // Log to verify the adapter is set
        Log.d(TAG, "RecyclerView setup complete, adapter set");

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize spinner data
        specialties = new ArrayList<>();
        cities = new ArrayList<>();

        // Add default options
        specialties.add("All Specialties");
        cities.add("All Cities");

        // Setup spinners
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, specialties);
        specialtySpinner.setAdapter(specialtyAdapter);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, cities);
        citySpinner.setAdapter(cityAdapter);

        // Load filters
        loadDoctorFilters();

        // Search button click listener
        searchButton.setOnClickListener(v -> {
            searchDoctors();
        });

        return view;
    }
    private void loadDoctorFilters() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getDoctorFilters().enqueue(new Callback<DoctorFilters>() {
            @Override
            public void onResponse(Call<DoctorFilters> call, Response<DoctorFilters> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Log full response
                    Log.d(TAG, "Doctor filters response successful");

                    try {
                        Log.d(TAG, "Raw response: " + new com.google.gson.Gson().toJson(response.body()));
                    } catch (Exception e) {
                        Log.e(TAG, "Error serializing response", e);
                    }

                    List<String> apiSpecialties = response.body().getSpecialties();
                    List<String> apiCities = response.body().getCities();

                    Log.d(TAG, "Specialties: " + (apiSpecialties != null ? apiSpecialties.toString() : "null"));
                    Log.d(TAG, "Cities: " + (apiCities != null ? apiCities.toString() : "null"));

                    // Clear previous data
                    specialties.clear();
                    cities.clear();

                    // Add default options first
                    specialties.add("All Specialties");
                    cities.add("All Cities");

                    // Then add API data if available
                    if (apiSpecialties != null && !apiSpecialties.isEmpty()) {
                        specialties.addAll(apiSpecialties);
                    }
                    if (apiCities != null && !apiCities.isEmpty()) {
                        cities.addAll(apiCities);
                    }

                    // Update spinners
                    ((ArrayAdapter<?>) specialtySpinner.getAdapter()).notifyDataSetChanged();
                    ((ArrayAdapter<?>) citySpinner.getAdapter()).notifyDataSetChanged();

                } else {
                    // Log error details
                    try {
                        Log.e(TAG, "Error code: " + response.code());
                        if (response.errorBody() != null) {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error response", e);
                    }

                    Toast.makeText(getContext(), "Failed to load filters: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorFilters> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Network failure", t);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchDoctors() {
        progressBar.setVisibility(View.VISIBLE);

        // Get selected values safely
        String specialtyValue = specialtySpinner.getSelectedItem() != null ?
                specialtySpinner.getSelectedItem().toString() : null;
        String cityValue = citySpinner.getSelectedItem() != null ?
                citySpinner.getSelectedItem().toString() : null;

        // Convert "All X" to null to match Django behavior
        if ("All Specialties".equals(specialtyValue)) {
            specialtyValue = null;
        }
        if ("All Cities".equals(cityValue)) {
            cityValue = null;
        }

        // Log what we're sending
        Log.d(TAG, "Searching with Specialiter=" + specialtyValue + ", ville=" + cityValue);

        apiService.searchDoctors(specialtyValue, cityValue).enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Log complete response
                        try {
                            String jsonResponse = new com.google.gson.Gson().toJson(response.body());
                            Log.d(TAG, "Response body: " + jsonResponse);
                            Log.d(TAG, "Found " + response.body().size() + " doctors");
                        } catch (Exception e) {
                            Log.e(TAG, "Error serializing response", e);
                        }

                        // Clear and update list
                        doctorsList.clear();
                        doctorsList.addAll(response.body());

                        // Debug each doctor in the list
                        for (int i = 0; i < doctorsList.size(); i++) {
                            Doctor doctor = doctorsList.get(i);
                            Log.d(TAG, "Doctor " + i + ": " + doctor.getDoctorName() +
                                    ", Specialty: " + doctor.getSpeciality() +
                                    ", City: " + doctor.getCity());
                        }

                        doctorAdapter.notifyDataSetChanged();

                        if (doctorsList.isEmpty()) {
                            Toast.makeText(getContext(), "No doctors found with these filters", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                        Toast.makeText(getContext(), "No data received from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log error
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error code: " + response.code() + ", Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error response", e);
                    }

                    Toast.makeText(getContext(), "Failed to search doctors: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Network failure", t);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}