package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapp.adapter.FoodAdapter;
import com.example.fooddeliveryapp.model.FoodItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuPage extends AppCompatActivity {

    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<FoodItem> foodList = new ArrayList<>();
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.foodRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchBar = findViewById(R.id.searchBar);

        populateFoodItems();

        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        setupBottomNav();
    }


    private void populateFoodItems() {
        foodList.add(new FoodItem("Herbal Pancake", "₹100", R.drawable.menu1));
        foodList.add(new FoodItem("Fruit Salad", "₹80", R.drawable.menu2));
        foodList.add(new FoodItem("Green Noodle", "₹120", R.drawable.menu3));
        foodList.add(new FoodItem("Spring Roll", "₹90", R.drawable.menu4));
        foodList.add(new FoodItem("Icecream", "₹70", R.drawable.menu5));
        foodList.add(new FoodItem("Cheesy Pizza", "₹150", R.drawable.menu6));
        foodList.add(new FoodItem("Veg Burger", "₹110", R.drawable.menu7));
        foodList.add(new FoodItem("Tofu Wrap", "₹100", R.drawable.menu8));
        foodList.add(new FoodItem("Sushi Rolls", "₹180", R.drawable.menu9));
        foodList.add(new FoodItem("Paneer Tikka", "₹140", R.drawable.menu10));
        foodList.add(new FoodItem("Chocolate Cake", "₹90", R.drawable.menu11));
        foodList.add(new FoodItem("Avocado Toast", "₹130", R.drawable.menu12));
        foodList.add(new FoodItem("Garlic Bread", "₹60", R.drawable.menu13));
        foodList.add(new FoodItem("Pasta Alfredo", "₹160", R.drawable.menu14));
        foodList.add(new FoodItem("Greek Salad", "₹100", R.drawable.menu15));
        foodList.add(new FoodItem("Vegan Biryani", "₹130", R.drawable.menu16));
        foodList.add(new FoodItem("Falafel Bowl", "₹120", R.drawable.menu17));
        foodList.add(new FoodItem("Tomato Soup", "₹70", R.drawable.menu18));
        foodList.add(new FoodItem("Veg Momos", "₹90", R.drawable.menu19));
        foodList.add(new FoodItem("Brownie Blast", "₹100", R.drawable.menu20));
        foodList.add(new FoodItem("Quinoa Salad", "₹110", R.drawable.menu21));
        foodList.add(new FoodItem("Stuffed Paratha", "₹80", R.drawable.menu22));
        foodList.add(new FoodItem("Paneer Wrap", "₹120", R.drawable.menu23));
        foodList.add(new FoodItem("Veg Lasagna", "₹150", R.drawable.menu24));
        foodList.add(new FoodItem("Hummus Platter", "₹100", R.drawable.menu25));
        foodList.add(new FoodItem("Banana Smoothie", "₹70", R.drawable.menu26));
        foodList.add(new FoodItem("Tofu Curry", "₹130", R.drawable.menu27));
        foodList.add(new FoodItem("Vegan Tacos", "₹140", R.drawable.menu28));
        foodList.add(new FoodItem("Masala Dosa", "₹90", R.drawable.menu29));
        foodList.add(new FoodItem("Chole Bhature", "₹100", R.drawable.menu30));
        foodList.add(new FoodItem("Idli Sambar", "₹80", R.drawable.menu31));
        foodList.add(new FoodItem("Vegetable Korma", "₹120", R.drawable.menu32));
        foodList.add(new FoodItem("Pumpkin Soup", "₹85", R.drawable.menu33));
        foodList.add(new FoodItem("Peanut Butter Toast", "₹60", R.drawable.menu34));
        foodList.add(new FoodItem("Zucchini Noodles", "₹130", R.drawable.menu35));
        foodList.add(new FoodItem("Spinach Corn Sandwich", "₹100", R.drawable.menu36));
        foodList.add(new FoodItem("Coconut Rice", "₹90", R.drawable.menu37));
        foodList.add(new FoodItem("Sweet Corn Chaat", "₹70", R.drawable.menu38));
        foodList.add(new FoodItem("Broccoli Stir Fry", "₹110", R.drawable.menu39));
        foodList.add(new FoodItem("Vegan Ice Cream", "₹95", R.drawable.menu40));
        foodList.add(new FoodItem("Rajma Chawal", "₹100", R.drawable.menu41));
        foodList.add(new FoodItem("Palak Paneer", "₹130", R.drawable.menu42));
        foodList.add(new FoodItem("Baingan Bharta", "₹90", R.drawable.menu43));
        foodList.add(new FoodItem("Kadhi Pakora", "₹100", R.drawable.menu44));
        foodList.add(new FoodItem("Aloo Gobi", "₹85", R.drawable.menu45));
        foodList.add(new FoodItem("Matar Paneer", "₹120", R.drawable.menu46));
        foodList.add(new FoodItem("Dhokla", "₹70", R.drawable.menu47));
        foodList.add(new FoodItem("Pav Bhaji", "₹90", R.drawable.menu48));
        foodList.add(new FoodItem("Sambar Vada", "₹80", R.drawable.menu49));
        foodList.add(new FoodItem("Tandoori Roti & Sabzi", "₹100", R.drawable.menu50));
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_search);

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(MenuPage.this, HomeActivity.class));
                    return true;
                } else if (id == R.id.nav_cart) {
                    startActivity(new Intent(MenuPage.this, CartActivity.class));
                    Toast.makeText(MenuPage.this, "Navigating to Cart", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_orders) {
                    startActivity(new Intent(MenuPage.this, OrdersActivity.class));
                    return true;
                } else if (id == R.id.nav_search) {
                    return true; // Already here
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(MenuPage.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });
    }
}
