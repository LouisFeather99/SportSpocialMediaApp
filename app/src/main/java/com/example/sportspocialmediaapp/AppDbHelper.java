package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

// Helper class for managing database operations
public class AppDbHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "SocialMediaApp.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public AppDbHelper(Context context) {
        // Call the superclass constructor to create or open the database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the users table
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the users table if it exists
        db.execSQL("DROP TABLE IF EXISTS users");
        // Recreate the users table
        onCreate(db);
    }

    // Validate user credentials
    public Bundle validateUser(String username, String password) {
        // Get a readable database instance
        SQLiteDatabase db = this.getReadableDatabase();
        // Perform a query to find the user with the given username and password
        Cursor cursor = db.query(
                "users",
                new String[] {"name", "email", "username"},
                "username = ? AND password = ?",
                new String[] {username, password},
                null, null, null
        );

        // Initialize a Bundle to store user data
        Bundle userData = null;
        // Check if the cursor is not null and move it to the first row
        if (cursor != null && cursor.moveToFirst()) {
            // Get the column indices
            int nameIdx = cursor.getColumnIndex("name");
            int emailIdx = cursor.getColumnIndex("email");
            int usernameIdx = cursor.getColumnIndex("username");

            // Check if all required columns exist in the database
            if (nameIdx == -1 || emailIdx == -1 || usernameIdx == -1) {
                // Log an error if any required column is missing
                Log.e("AppDbHelper", "One or more columns are missing in the database.");
            } else {
                // Retrieve user data from the cursor
                String name = cursor.getString(nameIdx);
                String email = cursor.getString(emailIdx);

                // Create a Bundle and store user data in it
                userData = new Bundle();
                userData.putString("name", name);
                userData.putString("email", email);
                userData.putString("username", username);
            }
            // Close the cursor
            cursor.close();
        }
        // Close the database
        db.close();
        // Return user data (or null if user not found)
        return userData;
    }

    // Update user password
    public boolean updatePassword(String username, String newPassword) {
        // Get a writable database instance
        SQLiteDatabase db = this.getWritableDatabase();
        // Create ContentValues object to store new password
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        // Perform update operation
        int rowsAffected = db.update("users", values, "username = ?", new String[]{username});
        // Close the database
        db.close();
        // Return true if password updated successfully, false otherwise
        return rowsAffected > 0;
    }

    // Search users by name
    public Cursor searchUsersByName(String name) {
        // Get a readable database instance
        SQLiteDatabase db = this.getReadableDatabase();
        // Define columns to be retrieved
        String[] columns = new String[] {"id", "name", "email", "username"};
        // Define selection criteria
        String selection = "name LIKE ?";
        // Define selection arguments
        String[] selectionArgs = new String[] {"%" + name + "%"};

        // Perform a query to search users by name
        return db.query(
                "users",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
}

