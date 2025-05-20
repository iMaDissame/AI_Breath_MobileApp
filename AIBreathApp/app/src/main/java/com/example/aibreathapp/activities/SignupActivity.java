package com.example.aibreathapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aibreathapp.R;
import com.example.aibreathapp.api.ApiClient;
import com.example.aibreathapp.api.ApiService;
import com.example.aibreathapp.models.RegisterRequest;
import com.example.aibreathapp.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.loginTextView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Set up click listeners
        signupButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> finish()); // Go back to login
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Create register request - match Django field names
        RegisterRequest registerRequest = new RegisterRequest(username, email, password, confirmPassword);

        // Make API call
        Call<RegisterResponse> call = apiService.register(registerRequest);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                // Log response for debugging
                Log.d(TAG, "Signup response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();

                    if (registerResponse.isSuccess()) {
                        // Registration successful
                        Toast.makeText(SignupActivity.this,
                                "Registration successful! Please login.",
                                Toast.LENGTH_SHORT).show();

                        // Return to login screen
                        finish();
                    } else {
                        // Registration failed with message
                        String error = registerResponse.getError() != null ?
                                registerResponse.getError() : "Registration failed";
                        Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error response from server
                    Toast.makeText(SignupActivity.this,
                            "Registration failed. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                Log.e(TAG, "Registration error", t);
                Toast.makeText(SignupActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}