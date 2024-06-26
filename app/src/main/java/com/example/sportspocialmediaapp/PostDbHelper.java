package com.example.sportspocialmediaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Class responsible for managing SQLite database creation and version management.
public class PostDbHelper extends SQLiteOpenHelper {

    // Database name and version constants.
    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 3; // Incremented to 3 to add the page_id column logic

    // Constructor to initialize the PostDbHelper with context, database name, and version.
    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the posts table.
        final String SQL_CREATE_POSTS_TABLE = "CREATE TABLE " +
                PostContract.PostEntry.TABLE_NAME + " (" +
                PostContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostContract.PostEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                PostContract.PostEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                PostContract.PostEntry.COLUMN_CATEGORY + " TEXT, " +  // Optional category column
                PostContract.PostEntry.COLUMN_PAGE_ID + " INTEGER NOT NULL" + // New column for page ID
                ");";

        // SQL statement to create the comments table.
        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " +
                PostContract.CommentEntry.TABLE_NAME + " (" +
                PostContract.CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostContract.CommentEntry.COLUMN_POST_ID + " INTEGER NOT NULL, " +
                PostContract.CommentEntry.COLUMN_COMMENT + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + PostContract.CommentEntry.COLUMN_POST_ID + ") REFERENCES " +
                PostContract.PostEntry.TABLE_NAME + "(" + PostContract.PostEntry._ID + ")" +
                ");";

        // Execute SQL statements to create tables.
        db.execSQL(SQL_CREATE_POSTS_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Check if the old version is less than 2, add CATEGORY column to posts table.
            if (oldVersion < 2) {
                Log.d("Database Upgrade", "Adding CATEGORY column to posts table.");
                db.execSQL("ALTER TABLE " + PostContract.PostEntry.TABLE_NAME + " ADD COLUMN " + PostContract.PostEntry.COLUMN_CATEGORY + " TEXT");
            }
            // Check if the old version is less than 3, add PAGE_ID column to posts table.
            if (oldVersion < 3) {
                Log.d("Database Upgrade", "Adding PAGE_ID column to posts table.");
                db.execSQL("ALTER TABLE " + PostContract.PostEntry.TABLE_NAME + " ADD COLUMN " + PostContract.PostEntry.COLUMN_PAGE_ID + " INTEGER NOT NULL DEFAULT 0");
            }
        } catch (Exception e) {
            // Log any errors that occur during the database upgrade.
            Log.e("Database Upgrade", "Error upgrading database: " + e.getMessage());
        }
    }
}
