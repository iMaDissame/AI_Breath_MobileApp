package com.example.aibreathapp.activities;

import android.content.Intent;
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
import com.example.aibreathapp.models.LoginRequest;
import com.example.aibreathapp.models.LoginResponse;
import com.example.aibreathapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextView;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupTextView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Set up click listeners
        loginButton.setOnClickListener(v -> loginUser());
        signupTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Create login request - match Django fields
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Make API call
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                // Log response for debugging
                Log.d(TAG, "Login response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // Login successful, store minimal info
                        sessionManager.createLoginSession(
                                loginResponse.getUsername(),
                                username,
                                password,
                                loginResponse.getUserId()
                        );

                        // Show success message
                        Toast.makeText(LoginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT).show();

                        // Return to MainActivity
                        finish();
                    } else {
                        // Login failed with message from server
                        String error = loginResponse.getError() != null ?
                                loginResponse.getError() : "Login failed";
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error response from server
                    Toast.makeText(LoginActivity.this,
                            "Login failed. Check your credentials.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                Log.e(TAG, "Login error", t);
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}