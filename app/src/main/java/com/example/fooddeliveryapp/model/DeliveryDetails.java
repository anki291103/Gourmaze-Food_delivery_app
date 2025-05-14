package com.example.fooddeliveryapp;

public class DeliveryDetails {
    public String name, phone, address, pincode;

    public DeliveryDetails() {}

    public DeliveryDetails(String name, String phone, String address, String pincode) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.pincode = pincode;
    }
}
