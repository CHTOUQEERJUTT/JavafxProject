package com.example;
import java.io.Serializable;

public class Product implements Serializable {
    String name;
    double price;
    int stockQuantity;
    Category category;

    Product(String name, double price, int stockQuantity, Category category) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
}