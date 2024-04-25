package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    private static final int PAGE_ID = 1; // Unique identifier for this instance of PostActivity
    private EditText editTextPostContent;
    private LinearLayout postContainer;
    private SQLiteDatabase mDb;
    private PostDbHelper dbHelper;
    private String currentUserName;  // User's name

    private ImageButton messageButton, newPostButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        editTextPostContent = findViewById(R.id.editTextPostContent);
        Button buttonPost = findViewById(R.id.buttonPost);
        postContainer = findViewById(R.id.postContainer);
        messageButton = findViewById(R.id.button_nav_to_message);
        newPostButton = findViewById(R.id.button_new_post);
        profileButton = findViewById(R.id.button_nav_to_profile);

        loadUserData();
        buttonPost.setOnClickListener(v -> handlePostSubmission());
        loadPosts();

        setupImageButtonListeners();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("name", "Unknown User"); // Default to "Unknown User" if nothing found
        Log.d("PostActivity", "Current user: " + currentUserName);
    }

    private void handlePostSubmission() {
        String postContent = editTextPostContent.getText().toString().trim();
        if (!postContent.isEmpty()) {
            insertPost(postContent, currentUserName);
        } else {
            Toast.makeText(this, "Post content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertPost(String postContent, String userName) {
        ContentValues values = new ContentValues();
        values.put(PostContract.PostEntry.COLUMN_CONTENT, postContent);
        values.put(PostContract.PostEntry.COLUMN_USER_NAME, userName);
        values.put(PostContract.PostEntry.COLUMN_PAGE_ID, PAGE_ID);

        new AsyncTask<ContentValues, Void, Long>() {
            @Override
            protected Long doInBackground(ContentValues... params) {
                return mDb.insert(PostContract.PostEntry.TABLE_NAME, null, params[0]);
            }

            @Override
            protected void onPostExecute(Long postId) {
                if (postId == -1) {
                    Toast.makeText(PostActivity.this, "Failed to add post", Toast.LENGTH_SHORT).show();
                } else {
                    loadPosts();
                    editTextPostContent.setText(""); // Clear the input field after successful post
                    Toast.makeText(PostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(values);
    }

    private void loadPosts() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String selection = PostContract.PostEntry.COLUMN_PAGE_ID + "=?";
                String[] selectionArgs = new String[] { String.valueOf(PAGE_ID) };
                String sortOrder = PostContract.PostEntry._ID + " DESC"; // Order by ID in descending order

                return mDb.query(
                        PostContract.PostEntry.TABLE_NAME,
                        new String[] { PostContract.PostEntry._ID, PostContract.PostEntry.COLUMN_CONTENT, PostContract.PostEntry.COLUMN_USER_NAME },
                        selection, selectionArgs, null, null, sortOrder
                );
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                postContainer.removeAllViews();
                if (cursor != null && cursor.moveToFirst()) {
                    LayoutInflater inflater = LayoutInflater.from(PostActivity.this);
                    do {
                        long postId = cursor.getLong(cursor.getColumnIndexOrThrow(PostContract.PostEntry._ID));
                        String content = cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_CONTENT));
                        String userName = cursor.getString(cursor.getColumnIndexOrThrow(PostContract.PostEntry.COLUMN_USER_NAME));

                        View postView = inflater.inflate(R.layout.post_item, postContainer, false);
                        TextView textViewPostContent = postView.findViewById(R.id.textViewPostContent);
                        TextView textViewUserName = postView.findViewById(R.id.textViewUserName);
                        Button buttonViewComments = postView.findViewById(R.id.buttonViewComments);

                        textViewPostContent.setText(content);
                        textViewUserName.setText(userName);

                        buttonViewComments.setOnClickListener(v -> {
                            Intent intent = new Intent(PostActivity.this, CommentActivity.class);
                            intent.putExtra("post_id", postId);
                            intent.putExtra("post_content", content);
                            intent.putExtra("user_name", userName);
                            startActivity(intent);
                        });

                        postContainer.addView(postView);
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    Log.d("PostActivity", "No posts found to load");
                }
            }
        }.execute();
    }

    private void setupImageButtonListeners() {
        messageButton.setOnClickListener(v -> startActivity(new Intent(this, CommunityActivity.class)));
        newPostButton.setOnClickListener(v -> startActivity(new Intent(this, LiveFeedActivity.class)));
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));
    }
}
