package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NJPWPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Explicitly set the title to "NJPW Community"
        setTitle("NJPW Community");

        // Update the TextView to reflect this title as well
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("NJPW Community");
        }
    }

    @Override
    protected int getPageId() {
        return 303; // Unique identifier for NJPW posts
    }
}

