// java/com/example/aibreathapp/fragments/HomeFragment.java
package com.example.aibreathapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aibreathapp.R;
import com.example.aibreathapp.activities.LoginActivity;
import com.example.aibreathapp.utils.SessionManager;

public class HomeFragment extends Fragment {

    private Button btnExploreFeatures;
    private Button btnLoginOrDoctorFeatures;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize SessionManager
        sessionManager = new SessionManager(requireContext());

        // Initialize UI components
        btnExploreFeatures = view.findViewById(R.id.btnExploreFeatures);
        btnLoginOrDoctorFeatures = view.findViewById(R.id.btnLoginOrDoctorFeatures);

        // Update button texts based on login status
        updateButtonText();

        // Set up click listeners
        btnExploreFeatures.setOnClickListener(v -> showFeatureSelectionDialog());
        btnLoginOrDoctorFeatures.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                showDoctorFeatureSelectionDialog();
            } else {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonText();
    }

    private void updateButtonText() {
        if (sessionManager.isLoggedIn()) {
            btnLoginOrDoctorFeatures.setText("Doctor Features");
        } else {
            btnLoginOrDoctorFeatures.setText("Login as Doctor");
        }
    }

    private void showFeatureSelectionDialog() {
        String[] features = {
                "Find a Doctor",
                "Medical Chatbot"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select a Feature");
        builder.setItems(features, (dialog, which) -> {
            Fragment selectedFragment = null;
            switch (which) {
                case 0:
                    selectedFragment = new DoctorSearchFragment();
                    break;
                case 1:
                    selectedFragment = new ChatbotFragment();
                    break;
            }

            if (selectedFragment != null) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        builder.show();
    }

    private void showDoctorFeatureSelectionDialog() {
        String[] features = {
                "Pneumonia Prediction",
                "Medical Reports",
                "Heartbeat Classification"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Doctor Feature");
        builder.setItems(features, (dialog, which) -> {
            Fragment selectedFragment = null;
            switch (which) {
                case 0:
                    selectedFragment = new PneumoniaPredictionFragment();
                    break;
                case 1:
                    selectedFragment = new MedicalReportsFragment();
                    break;
                case 2:
                    selectedFragment = new HeartbeatClassificationFragment();
                    break;
            }

            if (selectedFragment != null) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        builder.show();
    }
}