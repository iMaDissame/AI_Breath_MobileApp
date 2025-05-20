package com.example.aibreathapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AIBreathAppPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_AUTH_USERNAME = "authUsername";
    private static final String KEY_AUTH_PASSWORD = "authPassword";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String authUsername, String authPassword, int userId) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_AUTH_USERNAME, authUsername);
        editor.putString(KEY_AUTH_PASSWORD, authPassword);
        editor.putInt(KEY_USER_ID, userId);

        // Use username for name and email if not provided
        editor.putString(KEY_NAME, username);
        editor.putString(KEY_EMAIL, username + "@example.com");

        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "Guest");
    }

    public String getAuthUsername() {
        return pref.getString(KEY_AUTH_USERNAME, "");
    }

    public String getAuthPassword() {
        return pref.getString(KEY_AUTH_PASSWORD, "");
    }

    // Alias methods for backward compatibility
    public String getPassword() {
        return getAuthPassword();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "guest@example.com");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "Guest User");
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public void logout() {
        // Preserve first-time launch status
        boolean isFirstTime = isFirstTimeLaunch();

        // Clear all data
        editor.clear();
        editor.apply();

        // Restore first-time status
        setFirstTimeLaunch(isFirstTime);
    }

    // Methods for first time launch
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }
}