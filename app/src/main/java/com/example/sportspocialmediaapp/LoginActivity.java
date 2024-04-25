package com.example.sportspocialmediaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Activity responsible for handling user login.
public class LoginActivity extends AppCompatActivity {

    // UI elements
    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // Initialize database helper
        final AppDbHelper dbHelper = new AppDbHelper(this);

        // Check if user is already logged in
        checkUserSession();

        // Set click listener for login button
        loginButton.setOnClickListener(view -> {
            // Get username and password from input fields
            String username = loginUsername.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            // Validate user credentials
            Bundle userData = dbHelper.validateUser(username, password);
            if (userData != null) {
                // If credentials are valid, perform login
                onLoginSuccess(userData, username);
            } else {
                // If credentials are invalid, display error message
                Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for signup redirect text
        signupRedirectText.setOnClickListener(view -> {
            // Redirect to RegisterActivity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    // Method to handle successful login
    private void onLoginSuccess(Bundle userData, String username) {
        // Store login status and user data in SharedPreferences
        SharedPreferences sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("username", username); // Save username consistently
        editor.putString("name", userData.getString("name")); // Saving user's name
        editor.putString("email", userData.getString("email"));
        editor.apply();

        // Redirect to MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to check if user is already logged in
    private void checkUserSession() {
        SharedPreferences sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // If user is already logged in, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
