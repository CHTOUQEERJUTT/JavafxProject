package com.example;
import java.io.Serializable;

public class CartItem implements Serializable {
    Product product;
    int quantity;

    @Override
    public String toString() {
        return quantity + "x " + product.name + " ($" + (product.price * quantity) + ")";
    }
}