package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public abstract class BasePostActivity extends AppCompatActivity {
    protected EditText editTextPostContent;
    protected LinearLayout postContainer;
    protected SQLiteDatabase mDb;
    protected PostDbHelper dbHelper;
    protected String currentUserName;

    protected abstract int getPageId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        editTextPostContent = findViewById(R.id.editTextPostContent);
        postContainer = findViewById(R.id.postContainer);

        loadUserData();
        setupPostButton();
        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        loadPosts();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("name", "Unknown User");
        Log.d("BasePostActivity", "Current user data loaded: " + currentUserName);
    }

    private void setupPostButton() {
        Button buttonPost = findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(v -> {
            String postContent = editTextPostContent.getText().toString().trim();
            if (!postContent.isEmpty()) {
                Log.d("BasePostActivity", "Post button clicked, content: " + postContent);
                insertPost(postContent);
            } else {
                Toast.makeText(this, "Post content cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPost(String postContent) {
        ContentValues values = new ContentValues();
        values.put(PostContract.PostEntry.COLUMN_CONTENT, postContent);
        values.put(PostContract.PostEntry.COLUMN_USER_NAME, currentUserName);
        values.put(PostContract.PostEntry.COLUMN_PAGE_ID, getPageId());

        new InsertPostTask(this, values).execute();
    }

    protected void loadPosts() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String selection = PostContract.PostEntry.COLUMN_PAGE_ID + "=?";
                String[] selectionArgs = new String[]{String.valueOf(getPageId())};

                return mDb.query(
                        PostContract.PostEntry.TABLE_NAME,
                        new String[]{PostContract.PostEntry._ID, PostContract.PostEntry.COLUMN_CONTENT, PostContract.PostEntry.COLUMN_USER_NAME},
                        selection, selectionArgs, null, null, null
                );
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                postContainer.removeAllViews();
                if (cursor != null && cursor.moveToFirst()) {
                    LayoutInflater inflater = LayoutInflater.from(BasePostActivity.this);
                    do {
                        View postView = inflater.inflate(R.layout.post_item, postContainer, false);
                        setupPostView(postView, cursor);
                        postContainer.addView(postView);
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    Log.d("BasePostActivity", "No posts found to load.");
                }
            }
        }.execute();
    }

    private void setupPostView(View view, Cursor cursor) {
        // Setup post view
    }

    private static class InsertPostTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<BasePostActivity> activityReference;
        private ContentValues values;

        InsertPostTask(BasePostActivity context, ContentValues values) {
            activityReference = new WeakReference<>(context);
            this.values = values;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            BasePostActivity activity = activityReference.get();
            if (activity != null && !isCancelled()) {
                return activity.mDb.insert(PostContract.PostEntry.TABLE_NAME, null, values);
            }
            return -1L;
        }

        @Override
        protected void onPostExecute(Long postId) {
            BasePostActivity activity = activityReference.get();
            if (activity != null && postId != -1) {
                activity.loadPosts();
                activity.editTextPostContent.setText("");
                Toast.makeText(activity, "Post added successfully", Toast.LENGTH_SHORT).show();
            } else if (activity != null) {
                Toast.makeText(activity, "Failed to add post", Toast.LENGTH_SHORT).show();
            }
        }
    }
}







