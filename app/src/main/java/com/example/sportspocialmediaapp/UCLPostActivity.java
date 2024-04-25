package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.widget.TextView;

public class UCLPostActivity extends BasePostActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("UEFA Champions League Community");
        TextView titleTextView = findViewById(R.id.textViewTitle);
        if (titleTextView != null) {
            titleTextView.setText("UEFA Champions League Community")



            ;
        }
    }



    @Override
    protected int getPageId() {
        return 109; // Unique identifier for UCL posts
    }


}

