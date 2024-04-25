package com.example.sportspocialmediaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class RouletteActivity extends AppCompatActivity {

    private TextView tvResult, tvBalance;
    private Button spinButton;
    private Spinner spinnerChoices;
    private Random random = new Random();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        tvResult = findViewById(R.id.tvResult);
        tvBalance = findViewById(R.id.tvBalance);
        spinButton = findViewById(R.id.buttonSpin);
        spinnerChoices = findViewById(R.id.spinnerChoices);
        username = getCurrentUsername(); // Fetch the username as in SlotMachineActivity

        Integer[] numbers = new Integer[10];
        for (int i = 0; i < 10; i++) {
            numbers[i] = i + 1;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        spinnerChoices.setAdapter(adapter);

        updateBalanceDisplay();

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int balance = VCManager.getVC(RouletteActivity.this, username);
                if (balance > 0) {
                    VCManager.setVC(RouletteActivity.this, username, balance - 1); // Deduct 1 VC for the spin
                    spinRoulette();
                } else {
                    tvResult.setText("Not enough VC!");
                }
            }
        });
    }

    private void spinRoulette() {
        int selectedOutcome = random.nextInt(10) + 1;
        int userChoice = (Integer) spinnerChoices.getSelectedItem();

        tvResult.setText("Result: " + selectedOutcome);

        if (userChoice == selectedOutcome) {
            tvResult.append("\nYou won!");
            VCManager.setVC(this, username, VCManager.getVC(this, username) + 10); // Add 10 VC for a win
        } else {
            tvResult.append("\nYou lost!");
        }

        updateBalanceDisplay();
    }

    private void updateBalanceDisplay() {
        int balance = VCManager.getVC(this, username);
        tvBalance.setText("Balance: " + balance + " VC");
    }

    private String getCurrentUsername() {
        // Retrieve the username of the logged-in user
        return "exampleUser"; // Placeholder, replace with actual username retrieval logic
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveVCBalance();
    }

    private void saveVCBalance() {
        int balance = VCManager.getVC(this, username);
        SharedPreferences prefs = getSharedPreferences("VC_Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(username, balance);
        editor.apply();
    }
}

