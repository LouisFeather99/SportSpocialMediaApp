package com.example.sportspocialmediaapp;

import android.provider.BaseColumns;

public final class PostContract {

    private PostContract() {}  // Private constructor to prevent instantiation

    public static final class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PAGE_ID = "page_id";
        public static final String COLUMN_IMAGE_URI = "image_uri";  // For storing image URIs
        public static final String COLUMN_VIDEO_URI = "video_uri";  // For storing video URIs
    }

    public static final class CommentEntry implements BaseColumns {
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_COMMENT = "comment";
    }
}
