package com.example.ai_breath_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Locate the logo ImageView
        ImageView logoImage = findViewById(R.id.logoImage);

        // Create a ScaleAnimation for breathing effect
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, // Start and end scaling for X-axis
                1.0f, 1.2f, // Start and end scaling for Y-axis
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(2000); // Duration of one cycle (2000ms = 2s)
        scaleAnimation.setRepeatCount(Animation.INFINITE); // Repeat infinitely
        scaleAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end of cycle

        // Create an AlphaAnimation for fade in and fade out effect
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000); // Duration of one cycle (2000ms = 2s)
        alphaAnimation.setRepeatCount(Animation.INFINITE); // Repeat infinitely
        alphaAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end of cycle

        // Start the animations
        logoImage.startAnimation(scaleAnimation);
        logoImage.startAnimation(alphaAnimation);

        // Handler to start MainActivity after some delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
            startActivity(intent);
            finish();
        }, 6500); // Delay for 8 seconds before moving to MainActivity to allow breathing effect to be seen
    }
}
