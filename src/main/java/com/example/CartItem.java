package com.example;

public class CartItem {
    Product product;
    int quantity;

    @Override
    public String toString() {
        return quantity + "x " + product.name + " ($" + (product.price * quantity) + ")";
    }

}
