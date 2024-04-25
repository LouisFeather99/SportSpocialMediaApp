package com.example.sportspocialmediaapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class SlotMachineActivity extends AppCompatActivity {

    private ImageView[] slots;
    private Button buttonSpin;
    private TextView tvBalance;
    private Handler handler;
    private Random random;
    private int[] images = {R.drawable.slotlemon, R.drawable.slotorange}; // Your drawable resources
    private String username;
    private int[] results; // Store the results of the spin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_machine);

        slots = new ImageView[5];
        slots[0] = findViewById(R.id.slot1);
        slots[1] = findViewById(R.id.slot2);
        slots[2] = findViewById(R.id.slot3);
        slots[3] = findViewById(R.id.slot4);
        slots[4] = findViewById(R.id.slot5);

        results = new int[slots.length]; // Initialize the results array

        buttonSpin = findViewById(R.id.buttonSpin);
        tvBalance = findViewById(R.id.tvBalance);
        handler = new Handler();
        random = new Random();

        username = getCurrentUsername(); // Replace this with your username retrieval logic
        updateBalanceDisplay();

        buttonSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentBalance = VCManager.getVC(SlotMachineActivity.this, username);
                if (currentBalance >= 1) {
                    VCManager.setVC(SlotMachineActivity.this, username, currentBalance - 1); // Deduct 1 VC for spinning
                    updateBalanceDisplay();
                    buttonSpin.setEnabled(false); // Disable the button during the spin
                    spinSlots(); // Start spinning all slots
                }
            }
        });
    }

    private void spinSlots() {
        final int[] completedSpins = {0}; // Track completed spins

        for (int i = 0; i < slots.length; i++) {
            final int slotIndex = i;
            final int[] spinsLeft = {10}; // Number of changes before stopping
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (spinsLeft[0] > 0) {
                        int imageIndex = random.nextInt(images.length);
                        slots[slotIndex].setImageResource(images[imageIndex]);
                        results[slotIndex] = imageIndex;
                        spinsLeft[0]--;
                        handler.postDelayed(this, 100 + (10 * (10 - spinsLeft[0])));
                    } else {
                        completedSpins[0]++;
                        if (completedSpins[0] == slots.length) {
                            checkResults(); // Check results after the last spin completes
                        }
                    }
                }
            };
            handler.post(runnable);
        }
    }

    private void checkResults() {
        boolean allMatch = true;
        for (int i = 1; i < results.length; i++) {
            if (results[0] != results[i]) {
                allMatch = false;
                break;
            }
        }

        if (allMatch) {
            int currentBalance = VCManager.getVC(SlotMachineActivity.this, username);
            VCManager.setVC(SlotMachineActivity.this, username, currentBalance + 3); // All matches = 3 VC
        }

        updateBalanceDisplay();
        buttonSpin.setEnabled(true); // Re-enable the button after checking results
    }

    private void updateBalanceDisplay() {
        int balance = VCManager.getVC(this, username);
        tvBalance.setText("Balance: " + balance + " VC");
    }

    private String getCurrentUsername() {
        // Placeholder for actual username retrieval
        return "exampleUser";
    }
}
