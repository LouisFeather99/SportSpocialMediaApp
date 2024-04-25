package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class EuroLeaguePostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("EuroLeague Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("EuroLeague Community");
        }
    }

    @Override
    protected int getPageId() {
        return 106; // Unique identifier for EuroLeague posts
    }
}
