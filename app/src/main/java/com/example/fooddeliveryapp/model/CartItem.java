
package com.example.fooddeliveryapp.model;

public class CartItem {
    private String name;
    private int quantity;
    private int price; // single item price
    private int totalPrice;

    public CartItem() {
        // required for Firebase
    }

    public CartItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public void setPrice(int price) {
        this.price = price;
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        this.totalPrice = this.price * this.quantity;
    }
}
