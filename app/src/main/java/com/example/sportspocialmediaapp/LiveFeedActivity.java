package com.example.sportspocialmediaapp;



import android.os.Bundle;
import android.widget.TextView;

public class LiveFeedActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Live Feed");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("Live Feed");
        }
    }

    @Override
    protected int getPageId() {
        return 120; // Unique identifier for Bundesliga posts
    }
}
