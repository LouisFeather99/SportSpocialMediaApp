package com.example.sportspocialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FootballCommunityActivity extends AppCompatActivity {

    private Button btnToggleNav;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_community);

        // Initialize section FrameLayouts
        FrameLayout section1 = findViewById(R.id.section1);
        FrameLayout section2 = findViewById(R.id.section2);
        FrameLayout section3 = findViewById(R.id.section3);
        FrameLayout section4 = findViewById(R.id.section4);

        // Set onClick listeners for sections, replace with correct activities
        section1.setOnClickListener(v -> startActivity(new Intent(FootballCommunityActivity.this, PLPostActivity.class)));
        section2.setOnClickListener(v -> startActivity(new Intent(FootballCommunityActivity.this, EFLPostActivity.class)));
        section3.setOnClickListener(v -> startActivity(new Intent(FootballCommunityActivity.this, UCLPostActivity.class)));
        section4.setOnClickListener(v -> startActivity(new Intent(FootballCommunityActivity.this, BLPostActivity.class)));



        // You can add more setup code for other components if needed
    }
}
