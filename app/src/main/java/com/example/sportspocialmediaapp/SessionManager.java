package com.example.sportspocialmediaapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_PATH = "imagePath"; // Adding key for storing image path

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email, String imagePath){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_IMAGE_PATH, imagePath); // Storing image path
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, "No Name");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "No Email");
    }

    public String getImagePath() {
        return pref.getString(KEY_IMAGE_PATH, null); // Return null if no path is set
    }

    public void setImagePath(String imagePath) {
        editor.putString(KEY_IMAGE_PATH, imagePath);
        editor.commit();
    }
}

