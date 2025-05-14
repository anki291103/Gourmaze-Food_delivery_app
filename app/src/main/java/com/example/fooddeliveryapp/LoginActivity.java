package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btnLogin;
    TextView goToRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        goToRegister = findViewById(R.id.goToRegister);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class)); // Next screen
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
