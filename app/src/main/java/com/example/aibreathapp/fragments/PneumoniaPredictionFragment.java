package com.example.aibreathapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aibreathapp.R;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.PneumoniaResponse;
import com.example.aibreathapp.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PneumoniaPredictionFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String TAG = "PneumoniaPrediction";

    private ImageView xrayImageView;
    private Button uploadButton;
    private Button analyzeButton;
    private Button generateReportButton;
    private TextView resultTextView;
    private TextView confidenceTextView;
    private ProgressBar progressBar;

    private Uri imageUri;
    private ApiService apiService;
    private String predictionResult;
    private float confidenceScore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pneumonia_prediction, container, false);

        // Initialize UI components
        xrayImageView = view.findViewById(R.id.xrayImageView);
        uploadButton = view.findViewById(R.id.uploadButton);
        analyzeButton = view.findViewById(R.id.analyzeButton);
        generateReportButton = view.findViewById(R.id.generateReportButton);
        resultTextView = view.findViewById(R.id.resultTextView);
        confidenceTextView = view.findViewById(R.id.confidenceTextView);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Set initial state
        analyzeButton.setEnabled(false);
        generateReportButton.setEnabled(false);
        generateReportButton.setVisibility(View.GONE);

        // Set click listeners with debug information
        uploadButton.setOnClickListener(v -> {
            Log.d(TAG, "Upload button clicked");
            Toast.makeText(requireContext(), "Selecting image...", Toast.LENGTH_SHORT).show();
            checkPermissionAndPickImage();
        });

        analyzeButton.setOnClickListener(v -> {
            Log.d(TAG, "Analyze button clicked");
            Toast.makeText(requireContext(), "Analyzing image...", Toast.LENGTH_SHORT).show();
            analyzePneumonia();
        });

        generateReportButton.setOnClickListener(v -> {
            Log.d(TAG, "Generate report button clicked");
            navigateToReportGeneration();
        });

        return view;
    }

    private void checkPermissionAndPickImage() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting permission: " + permission);
            requestPermissions(new String[]{permission}, STORAGE_PERMISSION_CODE);
        } else {
            Log.d(TAG, "Permission already granted, picking image");
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        try {
            Log.d(TAG, "Opening gallery");

            Intent intent;
            if (Build.VERSION.SDK_INT >= 33) { // Android 13+
                intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 1);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            }

            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Log.e(TAG, "Error picking image: " + e.getMessage(), e);
            // Use alternative method
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select X-Ray Image"), PICK_IMAGE_REQUEST);
            } catch (Exception ex) {
                Log.e(TAG, "Fallback error: " + ex.getMessage(), ex);
                Toast.makeText(requireContext(), "Could not open gallery: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted, picking image");
                pickImageFromGallery();
            } else {
                Log.e(TAG, "Permission denied");
                Toast.makeText(requireContext(), "Permission denied. Cannot select image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode=" + requestCode +
                ", resultCode=" + resultCode +
                ", data=" + (data != null ? "not null" : "null"));

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {
                imageUri = data.getData();
                Log.d(TAG, "Image selected: " + imageUri);

                // Get MIME type and file info for debugging
                ContentResolver resolver = requireContext().getContentResolver();
                String mimeType = resolver.getType(imageUri);
                String fileName = getFileName(imageUri);

                Log.d(TAG, "File info - Name: " + fileName + ", MIME: " + mimeType);

                // Display image
                Glide.with(this)
                        .load(imageUri)
                        .into(xrayImageView);

                // Enable analyze button
                analyzeButton.setEnabled(true);

                // Clear previous results
                resultTextView.setText("");
                confidenceTextView.setText("");
                generateReportButton.setVisibility(View.GONE);

                Toast.makeText(requireContext(), "Image selected successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Error processing selected image", e);
                Toast.makeText(requireContext(), "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Log.e(TAG, "Image selection failed or was cancelled");
            Toast.makeText(requireContext(), "Failed to select image", Toast.LENGTH_SHORT).show();
        }
    }

    // Get file name from URI
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name", e);
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void analyzePneumonia() {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        analyzeButton.setEnabled(false);

        try {
            // Convert URI to File using the improved method
            File imageFile = createFileFromUri(imageUri);

            if (imageFile == null || !imageFile.exists() || imageFile.length() == 0) {
                Log.e(TAG, "Failed to create valid file from URI");
                Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                analyzeButton.setEnabled(true);
                return;
            }

            Log.d(TAG, "Image file created: " + imageFile.getAbsolutePath() + " (size: " + imageFile.length() + " bytes)");

            // Get MIME type
            String mimeType = requireContext().getContentResolver().getType(imageUri);
            if (mimeType == null) mimeType = "image/jpeg"; // Fallback

            // Create MultipartBody.Part for image
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(mimeType),
                    imageFile);

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                    "image_file",
                    imageFile.getName(),
                    requestFile);

            Log.d(TAG, "Sending API request with image part: " + imagePart);

            // Call API
            apiService.predictPneumonia(imagePart).enqueue(new Callback<PneumoniaResponse>() {
                @Override
                public void onResponse(Call<PneumoniaResponse> call, Response<PneumoniaResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    analyzeButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        PneumoniaResponse result = response.body();
                        Log.d(TAG, "API success: " + result.toString());

                        // Save results
                        predictionResult = result.getResult();
                        confidenceScore = result.getConfidence();

                        // Display results
                        resultTextView.setText("Prediction: " + predictionResult);
                        confidenceTextView.setText("Confidence: " + String.format("%.2f%%", confidenceScore));

                        // Show report button
                        generateReportButton.setVisibility(View.VISIBLE);
                        generateReportButton.setEnabled(true);

                        Toast.makeText(requireContext(), "Analysis completed", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = "Failed to analyze image";
                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string();
                                Log.e(TAG, "API error: " + errorMessage);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PneumoniaResponse> call, Throwable t) {
                    Log.e(TAG, "API call failed", t);
                    progressBar.setVisibility(View.GONE);
                    analyzeButton.setEnabled(true);
                    Toast.makeText(requireContext(),
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error processing image", e);
            progressBar.setVisibility(View.GONE);
            analyzeButton.setEnabled(true);
            Toast.makeText(requireContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Improved file creation from URI
    private File createFileFromUri(Uri uri) {
        try {
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(requireContext().getCacheDir(), fileName);

            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                if (inputStream == null) {
                    Log.e(TAG, "Failed to open input stream from URI");
                    return null;
                }

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                Log.d(TAG, "File created successfully: " + outputFile.getAbsolutePath());

                return outputFile;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error creating file from URI", e);
            return null;
        }
    }

    private void navigateToReportGeneration() {
        Bundle args = new Bundle();
        args.putString("imageUri", imageUri.toString());
        args.putString("prediction", predictionResult);
        args.putFloat("confidence", confidenceScore);

        ReportGenerationFragment reportFragment = new ReportGenerationFragment();
        reportFragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, reportFragment)
                .addToBackStack(null)
                .commit();
    }
}