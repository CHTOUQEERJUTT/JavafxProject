package com.example;
import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    String name;
    String password;
    Cart cart = new Cart();
    ArrayList<Order> orderHistory = new ArrayList<>();

    Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Order checkOut() {
        if (cart.items.isEmpty()) return null;
        
        Order newOrder = new Order();
        newOrder.orderId = (int)(Math.random() * 1000);

        for (CartItem item : cart.items) {
            newOrder.purchasedItems.add(item);
            newOrder.totalAmount += (item.product.price * item.quantity);
        }
        
        orderHistory.add(newOrder);
        cart.clearCart();
        return newOrder;
    }
}