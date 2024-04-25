package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class PLPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Premier League Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("Premier League Community");
        }
    }

    @Override
    protected int getPageId() {
        return 107; // Unique identifier for Premier League posts
    }
}
