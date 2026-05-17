package com.example;

public class PaymentProcessing {
    public String processPayment(Order order, double enteredAmount) {
        if (enteredAmount == order.totalAmount) {
            return "Payment completed successfully! Thank you.";
        } else if (enteredAmount > order.totalAmount) {
            double change = enteredAmount - order.totalAmount;
            return "Payment completed! Returning change: $" + change;
        } else {
            return "Payment failed. Amount is insufficient.";
        }
    }
}