package com.example.sportspocialmediaapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private Button btnSignOut, btnChooseImage;
    private TextView nameTextView, emailTextView;
    private ImageView profileImageView, messageButton, newPostButton, profileButton, betButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        loadUserData();
        setupButtonListeners();
    }

    private void initializeUI() {
        btnSignOut = findViewById(R.id.btnSignOut);
        nameTextView = findViewById(R.id.textView);
        emailTextView = findViewById(R.id.textView2);
        profileImageView = findViewById(R.id.profileImageView);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        messageButton = findViewById(R.id.button_nav_to_message);
        newPostButton = findViewById(R.id.button_new_post);
        profileButton = findViewById(R.id.button_nav_to_profile);
        betButton = findViewById(R.id.button_nav_to_search);

        messageButton.setOnClickListener(v -> startActivity(new Intent(this, CommunityActivity.class)));
        newPostButton.setOnClickListener(v -> startActivity(new Intent(this, LiveFeedActivity.class)));
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));
        betButton.setOnClickListener(v -> startActivity(new Intent(this, BettingActivity.class)));
    }

    private void loadUserData() {
        SharedPreferences sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        nameTextView.setText(sharedPrefs.getString("name", "No Name"));
        emailTextView.setText(sharedPrefs.getString("email", "No Email"));
        String imagePath = sharedPrefs.getString("imagePath", null);
        loadImageFromPath(imagePath);
    }

    private void loadImageFromPath(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                profileImageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Image cannot be loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupButtonListeners() {
        btnSignOut.setOnClickListener(v -> showSignOutDialog());
        btnChooseImage.setOnClickListener(v -> checkPermissionAndOpenGallery());
    }

    private void showSignOutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> signOut())
                .setNegativeButton("No", null)
                .show();
    }

    private void signOut() {
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImageView.setImageBitmap(bitmap);
                saveImageUri(imageUri);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageUri(Uri imageUri) {
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.putString("imagePath", imageUri.toString());
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
