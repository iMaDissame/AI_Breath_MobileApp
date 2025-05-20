package com.example.aibreathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aibreathapp.R;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.HeartbeatRequest;
import com.example.aibreathapp.models.HeartbeatResponse;
import com.example.aibreathapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeartbeatClassificationFragment extends Fragment {

    private EditText ageEditText;
    private EditText genderEditText;
    private EditText heartRateEditText; // Nouveau champ
    private Button analyzeButton;
    private TextView heartRateTextView;
    private TextView analysisTextView;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heartbeat_classification, container, false);

        // Initialize UI components
        ageEditText = view.findViewById(R.id.ageEditText);
        genderEditText = view.findViewById(R.id.genderEditText);
        heartRateEditText = view.findViewById(R.id.heartRateEditText); // Nouveau champ
        analyzeButton = view.findViewById(R.id.analyzeButton);
        heartRateTextView = view.findViewById(R.id.heartRateTextView);
        analysisTextView = view.findViewById(R.id.analysisTextView);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Set up click listener
        analyzeButton.setOnClickListener(v -> analyzeHeartbeat());

        return view;
    }

    private void analyzeHeartbeat() {
        String age = ageEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String heartRate = heartRateEditText.getText().toString().trim();

        if (age.isEmpty() || gender.isEmpty() || heartRate.isEmpty()) {
            Toast.makeText(getContext(), "Please enter all required information", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        analyzeButton.setEnabled(false);

        // Reset previous results
        heartRateTextView.setText("");
        analysisTextView.setText("");

        // Create request object that includes the heart rate
        HeartbeatRequest request = new HeartbeatRequest(age, gender, heartRate);

        // Make API call
        apiService.classifyHeartbeat(request).enqueue(new Callback<HeartbeatResponse>() {
            @Override
            public void onResponse(Call<HeartbeatResponse> call, Response<HeartbeatResponse> response) {
                progressBar.setVisibility(View.GONE);
                analyzeButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    HeartbeatResponse heartbeatResponse = response.body();

                    // Display results
                    heartRateTextView.setText("Heart Rate: " + heartbeatResponse.getHeartRate() + " BPM");
                    analysisTextView.setText(heartbeatResponse.getPrediction());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Toast.makeText(getContext(),
                                "Error: " + errorBody,
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(),
                                "Failed to analyze heartbeat data",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<HeartbeatResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                analyzeButton.setEnabled(true);
                Toast.makeText(getContext(),
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}