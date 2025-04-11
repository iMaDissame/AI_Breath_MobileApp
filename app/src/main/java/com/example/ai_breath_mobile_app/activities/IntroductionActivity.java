package com.example.ai_breath_mobile_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_breath_mobile_app.R;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        CheckBox checkBoxAcceptTerms = findViewById(R.id.checkBoxAcceptTerms);
        Button buttonGetStarted = findViewById(R.id.buttonGetStarted);

        checkBoxAcceptTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonGetStarted.setEnabled(isChecked);
            if (isChecked) {
                buttonGetStarted.setBackgroundTintList(getResources().getColorStateList(R.color.getstarted)); // Change to your active button color
            } else {
                buttonGetStarted.setBackgroundTintList(getResources().getColorStateList(R.color.gray));
            }
        });

        buttonGetStarted.setOnClickListener(v -> {
            if (checkBoxAcceptTerms.isChecked()) {
                startActivity(new Intent(IntroductionActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
