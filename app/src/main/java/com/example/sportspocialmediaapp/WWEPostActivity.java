package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WWEPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Explicitly set the title to "WWE Community"
        setTitle("WWE Community");

        // Update the TextView to reflect this title as well
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("WWE Community");
        }
    }

    @Override
    protected int getPageId() {
        return 301; // Unique identifier for WWE posts
    }
}



