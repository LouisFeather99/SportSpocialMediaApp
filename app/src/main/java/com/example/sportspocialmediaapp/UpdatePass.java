package com.example.sportspocialmediaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdatePass extends AppCompatActivity {
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword; // New EditText for confirming password
    private Button buttonUpdatePassword;
    private AppDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);

        dbHelper = new AppDbHelper(this); // Initialize your database helper

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword); // Initialize EditText for confirming password
        buttonUpdatePassword = findViewById(R.id.buttonUpdatePassword);

        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String username = prefs.getString("username", null); // Get the username from SharedPreferences

                if (username != null && dbHelper.updatePassword(username, newPassword)) {
                    Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close this activity and go back
                } else {
                    Toast.makeText(this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Password fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}

