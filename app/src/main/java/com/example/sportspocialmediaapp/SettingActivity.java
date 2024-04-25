package com.example.sportspocialmediaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private ImageButton messageButton, newPostButton, profileButton, HomeButton;
    private Button btnLogout, btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        // Find buttons by their IDs
        Button btnUpdateEmail = findViewById(R.id.btnChangeEmail);
        btnLogout = findViewById(R.id.btnLogout);
        messageButton = findViewById(R.id.button_nav_to_message);
        newPostButton = findViewById(R.id.button_new_post);
        profileButton = findViewById(R.id.button_nav_to_profile);
        HomeButton = findViewById(R.id.button_nav_to_search);
        btnChangePass = findViewById(R.id.btnChangePassword);

        // Set onClickListeners
        btnUpdateEmail.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, UpdateEmail.class)));
        messageButton.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, CommunityActivity.class)));
        newPostButton.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, LiveFeedActivity.class)));
        profileButton.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, SettingActivity.class)));
        HomeButton.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, MainActivity.class)));
        btnChangePass.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, UpdatePass.class)));

        // Handling the logout button click
        btnLogout.setOnClickListener(v -> {
            logoutUser();
        });
    }

    private void logoutUser() {
        // Clear all stored user data or sessions
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to LoginActivity and clear all previous activities from the stack
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
