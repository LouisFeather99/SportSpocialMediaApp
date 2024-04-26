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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BasePostActivity extends AppCompatActivity {
    protected EditText editTextPostContent;
    protected LinearLayout postContainer;
    protected SQLiteDatabase mDb;
    protected PostDbHelper dbHelper;
    protected String currentUserName;
    protected abstract int getPageId(); // Abstract method to be implemented by subclasses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        editTextPostContent = findViewById(R.id.editTextPostContent);
        Button buttonPost = findViewById(R.id.buttonPost);
        postContainer = findViewById(R.id.postContainer);

        getCurrentUser();
        buttonPost.setOnClickListener(v -> handlePostSubmission());
        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();  // Ensure the current user is re-checked when the activity resumes
        loadPosts();       // Refresh posts to reflect any new data or changes
    }

    private void getCurrentUser() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("username", "Unknown User");
        Log.d("BasePostActivity", "Current user loaded: " + currentUserName);  // Log the username every time it's loaded
    }

    private void handlePostSubmission() {
        String postContent = editTextPostContent.getText().toString().trim();
        if (!postContent.isEmpty()) {
            insertPost(postContent);
        } else {
            Toast.makeText(this, "Post content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertPost(String postContent) {
        ContentValues values = new ContentValues();
        values.put(PostContract.PostEntry.COLUMN_CONTENT, postContent);
        values.put(PostContract.PostEntry.COLUMN_USER_NAME, currentUserName);
        values.put(PostContract.PostEntry.COLUMN_PAGE_ID, getPageId());

        new AsyncTask<ContentValues, Void, Long>() {
            @Override
            protected Long doInBackground(ContentValues... params) {
                return mDb.insert(PostContract.PostEntry.TABLE_NAME, null, params[0]);
            }

            @Override
            protected void onPostExecute(Long postId) {
                if (postId != -1) {
                    loadPosts();
                    editTextPostContent.setText("");  // Clear the text field after posting
                    Toast.makeText(BasePostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BasePostActivity.this, "Failed to add post", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(values);
    }

    protected void loadPosts() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String selection = PostContract.PostEntry.COLUMN_PAGE_ID + "=?";
                String[] selectionArgs = new String[] {String.valueOf(getPageId())};

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
                        TextView textViewPostContent = postView.findViewById(R.id.textViewPostContent);
                        TextView textViewUserName = postView.findViewById(R.id.textViewUserName);

                        textViewPostContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_CONTENT)));
                        textViewUserName.setText(cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_USER_NAME)));

                        postContainer.addView(postView);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }.execute();
    }
}

