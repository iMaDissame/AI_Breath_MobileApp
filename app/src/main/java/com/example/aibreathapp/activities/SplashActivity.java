package com.example.aibreathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aibreathapp.R;
import com.example.aibreathapp.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Create a handler to delay the transition to the next screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = new SessionManager(SplashActivity.this);

                // Check if the app is started for the first time
                if (sessionManager.isFirstTimeLaunch()) {
                    // Show introduction screen
                    startActivity(new Intent(SplashActivity.this, IntroductionActivity.class));
                } else {
                    // Go directly to MainActivity without login
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, SPLASH_DELAY);
    }
}