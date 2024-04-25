package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class NFLPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("NFL Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("NFL Community");
        }
    }

    @Override
    protected int getPageId() {
        return 105; // Unique identifier for NFL posts
    }
}

