package com.example.fooddeliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

        Button btnLogin, btnRegister;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);

            btnLogin.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
            });

            btnRegister.setOnClickListener(v -> {
                startActivity(new Intent(this, RegisterActivity.class));
            });
        }
    }
