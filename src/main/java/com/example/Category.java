package com.example;
import java.io.Serializable;

public class Category implements Serializable {
    String name;

    Category(String name) {
        this.name = name;
    }
}