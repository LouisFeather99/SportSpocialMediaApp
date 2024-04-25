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
    protected String currentUserName; // Username is fetched and set here

    protected abstract int getPageId(); // Abstract method to be implemented by subclasses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        editTextPostContent = findViewById(R.id.editTextPostContent);
        postContainer = findViewById(R.id.postContainer);

        loadUserData(); // Direct method to load user data
        setupPostButton();
        loadPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();  // Reload user data when activity resumes
        loadPosts();     // Reload posts to ensure they use the latest username
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("name", "Unknown User"); // Ensure this matches the key used when saving the name
        Log.d("BasePostActivity", "Current user: " + currentUserName);
    }

    private void setupPostButton() {
        Button buttonPost = findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(v -> {
            String postContent = editTextPostContent.getText().toString().trim();
            if (!postContent.isEmpty()) {
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

        new AsyncTask<ContentValues, Void, Long>() {
            @Override
            protected Long doInBackground(ContentValues... params) {
                return mDb.insert(PostContract.PostEntry.TABLE_NAME, null, params[0]);
            }

            @Override
            protected void onPostExecute(Long postId) {
                if (postId != -1) {
                    loadPosts();
                    editTextPostContent.setText("");
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

