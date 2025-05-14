package com.example.fooddeliveryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.model.CartItem;
import com.example.fooddeliveryapp.model.FoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private List<FoodItem> fullList;

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
        this.fullList = new ArrayList<>(foodList);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        holder.image.setImageResource(item.getImageResId());

        // Reset quantity for each bind
        holder.quantity = 1;
        holder.quantityText.setText(String.valueOf(holder.quantity));

        holder.btnIncrease.setOnClickListener(v -> {
            holder.quantity++;
            holder.quantityText.setText(String.valueOf(holder.quantity));
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (holder.quantity > 1) {
                holder.quantity--;
                holder.quantityText.setText(String.valueOf(holder.quantity));
            }
        });

        holder.addBtn.setOnClickListener(v -> {
            String message = item.getName() + " x" + holder.quantity + " added to cart";
            Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();

            // Firebase cart logic
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child(userId)
                        .child(item.getName());

                int quantity = holder.quantity;
                // Clean price (if needed, strip ₹ or $)
                String cleanPriceStr = item.getPrice().replaceAll("[^0-9]", "");
                int price = Integer.parseInt(cleanPriceStr);

                CartItem cartItem = new CartItem(item.getName(), quantity, price);
                cartRef.setValue(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void filter(String query) {
        foodList.clear();
        if (query.isEmpty()) {
            foodList.addAll(fullList);
        } else {
            for (FoodItem item : fullList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    foodList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantityText;
        ImageView image;
        Button addBtn, btnIncrease, btnDecrease;
        int quantity = 1;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodName);
            price = itemView.findViewById(R.id.foodPrice);
            image = itemView.findViewById(R.id.foodImage);
            addBtn = itemView.findViewById(R.id.addToCartBtn);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            quantityText = itemView.findViewById(R.id.quantityText);
        }
    }
}
