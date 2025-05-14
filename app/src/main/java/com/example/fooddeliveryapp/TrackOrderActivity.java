package com.example.fooddeliveryapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TrackOrderActivity extends AppCompatActivity {

    private ImageView deliveryIcon;
    private TextView orderStatusView;
    private Button btnTrack;
    private LinearLayout checkpointsLayout;

    private final Handler handler = new Handler();
    private final String[] statuses = {
            "Status: Order Confirmed",
            "Status: Preparing",
            "Status: Out for Delivery",
            "Status: Nearby",
            "Status: Delivered"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        deliveryIcon = findViewById(R.id.ic_shipped);
        orderStatusView = findViewById(R.id.orderStatusView);
        btnTrack = findViewById(R.id.btnTrackOrder);
        checkpointsLayout = findViewById(R.id.checkpointsLayout);

        btnTrack.setOnClickListener(v -> startVerticalTrackingAnimation());
    }

    private void startVerticalTrackingAnimation() {
        checkpointsLayout.post(() -> {
            int checkpointCount = checkpointsLayout.getChildCount();

            if (checkpointCount == 0) {
                Toast.makeText(this, "No checkpoints found!", Toast.LENGTH_SHORT).show();
                return;
            }

            View firstCheckpoint = checkpointsLayout.getChildAt(0);
            float startY = firstCheckpoint.getY() + checkpointsLayout.getY() - deliveryIcon.getHeight() / 2f;
            deliveryIcon.setY(startY);

            for (int i = 0; i < checkpointCount; i++) {
                View checkpoint = checkpointsLayout.getChildAt(i);

                float targetY = checkpoint.getY() + checkpointsLayout.getY() - deliveryIcon.getHeight() / 2f;
                int delay = i * 2000;
                int finalI = i;

                handler.postDelayed(() -> {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(deliveryIcon, "y", targetY);
                    animator.setDuration(800);
                    animator.start();

                    // Set status text
                    if (finalI < statuses.length) {
                        orderStatusView.setText(statuses[finalI]);
                    } else {
                        orderStatusView.setText("Status: Not Predefined");
                    }

                    // 💚 Change dot color to green
                    if (checkpoint instanceof LinearLayout) {
                        View dotView = ((LinearLayout) checkpoint).getChildAt(0); // dot is the first child
                        dotView.setBackgroundResource(R.drawable.dot_green);
                    }

                    // Final checkpoint logic
                    if (finalI == checkpointCount - 1) {
                        Toast.makeText(TrackOrderActivity.this, "Order Delivered!", Toast.LENGTH_SHORT).show();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(TrackOrderActivity.this, HomeActivity.class);
                            intent.putExtra("order_status_message", "Order Delivered");
                            startActivity(intent);
                            finish();
                        }, 3000);
                    }

                }, delay);
            }
        });
    }
}
