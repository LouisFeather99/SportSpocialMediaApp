package com.example.sportspocialmediaapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WrestlingCommunityActivity extends AppCompatActivity {

    private Button btnToggleNav;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrestling_community);

        // Initialize section FrameLayouts
        FrameLayout section1 = findViewById(R.id.section1);
        FrameLayout section2 = findViewById(R.id.section2);
        FrameLayout section3 = findViewById(R.id.section3);
        FrameLayout section4 = findViewById(R.id.section4);

        // Set onClick listeners for sections, replace with correct activities
        section1.setOnClickListener(v -> startActivity(new Intent(WrestlingCommunityActivity.this, WWEPostActivity.class)));
        section2.setOnClickListener(v -> startActivity(new Intent(WrestlingCommunityActivity.this, AEWPostActivity.class)));
        section3.setOnClickListener(v -> startActivity(new Intent(WrestlingCommunityActivity.this, NJPWPostActivity.class)));
        section4.setOnClickListener(v -> startActivity(new Intent(WrestlingCommunityActivity.this, TNAPostActivity.class)));

        // Initialize the toggle navigation button and bottom navigation view
        bottomNav = findViewById(R.id.bottom_navigation);

        // Set an OnClickListener to toggle the visibility of the bottom navigation

        // You can add more setup code for other components if needed
    }
}
