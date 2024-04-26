package com.example.sportspocialmediaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CommunityActivity extends AppCompatActivity {


    private ImageButton BettingButton, newPostButton, profileButton, HomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        // Initialize section FrameLayouts
        FrameLayout section1 = findViewById(R.id.section1);
        FrameLayout section2 = findViewById(R.id.section2);
        FrameLayout section3 = findViewById(R.id.section3);
        FrameLayout section4 = findViewById(R.id.section4);
        BettingButton = findViewById(R.id.button_nav_to_message);
        newPostButton = findViewById(R.id.button_new_post);
        profileButton = findViewById(R.id.button_nav_to_profile);
        HomeButton = findViewById(R.id.button_nav_to_search);


        section1.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, FootballCommunityActivity.class)));
        section2.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, NFLPostActivity.class)));
        section3.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, BasketballCommunityActivity.class)));
        section4.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, WrestlingCommunityActivity.class)));
        BettingButton.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, BettingActivity.class)));
        newPostButton.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, PostActivity.class)));
        profileButton.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, SettingActivity.class)));
        HomeButton.setOnClickListener(v -> startActivity(new Intent(CommunityActivity.this, MainActivity.class)));




        // You can add more setup code for other components if needed
    }
}
