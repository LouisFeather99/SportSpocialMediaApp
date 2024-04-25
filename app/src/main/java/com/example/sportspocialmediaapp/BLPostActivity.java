package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class BLPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Bundesliga Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("Bundesliga Community");
        }
    }

    @Override
    protected int getPageId() {
        return 110; // Unique identifier for Bundesliga posts
    }
}
