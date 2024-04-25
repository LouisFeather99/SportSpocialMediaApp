package com.example.sportspocialmediaapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateEmail extends AppCompatActivity {

    private EditText newEmailEditText;
    private EditText confirmEmailEditText;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        newEmailEditText = findViewById(R.id.newEmail);
        confirmEmailEditText = findViewById(R.id.confirmNewEmail);
        updateButton = findViewById(R.id.submitChangesButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });
    }

    private void updateEmail() {
        String newEmail = newEmailEditText.getText().toString();
        String confirmEmail = confirmEmailEditText.getText().toString();

        if (newEmail.isEmpty() || confirmEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newEmail.equals(confirmEmail)) {
            Toast.makeText(this, "New email and confirm email do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the content of the last two fields match, show success message and go back to MainActivity
        Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show();
        finish(); // Finish current activity and go back to MainActivity
    }
}
