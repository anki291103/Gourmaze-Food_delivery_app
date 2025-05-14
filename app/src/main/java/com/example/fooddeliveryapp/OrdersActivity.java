package com.example.fooddeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {

    private LinearLayout ordersContainer;
    private DatabaseReference ordersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersContainer = findViewById(R.id.ordersContainer);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity safely
            return;
        }

        userId = currentUser.getUid();
        FirebaseDatabase.getInstance().goOnline(); // Force online (optional safety)

        ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
        loadOrders();
    }

    private void loadOrders() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
// Instead of this:
// ordersContainer.removeAllViews();

// Use this:
                int childCount = ordersContainer.getChildCount();
                if (childCount > 2) {
                    ordersContainer.removeViews(2, childCount - 2); // Keep title (0) and divider (1)
                }

                if (!snapshot.exists()) {
                    TextView empty = new TextView(OrdersActivity.this);
                    empty.setText("No orders yet. Start shopping! 🛒");
                    empty.setTextSize(16);
                    empty.setPadding(16, 32, 16, 0);
                    ordersContainer.addView(empty);
                    return;
                }

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderId = orderSnapshot.getKey();

                    // Safely get total amount
                    Long totalLong = orderSnapshot.child("total").getValue(Long.class);
                    int totalAmount = totalLong != null ? totalLong.intValue() : 0;

                    // Safely get timestamp
                    Long timestamp = orderSnapshot.child("timestamp").getValue(Long.class);
                    String dateString;
                    if (timestamp != null) {
                        dateString = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(timestamp));
                    } else {
                        dateString = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
                    }

                    // Inflate order card layout
                    View orderView = LayoutInflater.from(OrdersActivity.this)
                            .inflate(R.layout.item_order, ordersContainer, false);

                    TextView orderNumberView = orderView.findViewById(R.id.orderNumber);
                    TextView orderDateView = orderView.findViewById(R.id.orderDate);
                    TextView orderTotalView = orderView.findViewById(R.id.orderTotal);
                    LinearLayout orderItemsContainer = orderView.findViewById(R.id.orderItemsContainer);

                    orderNumberView.setText("🧾 Order #" + orderId);
                    orderDateView.setText("Placed on: " + dateString);
                    orderTotalView.setText("Total: ₹" + totalAmount);

                    // Populate order items
                    DataSnapshot itemsSnapshot = orderSnapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        Log.d("OrderItemDebug", itemSnapshot.getValue().toString());

                        String name = itemSnapshot.child("name").getValue(String.class);
                        Long quantity = itemSnapshot.child("quantity").getValue(Long.class);
                        Long price = itemSnapshot.child("price").getValue(Long.class);

                        if (name != null && quantity != null && price != null) {
                            TextView item = new TextView(OrdersActivity.this);
                            item.setText("• " + name + " (x" + quantity + ") - ₹" + (price * quantity));
                            item.setTextColor(getResources().getColor(android.R.color.black));
                            orderItemsContainer.addView(item);
                        }
                    }

                    ordersContainer.addView(orderView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrdersActivity.this, "Failed to load orders: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("FirebaseError", "Error loading orders", error.toException());
            }
        });
    }
}
