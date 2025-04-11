package com.example.ai_breath_mobile_app.activities;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_breath_mobile_app.R;
import com.example.ai_breath_mobile_app.activities.LoginActivity;
import com.example.ai_breath_mobile_app.beans.User;
import com.example.ai_breath_mobile_app.services.UserService;

public class RegisterActivity extends AppCompatActivity {

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UserService
        userService = new UserService();

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Spinner genderSpinner = findViewById(R.id.genderSpinner); // Spinner for gender
        Button registerButton = findViewById(R.id.registerButton);

        // Populate the Spinner with gender options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options, // Array of gender options
                android.R.layout.simple_spinner_item // Default layout for spinner items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Default dropdown layout
        genderSpinner.setAdapter(adapter);

        // Set white text color for the selected item in the Spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.WHITE); // Set white text color
                    ((TextView) view).setGravity(Gravity.CENTER_VERTICAL | Gravity.START); // Align text properly
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set up the register button click listener
        registerButton.setOnClickListener(v -> {
            String enteredFirstName = firstName.getText().toString().trim();
            String enteredLastName = lastName.getText().toString().trim();
            String enteredUsername = username.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();
            String enteredGender = genderSpinner.getSelectedItem().toString(); // Get selected gender

            // Validate input
            if (enteredFirstName.isEmpty() || enteredLastName.isEmpty() || enteredUsername.isEmpty() ||
                    enteredPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new user
            User newUser = new User(
                    enteredUsername,
                    enteredPassword,
                    enteredFirstName,
                    enteredLastName,
                    0, // Default age
                    0.0, // Default height
                    0.0, // Default weight
                    enteredGender
            );

            // Register the user
            String result = userService.registerUser(newUser);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            // Navigate back to login screen
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the registration activity
        });
    }
}