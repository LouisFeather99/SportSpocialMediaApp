package com.example.sportspocialmediaapp;

import android.content.ContentValues;
import android.content.Intent;
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

public class CommentActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private PostDbHelper dbHelper;
    private long postId;
    private String postContent;
    private String userName;
    private LinearLayout commentsContainer;
    private EditText editTextComment;
    private Button buttonSubmitComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        dbHelper = new PostDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        commentsContainer = findViewById(R.id.commentsContainer);
        editTextComment = findViewById(R.id.editTextComment);

        // Get data from intent
        Intent intent = getIntent();
        postId = intent.getLongExtra("post_id", -1);
        postContent = intent.getStringExtra("post_content");
        userName = intent.getStringExtra("user_name");

        if (postId == -1 || postContent == null || userName == null) {
            Toast.makeText(this, "Error loading post details!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if necessary data is missing
            return;
        }

        setupPostView();
        loadComments();

        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);
        buttonSubmitComment.setOnClickListener(v -> {
            Log.d("CommentActivity", "Submit button clicked");
            submitComment();
        });
    }

    private void setupPostView() {
        TextView textViewUserName = findViewById(R.id.textViewUserName);
        TextView textViewPostContent = findViewById(R.id.textViewPostContent);

        textViewUserName.setText(userName);
        textViewPostContent.setText(postContent);
    }

    private void submitComment() {
        String commentText = editTextComment.getText().toString().trim();
        if (!commentText.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(PostContract.CommentEntry.COLUMN_COMMENT, commentText);
            values.put(PostContract.CommentEntry.COLUMN_POST_ID, postId);
            new InsertCommentTask().execute(values);
        } else {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private class InsertCommentTask extends AsyncTask<ContentValues, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            buttonSubmitComment.setEnabled(false); // Disable the button while processing
        }

        @Override
        protected Boolean doInBackground(ContentValues... values) {
            long result = mDb.insert(PostContract.CommentEntry.TABLE_NAME, null, values[0]);
            return result != -1; // Return true if insert was successful
        }

        @Override
        protected void onPostExecute(Boolean success) {
            buttonSubmitComment.setEnabled(true); // Re-enable the button
            if (success) {
                loadComments();
                editTextComment.setText("");  // Clear input after submission
                Toast.makeText(CommentActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadComments() {
        Cursor cursor = mDb.query(
                PostContract.CommentEntry.TABLE_NAME,
                new String[]{PostContract.CommentEntry.COLUMN_COMMENT},
                PostContract.CommentEntry.COLUMN_POST_ID + "=?",
                new String[]{String.valueOf(postId)},
                null, null, null
        );

        LayoutInflater inflater = LayoutInflater.from(this);
        commentsContainer.removeAllViews(); // Clear previous comments before reloading
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(PostContract.CommentEntry.COLUMN_COMMENT));
                View commentView = inflater.inflate(R.layout.comment_item, commentsContainer, false);
                TextView textViewComment = commentView.findViewById(R.id.textViewComment);
                textViewComment.setText(comment);
                commentsContainer.addView(commentView);
            }
            cursor.close();
        } else {
            TextView noCommentsView = new TextView(this);
            noCommentsView.setText("No comments yet.");
            commentsContainer.addView(noCommentsView);
        }
    }
}
