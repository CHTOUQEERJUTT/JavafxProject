package com.example;

import java.util.ArrayList;

public class Order {
    int orderId;
    ArrayList<CartItem> purchasedItems = new ArrayList<>();
    double totalAmount;

    @Override
    public String toString() {
        return "Order #" + orderId + " | Items: " + purchasedItems + " | Total: RS" + totalAmount;
    }

}
