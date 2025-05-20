// java/com/example/aibreathapp/activities/IntroductionActivity.java
package com.example.aibreathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aibreathapp.R;
import com.example.aibreathapp.utils.SessionManager;

public class IntroductionActivity extends AppCompatActivity {

    private CheckBox checkBoxAcceptTerms;
    private Button buttonGetStarted;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        // Initialize views
        checkBoxAcceptTerms = findViewById(R.id.checkBoxAcceptTerms);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);
        sessionManager = new SessionManager(this);

        // Set button click listener
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAcceptTerms.isChecked()) {
                    // Set first time launch to false
                    sessionManager.setFirstTimeLaunch(false);

                    // Navigate to MainActivity
                    Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(IntroductionActivity.this,
                            "Please accept the terms and conditions",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}