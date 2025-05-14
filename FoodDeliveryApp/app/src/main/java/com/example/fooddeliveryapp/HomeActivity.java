package com.example.fooddeliveryapp;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNavigationView;
    private ViewPager bannerSlider;
    private Handler handler;
    private Runnable updateRunnable;
    private int currentPage = 0;
    private int[] images = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createNotificationChannel();

        welcomeText = findViewById(R.id.welcomeText);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bannerSlider = findViewById(R.id.bannerSlider);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        // Show toast if redirected after delivery
        String message = getIntent().getStringExtra("order_status_message");
        if (message != null && !message.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        // Banner Slider Setup
        BannerAdapter adapter = new BannerAdapter(this, images);
        bannerSlider.setAdapter(adapter);
        handler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.length) currentPage = 0;
                bannerSlider.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(updateRunnable, 3000);

        if (user != null) {
            String userId = user.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("Users");

            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        welcomeText.setText(name != null && !name.isEmpty() ? "Hi, " + name + " 👋" : "Hi there 👋");
                    } else {
                        welcomeText.setText("Hi there 👋");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });

            checkAndPromptDeliveryDetails(userId);
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) return true;
                else if (id == R.id.nav_cart) {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    return true;
                } else if (id == R.id.nav_orders) {
                    startActivity(new Intent(HomeActivity.this, OrdersActivity.class));
                    return true;
                } else if (id == R.id.nav_search) {
                    startActivity(new Intent(HomeActivity.this, MenuPage.class));
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void checkAndPromptDeliveryDetails(String userId) {
        DatabaseReference deliveryRef = FirebaseDatabase.getInstance().getReference("DeliveryDetails").child(userId);
        deliveryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    showDeliveryDetailsDialog(deliveryRef);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error loading delivery details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeliveryDetailsDialog(DatabaseReference deliveryRef) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_delivery_details, null);

        EditText nameInput = view.findViewById(R.id.inputName);
        EditText phoneInput = view.findViewById(R.id.inputPhone);
        EditText addressInput = view.findViewById(R.id.inputAddress);
        EditText pincodeInput = view.findViewById(R.id.inputPincode);

        new AlertDialog.Builder(this)
                .setTitle("Enter Delivery Details")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();
                    String address = addressInput.getText().toString().trim();
                    String pincode = pincodeInput.getText().toString().trim();

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || pincode.isEmpty()) {
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        showDeliveryDetailsDialog(deliveryRef);
                        return;
                    }

                    com.example.fooddeliveryapp.DeliveryDetails details = new com.example.fooddeliveryapp.DeliveryDetails(name, phone, address, pincode);
                    deliveryRef.setValue(details)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Delivery details saved", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to save details", Toast.LENGTH_SHORT).show());
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "OrderChannel",
                    "Order Updates",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for order confirmations and delivery updates");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}

