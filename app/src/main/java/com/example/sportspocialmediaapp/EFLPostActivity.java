package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class EFLPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("English Football League Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("English Football League Community");
        }
    }

    @Override
    protected int getPageId() {
        return 108; // Unique identifier for EFL posts
    }
}
