package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NBAPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Explicitly set the title to "NBA Community"
        setTitle("NBA Community");

        // Update the TextView to reflect this title as well
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("NBA Community");
        }
    }

    @Override
    protected int getPageId() {
        return 101; // Unique identifier for NBA posts
    }
}
