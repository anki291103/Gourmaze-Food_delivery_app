package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, phone, password;
    Button btnRegister;
    TextView goToLogin;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        password = findViewById(R.id.Password);
        btnRegister = findViewById(R.id.btnRegister);
        goToLogin = findViewById(R.id.goToLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister.setOnClickListener(v -> {
            String userName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPhone = phone.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();

                            // Save user details in Realtime Database
                            User user = new User(userName, userEmail, userPhone);
                            mDatabase.child(userId).setValue(user);

                            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}
