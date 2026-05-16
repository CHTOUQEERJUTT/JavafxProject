package com.example;

public class Product {
    String name;
    double price;
    int stockQuantity;

    Category category;

    // Pass the category in as a parameter
    Product(String name, double price, int stockQuantity, Category category) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity; // Fixed missing assignment
        this.category = category; // Links to an existing category
    }

    public void getCategory(){
         System.out.println(this.category.name);
    }
}