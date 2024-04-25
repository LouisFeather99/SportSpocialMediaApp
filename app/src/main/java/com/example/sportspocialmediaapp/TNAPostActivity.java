package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TNAPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Explicitly set the title to "TNA Community"
        setTitle("TNA Community");

        // Update the TextView to reflect this title as well
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("TNA Community");
        }
    }

    @Override
    protected int getPageId() {
        return 304; // Unique identifier for TNA posts
    }
}


