package com.example.sportspocialmediaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PostDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 3; // Incremented to 3 to add the page_id column logic

    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_POSTS_TABLE = "CREATE TABLE " +
                PostContract.PostEntry.TABLE_NAME + " (" +
                PostContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostContract.PostEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                PostContract.PostEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                PostContract.PostEntry.COLUMN_CATEGORY + " TEXT, " +  // Optional category column
                PostContract.PostEntry.COLUMN_PAGE_ID + " INTEGER NOT NULL" + // New column for page ID
                ");";

        final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " +
                PostContract.CommentEntry.TABLE_NAME + " (" +
                PostContract.CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PostContract.CommentEntry.COLUMN_POST_ID + " INTEGER NOT NULL, " +
                PostContract.CommentEntry.COLUMN_COMMENT + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + PostContract.CommentEntry.COLUMN_POST_ID + ") REFERENCES " +
                PostContract.PostEntry.TABLE_NAME + "(" + PostContract.PostEntry._ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_POSTS_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + PostContract.PostEntry.TABLE_NAME + " ADD COLUMN " + PostContract.PostEntry.COLUMN_CATEGORY + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + PostContract.PostEntry.TABLE_NAME + " ADD COLUMN " + PostContract.PostEntry.COLUMN_PAGE_ID + " INTEGER NOT NULL DEFAULT 0");
        }
    }
}
