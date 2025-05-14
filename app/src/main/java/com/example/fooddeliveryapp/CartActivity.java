package com.example.fooddeliveryapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartContainer;
    private DatabaseReference cartRef;
    private String userId;
    private Button btnCheckout, btnClearCart;
    private TextView grandTotalView;
    private int grandTotal = 0; // Track total globally
    private static final String CHANNEL_ID = "order_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartContainer = findViewById(R.id.cartContainer);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnClearCart = findViewById(R.id.btnClearCart);
        grandTotalView = findViewById(R.id.cartGrandTotal);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId);

        createNotificationChannel(); // For Android 8+

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(CartActivity.this).inflate(R.layout.dialog_payment_processing, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setView(dialogView);
            builder.setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.show();

            new Handler().postDelayed(() -> {
                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
                            String orderId = ordersRef.push().getKey(); // unique order ID

                            Map<String, Object> orderData = new HashMap<>();
                            orderData.put("items", snapshot.getValue());
                            orderData.put("total", grandTotal); // Ensure the grand total is correctly passed
                            orderData.put("timestamp", ServerValue.TIMESTAMP);

                            ordersRef.child(orderId).setValue(orderData); // Save order with total
                            showOrderPlacedNotification(grandTotal); // Pass the correct grandTotal here
                        }

                        cartRef.removeValue().addOnSuccessListener(unused -> {
                            dialog.dismiss();
                            Intent intent = new Intent(CartActivity.this, OrderSuccessActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(CartActivity.this, "Failed to place order.", Toast.LENGTH_SHORT).show();
                    }
                });
            }, 2000);
        });

        btnClearCart.setOnClickListener(v -> {
            cartRef.removeValue().addOnSuccessListener(unused -> {
                Toast.makeText(this, "Cart cleared!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void loadCartItems() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cartContainer.removeAllViews();
                grandTotal = 0;

                if (!snapshot.exists()) {
                    addEmptyView();
                    grandTotalView.setText("Grand Total: ₹0");
                    return;
                }

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String key = itemSnapshot.getKey();
                    String name = itemSnapshot.child("name").getValue(String.class);
                    int price = 0;
                    int quantity = 1;

                    if (itemSnapshot.child("price").getValue() != null)
                        price = Integer.parseInt(itemSnapshot.child("price").getValue().toString());

                    if (itemSnapshot.child("quantity").getValue() != null)
                        quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    int total = price * quantity;
                    grandTotal += total;

                    View itemView = LayoutInflater.from(CartActivity.this)
                            .inflate(R.layout.item_cart, cartContainer, false);

                    TextView nameView = itemView.findViewById(R.id.cartFoodName);
                    TextView quantityView = itemView.findViewById(R.id.cartFoodQuantity);
                    TextView totalPriceView = itemView.findViewById(R.id.cartFoodTotal);
                    Button btnRemove = itemView.findViewById(R.id.btnRemove);

                    nameView.setText(name);
                    quantityView.setText("Qty: " + quantity);
                    totalPriceView.setText("Total: ₹" + total);

                    btnRemove.setOnClickListener(v -> {
                        cartRef.child(key).removeValue()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(CartActivity.this, name + " removed", Toast.LENGTH_SHORT).show();
                                });
                    });

                    cartContainer.addView(itemView);
                }

                grandTotalView.setText("Grand Total: ₹" + grandTotal);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CartActivity.this, "Failed to load cart.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addEmptyView() {
        TextView emptyText = new TextView(this);
        emptyText.setText("🛒 Your cart is empty!");
        emptyText.setTextSize(18);
        emptyText.setPadding(12, 50, 12, 12);
        emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cartContainer.addView(emptyText);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Channel for order placed alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showOrderPlacedNotification(int total) {
        // Ensure that the grandTotal is correctly reflected in the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Add your icon in drawable
                .setContentTitle("Order Placed Successfully")
                .setContentText("Thank you! Your order is confirmed. Total: ₹" + total)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void trackOrder(String orderId) {
        Intent intent = new Intent(CartActivity.this, TrackOrderActivity.class);
        intent.putExtra("orderId", orderId); // Pass orderId to the tracking activity
        startActivity(intent);
    }
}
