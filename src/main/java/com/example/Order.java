package com.example;
import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    int orderId;
    ArrayList<CartItem> purchasedItems = new ArrayList<>();
    double totalAmount;

    @Override
    public String toString() {
        return "Order #" + orderId + " | Items: " + purchasedItems.size() + " | Total: $" + totalAmount;
    }
}