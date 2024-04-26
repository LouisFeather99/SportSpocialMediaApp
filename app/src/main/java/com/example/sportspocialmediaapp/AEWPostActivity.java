package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class AEWPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Explicitly set the title to "AEW Community"
        setTitle("AEW Community");

        // Update the TextView to reflect this title as well
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("AEW Community");
        }
    }

    @Override
    protected int getPageId() {
        return 302; // Unique identifier for AEW posts
    }
}
