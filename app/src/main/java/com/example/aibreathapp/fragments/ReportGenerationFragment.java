package com.example.aibreathapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aibreathapp.R;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.Document;
import com.example.aibreathapp.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportGenerationFragment extends Fragment {
    private static final String TAG = "ReportGeneration";

    private ImageView xrayImageView;
    private EditText patientFirstNameEditText;
    private EditText patientLastNameEditText;
    private EditText notesEditText;
    private Button generateReportButton;
    private ProgressBar progressBar;

    private ApiService apiService;
    private Uri imageUri;
    private String prediction;
    private float confidence;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_generation, container, false);

        // Initialize UI components
        xrayImageView = view.findViewById(R.id.xrayImageView);
        patientFirstNameEditText = view.findViewById(R.id.patientFirstNameEditText);
        patientLastNameEditText = view.findViewById(R.id.patientLastNameEditText);
        notesEditText = view.findViewById(R.id.notesEditText);
        generateReportButton = view.findViewById(R.id.generateReportButton);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            String imageUriStr = args.getString("imageUri");
            if (imageUriStr != null) {
                imageUri = Uri.parse(imageUriStr);
                Log.d(TAG, "Image URI received: " + imageUri);

                // Load the image
                Glide.with(this)
                        .load(imageUri)
                        .into(xrayImageView);
            }

            prediction = args.getString("prediction", "");
            confidence = args.getFloat("confidence", 0f);

            // Pre-fill the notes field with the prediction result
            String notes = "Prediction Result: " + prediction +
                    "\nConfidence: " + String.format("%.2f%%", confidence) +
                    "\n\nPlease add your medical notes here...";
            notesEditText.setText(notes);
        }

        // Set up click listener
        generateReportButton.setOnClickListener(v -> {
            Log.d(TAG, "Generate report button clicked");
            generateReport();
        });

        return view;
    }

    private void generateReport() {
        // Get user inputs
        String firstName = patientFirstNameEditText.getText().toString().trim();
        String lastName = patientLastNameEditText.getText().toString().trim();
        String description = notesEditText.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter patient name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter case description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        generateReportButton.setEnabled(false);

        try {
            // Convert Uri to File with improved method
            File imageFile = createFileFromUri(imageUri);

            if (imageFile == null) {
                Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                generateReportButton.setEnabled(true);
                return;
            }

            Log.d(TAG, "Image file created for report: " + imageFile.getAbsolutePath() + " (size: " + imageFile.length() + " bytes)");

            // Create request parts
            RequestBody firstNamePart = RequestBody.create(MediaType.parse("text/plain"), firstName);
            RequestBody lastNamePart = RequestBody.create(MediaType.parse("text/plain"), lastName);
            RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

            // Ensure we have the correct MIME type
            String mimeType = requireContext().getContentResolver().getType(imageUri);
            if (mimeType == null) mimeType = "image/jpeg"; // Fallback

            // Create image part
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(mimeType),
                    imageFile);

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                    "image_file",
                    imageFile.getName(),
                    requestFile);

            Log.d(TAG, "Sending generate report request");

            // Call API
            apiService.generateReport(
                    imagePart,
                    firstNamePart,
                    lastNamePart,
                    descriptionPart
            ).enqueue(new Callback<Document>() {
                @Override
                public void onResponse(Call<Document> call, Response<Document> response) {
                    progressBar.setVisibility(View.GONE);
                    generateReportButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        Document document = response.body();
                        Log.d(TAG, "Report generated successfully: " + document.getReportUrl());

                        Toast.makeText(requireContext(),
                                "Report generated successfully",
                                Toast.LENGTH_SHORT).show();

                        // Navigate back to the previous screen
                        getParentFragmentManager().popBackStack();
                    } else {
                        String errorMessage = "Failed to generate report";
                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string();
                                Log.e(TAG, "API error: " + errorMessage);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Document> call, Throwable t) {
                    Log.e(TAG, "API call failed", t);
                    progressBar.setVisibility(View.GONE);
                    generateReportButton.setEnabled(true);
                    Toast.makeText(requireContext(),
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error generating report", e);
            progressBar.setVisibility(View.GONE);
            generateReportButton.setEnabled(true);
            Toast.makeText(requireContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // Improved file creation from URI
    private File createFileFromUri(Uri uri) {
        try {
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(requireContext().getCacheDir(), fileName);

            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                if (inputStream == null) return null;

                byte[] buffer = new byte[4096]; // 4KB buffer
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                return outputFile;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating file from URI", e);
            return null;
        }
    }
}