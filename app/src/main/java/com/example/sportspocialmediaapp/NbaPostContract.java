package com.example.sportspocialmediaapp;


import android.provider.BaseColumns;

public final class NbaPostContract {

    // Private constructor to prevent instantiation
    private NbaPostContract() {}

    // Inner class that defines the table contents for NBA posts
    public static final class NbaPostEntry implements BaseColumns {
        public static final String TABLE_NAME = "nba_posts";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_USER_NAME = "user_name";
    }
}
