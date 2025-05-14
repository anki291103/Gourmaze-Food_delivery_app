package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText;
    Button updateButton, editButton;
    ImageView profileImage;

    FirebaseAuth mAuth;
    DatabaseReference userRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        nameEditText = findViewById(R.id.editName);
        emailEditText = findViewById(R.id.editEmail);
        phoneEditText = findViewById(R.id.editPhone);
        updateButton = findViewById(R.id.btnUpdate);
        editButton = findViewById(R.id.btnEdit);
        Button logoutButton = findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out from Firebase

            // Go back to main/login screen and clear the activity stack
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear session
            startActivity(intent);
            finish(); // finish current activity
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot snapshot = task.getResult();
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String imageUrl = snapshot.child("profileImage").getValue(String.class);

                    nameEditText.setText(name != null ? name : "");
                    emailEditText.setText(email != null ? email : "");
                    phoneEditText.setText(phone != null ? phone : "");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this).load(imageUrl).into(profileImage);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Edit button functionality
        editButton.setOnClickListener(v -> {
            nameEditText.setEnabled(true);
            phoneEditText.setEnabled(true);
            updateButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE); // Hide edit button while editing
        });

        // Save/update button
        updateButton.setOnClickListener(v -> {
            String updatedName = nameEditText.getText().toString().trim();
            String updatedPhone = phoneEditText.getText().toString().trim();

            if (updatedName.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update in Firebase
            userRef.child("name").setValue(updatedName);
            userRef.child("phone").setValue(updatedPhone);

            // Disable again and hide update button
            nameEditText.setEnabled(false);
            phoneEditText.setEnabled(false);
            updateButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);

            Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        });

        // Email stays non-editable
        emailEditText.setInputType(InputType.TYPE_NULL);
        emailEditText.setFocusable(false);
    }
}
