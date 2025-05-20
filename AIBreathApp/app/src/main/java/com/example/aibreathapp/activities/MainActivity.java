// java/com/example/aibreathapp/activities/MainActivity.java
package com.example.aibreathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.aibreathapp.R;
import com.example.aibreathapp.fragments.ChatbotFragment;
import com.example.aibreathapp.fragments.DoctorSearchFragment;
import com.example.aibreathapp.fragments.HeartbeatClassificationFragment;
import com.example.aibreathapp.fragments.HomeFragment;
import com.example.aibreathapp.fragments.MedicalReportsFragment;
import com.example.aibreathapp.fragments.PneumoniaPredictionFragment;
import com.example.aibreathapp.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SessionManager sessionManager;
    private TextView navHeaderName, navHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and NavigationView
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Initialize Navigation Header Views
        View headerView = navigationView.getHeaderView(0);
        navHeaderName = headerView.findViewById(R.id.nav_header_name);
        navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        // Update UI based on login status
        updateUIBasedOnLoginStatus();

        // If it's the first time loading, show the HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_doctor_search) {
            selectedFragment = new DoctorSearchFragment();
        } else if (itemId == R.id.nav_chatbot) {
            selectedFragment = new ChatbotFragment();
        } else if (itemId == R.id.nav_pneumonia) {
            // Check if user is logged in for doctor features
            if (sessionManager.isLoggedIn()) {
                selectedFragment = new PneumoniaPredictionFragment();
            } else {
                Toast.makeText(this, "Please login to access this feature", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        } else if (itemId == R.id.nav_heartbeat) {
            if (sessionManager.isLoggedIn()) {
                selectedFragment = new HeartbeatClassificationFragment();
            } else {
                Toast.makeText(this, "Please login to access this feature", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        } else if (itemId == R.id.nav_reports) {
            if (sessionManager.isLoggedIn()) {
                selectedFragment = new MedicalReportsFragment();
            } else {
                Toast.makeText(this, "Please login to access this feature", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        } else if (itemId == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.nav_signup) {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.nav_logout) {
            sessionManager.logout();
            updateUIBasedOnLoginStatus();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            selectedFragment = new HomeFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIBasedOnLoginStatus();
    }

    private void updateUIBasedOnLoginStatus() {
        // Update navigation drawer based on login status
        if (sessionManager.isLoggedIn()) {
            // Update header
            navHeaderName.setText(sessionManager.getName());
            navHeaderEmail.setText(sessionManager.getEmail());

            // Show doctor features
            navigationView.getMenu().findItem(R.id.nav_pneumonia).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_heartbeat).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_reports).setVisible(true);

            // Show logout, hide login and signup
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
        } else {
            // Update header for guest user
            navHeaderName.setText("Guest User");
            navHeaderEmail.setText("Not logged in");

            // Hide doctor features
            navigationView.getMenu().findItem(R.id.nav_pneumonia).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_heartbeat).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_reports).setVisible(false);

            // Hide logout, show login and signup
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
        }
    }
}