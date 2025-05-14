package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class OrderSuccessActivity extends AppCompatActivity {

    private Button btnBackHome, btnTrackOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // Initialize views
        ImageView successGif = findViewById(R.id.successGif);
        btnBackHome = findViewById(R.id.btnBackHome);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);

        // Load GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.success_tick) // your gif name
                .into(successGif);

        // Set OnClickListener for Back to Home button
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class); // Replace with your actual Home activity
            startActivity(intent);
            finish();
        });

        // Set OnClickListener for Track Order button
        btnTrackOrder.setOnClickListener(v -> {
            // Pass the order ID to TrackOrderActivity using the consistent key
            Intent intent = new Intent(OrderSuccessActivity.this, TrackOrderActivity.class);
            intent.putExtra("ORDER_ID", "your_order_id_here");  // Pass the actual order ID here
            startActivity(intent);
        });
    }
}
