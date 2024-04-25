package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentActivity extends AppCompatActivity {
    private long postId; // This should be passed via intent
    private LinearLayout commentsContainer;
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private SQLiteDatabase mDb;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = getIntent().getLongExtra("post_id", -1);
        commentsContainer = findViewById(R.id.commentsContainer); // Ensure this ID matches your layout
        editTextComment = findViewById(R.id.editTextComment); // Ensure this ID matches your layout
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment); // Ensure this ID matches your layout

        // Set up the database
        PostDbHelper dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        loadComments();
        setupSubmitButton();
    }

    private void loadComments() {
        executorService.execute(() -> {
            Cursor cursor = mDb.query(
                    "comments", // Assuming there's a table named "comments"
                    new String[]{"comment_id", "content"}, // Columns to return
                    "post_id = ?", // Columns for the WHERE clause
                    new String[]{String.valueOf(postId)}, // Values for the WHERE clause
                    null, null, null
            );
            runOnUiThread(() -> {
                if (cursor != null) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    int contentIndex = cursor.getColumnIndex("content");
                    if (contentIndex == -1) {
                        Log.e("CommentActivity", "Column 'content' does not exist");
                        return;
                    }
                    while (cursor.moveToNext()) {
                        View commentView = inflater.inflate(R.layout.comment_item, commentsContainer, false);
                        TextView textViewComment = commentView.findViewById(R.id.textViewComment);
                        textViewComment.setText(cursor.getString(contentIndex));
                        commentsContainer.addView(commentView);
                    }
                    cursor.close();
                } else {
                    Log.d("CommentActivity", "No comments found");
                }
            });
        });
    }

    private void setupSubmitButton() {
        buttonSubmitComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            Log.d("CommentActivity", "Comment to submit: " + commentText);
            if (!commentText.isEmpty()) {
                insertComment(commentText);
            } else {
                Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertComment(String comment) {
        ContentValues values = new ContentValues();
        values.put("content", comment);  // Ensure column names match your database schema
        values.put("post_id", postId);

        executorService.execute(() -> {
            long result = mDb.insert("comments", null, values);
            runOnUiThread(() -> {
                if (result == -1) {
                    Log.e("CommentActivity", "Failed to insert comment");
                    Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                } else {
                    editTextComment.setText(""); // Clear the input field
                    loadComments(); // Reload comments to display the new one
                    Toast.makeText(CommentActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

