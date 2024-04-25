package com.example.sportspocialmediaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Settings_Activity extends AppCompatActivity {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String THEME_MODE_KEY = "ThemeMode";
    private static final int LIGHT_MODE = 0;
    private static final int DARK_MODE = 1;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Switch themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(getCurrentThemeMode() == DARK_MODE);

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int mode = isChecked ? DARK_MODE : LIGHT_MODE;
                saveThemeMode(mode);
                applyTheme(mode);
            }
        });
    }

    private int getCurrentThemeMode() {
        return sharedPreferences.getInt(THEME_MODE_KEY, LIGHT_MODE);
    }

    private void saveThemeMode(int mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(THEME_MODE_KEY, mode);
        editor.apply();
    }

    private void applyTheme(int mode) {
        switch (mode) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
}
