package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class AppDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SocialMediaApp.db";
    private static final int DATABASE_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public Bundle validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[] {"name", "email", "username"},
                "username = ? AND password = ?",
                new String[] {username, password},
                null, null, null
        );

        Bundle userData = null;
        if (cursor != null && cursor.moveToFirst()) {
            int nameIdx = cursor.getColumnIndex("name");
            int emailIdx = cursor.getColumnIndex("email");
            int usernameIdx = cursor.getColumnIndex("username");

            if (nameIdx == -1 || emailIdx == -1 || usernameIdx == -1) {
                Log.e("AppDbHelper", "One or more columns are missing in the database.");
            } else {
                String name = cursor.getString(nameIdx);
                String email = cursor.getString(emailIdx);

                userData = new Bundle();
                userData.putString("name", name);
                userData.putString("email", email);
                userData.putString("username", username);
            }
            cursor.close();
        }
        db.close();
        return userData;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int rowsAffected = db.update("users", values, "username = ?", new String[]{username});
        db.close();
        return rowsAffected > 0;
    }

    public Cursor searchUsersByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {"id", "name", "email", "username"};
        String selection = "name LIKE ?";
        String[] selectionArgs = new String[] {"%" + name + "%"};

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

