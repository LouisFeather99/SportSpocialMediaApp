package com.example.sportspocialmediaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class VCManager {
    private static final String PREFS_NAME = "VCPrefs";
    private static final String TAG = "VCManager";

    // Get the current VC balance for a specific user
    public static int getVC(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String key = "VC_" + username;
        int defaultVC = 100;  // Default VC if none found
        int vc = prefs.getInt(key, defaultVC);

        Log.d(TAG, "Retrieved VC for " + username + ": " + vc);
        return vc;
    }

    // Update the VC balance for a specific user
    public static void setVC(Context context, String username, int newBalance) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String key = "VC_" + username;

        editor.putInt(key, newBalance);
        editor.apply();

        Log.d(TAG, "Updated VC for " + username + " to: " + newBalance);
    }
}
