package com.example.sportspocialmediaapp;



import android.os.Bundle;
import android.widget.TextView;

public class LiveFeedActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("NCAA");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("NCAA");
        }
    }

    @Override
    protected int getPageId() {
        return 120; // Unique identifier for Bundesliga posts
    }
}
