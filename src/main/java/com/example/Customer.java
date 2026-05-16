package com.example;

import java.util.ArrayList;

public class Customer {
    String name;
    Cart cart = new Cart();

    ArrayList<Order> orderHistory = new ArrayList<>();

    Customer(String name){
        this.name=name;
    }

    public Order checkOut(){
        if (cart.items.isEmpty()) {
            System.out.println("Cart is empty!");
            return null;
        }
        Order newOrder = new Order();
        newOrder.orderId = (int)(Math.random() * 1000);

        for (CartItem item : cart.items) {
            newOrder.purchasedItems.add(item);
            newOrder.totalAmount+=(item.product.price * item.quantity);

        }
        orderHistory.add(newOrder);
        cart.clearCart();

        System.out.println("Order placed successfully! Total: $" + newOrder.totalAmount);

        return newOrder;

        

    }


}
