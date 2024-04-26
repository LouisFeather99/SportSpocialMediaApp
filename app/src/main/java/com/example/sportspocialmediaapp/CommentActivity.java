package com.example.sportspocialmediaapp;

import android.content.ContentValues;
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

public class CommentActivity extends AppCompatActivity {
    private long postId; // This should be passed via intent
    private LinearLayout commentsContainer;
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = getIntent().getLongExtra("post_id", -1);
        commentsContainer = findViewById(R.id.commentsContainer);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);

        PostDbHelper dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        loadComments();
        setupSubmitButton();
    }

    private void loadComments() {
        commentsContainer.removeAllViews(); // Clear all views to prevent duplication
        Cursor cursor = mDb.query(
                PostContract.CommentEntry.TABLE_NAME,
                new String[]{PostContract.CommentEntry._ID, PostContract.CommentEntry.COLUMN_COMMENT},
                PostContract.CommentEntry.COLUMN_POST_ID + " = ?",
                new String[]{String.valueOf(postId)},
                null, null, null
        );

        if (cursor != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            int commentIndex = cursor.getColumnIndex(PostContract.CommentEntry.COLUMN_COMMENT);
            while (cursor.moveToNext()) {
                addCommentView(cursor.getString(commentIndex));
            }
            cursor.close();
        } else {
            Log.d("CommentActivity", "No comments found");
        }
    }

    private void setupSubmitButton() {
        buttonSubmitComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                insertComment(commentText);
            } else {
                Toast.makeText(CommentActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(PostContract.CommentEntry.COLUMN_COMMENT, comment);
        values.put(PostContract.CommentEntry.COLUMN_POST_ID, postId);

        long result = mDb.insert(PostContract.CommentEntry.TABLE_NAME, null, values);
        if (result != -1) {
            editTextComment.setText(""); // Clear the input field
            addCommentView(comment); // Add only the new comment
            Toast.makeText(CommentActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("CommentActivity", "Failed to insert comment");
            Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCommentView(String comment) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View commentView = inflater.inflate(R.layout.comment_item, commentsContainer, false);
        TextView textViewComment = commentView.findViewById(R.id.textViewComment);
        textViewComment.setText(comment);
        commentsContainer.addView(commentView);
    }
}


