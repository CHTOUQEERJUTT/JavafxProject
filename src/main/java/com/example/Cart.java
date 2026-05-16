package com.example;

import java.util.ArrayList;

public class Cart {
    ArrayList<CartItem> items = new ArrayList<>();

    public void addItem(Product p,int quantity){
        if (p.stockQuantity>=quantity) {
            CartItem newItem = new CartItem();
            newItem.product = p;
            newItem.quantity = quantity;

            items.add(newItem);
            p.stockQuantity-=quantity;
        } else{
            System.out.println("Not enough stock!");
        }
    }

    public void getCartItems(){
        System.out.println(items);
    }
    public void clearCart() {
        items.clear();
    }

}
