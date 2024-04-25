package com.example.sportspocialmediaapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        // Initialize section FrameLayouts
        FrameLayout section1 = findViewById(R.id.section1);
        FrameLayout section2 = findViewById(R.id.section2);
        FrameLayout section3 = findViewById(R.id.section3);
        FrameLayout section4 = findViewById(R.id.section4);
        FrameLayout section5 = findViewById(R.id.section5); // Add this line for section5

        // Set onClick listeners for sections, replace with correct activities
        section1.setOnClickListener(v -> startActivity(new Intent(BettingActivity.this, RouletteActivity.class)));
        section2.setOnClickListener(v -> startActivity(new Intent(BettingActivity.this, SlotMachineActivity.class)));
        section3.setOnClickListener(v -> startActivity(new Intent(BettingActivity.this, PremOddActivity.class)));
        section4.setOnClickListener(v -> startActivity(new Intent(BettingActivity.this, BundaOddsActivity.class)));

        // Set onClick listener for section5 to open a web page
        section5.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gamcare.org.uk/"));
            startActivity(browserIntent);
        });

        // You can add more setup code for other components if needed
    }
}
