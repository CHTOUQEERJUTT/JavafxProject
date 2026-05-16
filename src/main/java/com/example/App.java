package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {

    public static Customer currentCustomer;
    public static ArrayList<Product> storeInventory = new ArrayList<>();
    public static PaymentProcessing paymentGateway = new PaymentProcessing();
    
    private Stage mainWindow;

    @Override
    public void start(Stage primaryStage) {
        this.mainWindow = primaryStage;
        mainWindow.setTitle("My E-Commerce Store");

        Category category = new Category("Clothing");
        storeInventory.add(new Product("Shirt", 500, 10, category));
        storeInventory.add(new Product("Bottom", 3000, 50, category));
        storeInventory.add(new Product("Hoodie", 4500, 100, category));

        mainWindow.setScene(createLoginScene());
        mainWindow.show();
    }

    private Scene createLoginScene() {
        Label welcomeLabel = new Label("Welcome! Please enter your name:");
        TextField nameInput = new TextField();
        nameInput.setMaxWidth(200);
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> {
            String typedName = nameInput.getText();
            
            if (typedName != null && !typedName.trim().isEmpty()) {
                currentCustomer = new Customer(typedName);
                mainWindow.setScene(createStoreScene());
            }
        });

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(welcomeLabel, nameInput, loginButton);

        return new Scene(layout, 640, 480);
    }

    private Scene createStoreScene() {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Hello, " + currentCustomer.name + "! Here are our products:");
        mainLayout.getChildren().add(welcomeLabel);

        for (Product product : storeInventory) {
            HBox productRow = new HBox(20);
            productRow.setAlignment(Pos.CENTER);

            Label productDetails = new Label(product.name + " - $" + product.price);
            Button addButton = new Button("Add to Cart");

            addButton.setOnAction(event -> {
                currentCustomer.cart.addItem(product, 1);
            });

            productRow.getChildren().addAll(productDetails, addButton);
            mainLayout.getChildren().add(productRow);
        }

        Button goToCartButton = new Button("Go to Cart");
        goToCartButton.setOnAction(event -> {
            mainWindow.setScene(createCartScene());
        });
        
        mainLayout.getChildren().add(goToCartButton);

        return new Scene(mainLayout, 640, 480);
    }

    private Scene createCartScene() {
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("--- Your Shopping Cart ---");
        mainLayout.getChildren().add(titleLabel);

        if (currentCustomer.cart.items.isEmpty()) {
            mainLayout.getChildren().add(new Label("Your cart is empty."));
        } else {
            for (CartItem item : currentCustomer.cart.items) {
                Label itemLabel = new Label(item.quantity + "x " + item.product.name + " - $" + (item.product.price * item.quantity));
                mainLayout.getChildren().add(itemLabel);
            }
        }

        Label statusLabel = new Label("");
        Button placeOrderButton = new Button("Place Order");
        Button backToStoreButton = new Button("Back to Store");

        HBox paymentBox = new HBox(10);
        paymentBox.setAlignment(Pos.CENTER);
        TextField paymentInput = new TextField();
        paymentInput.setPromptText("Enter amount...");
        Button payButton = new Button("Pay");
        paymentBox.getChildren().addAll(new Label("Payment: $"), paymentInput, payButton);
        paymentBox.setVisible(false);

        final Order[] pendingOrder = new Order[1];

        placeOrderButton.setOnAction(event -> {
            pendingOrder[0] = currentCustomer.checkOut();
            
            if (pendingOrder[0] != null) {
                statusLabel.setText("Order Placed! Total Amount Due: $" + pendingOrder[0].totalAmount);
                placeOrderButton.setDisable(true);
                paymentBox.setVisible(true);
            } else {
                statusLabel.setText("Cannot place order. Cart is empty!");
            }
        });

        payButton.setOnAction(event -> {
            try {
                double enteredAmount = Double.parseDouble(paymentInput.getText());
                
                String resultMessage = paymentGateway.processPayment(pendingOrder[0], enteredAmount);
                
                statusLabel.setText(resultMessage);

                if (enteredAmount >= pendingOrder[0].totalAmount) {
                    paymentBox.setDisable(true);
                }
                
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid input! Please enter numbers only.");
            }
        });

        backToStoreButton.setOnAction(event -> {
            mainWindow.setScene(createStoreScene());
        });

        mainLayout.getChildren().addAll(statusLabel, placeOrderButton, paymentBox, backToStoreButton);

        return new Scene(mainLayout, 640, 480);
    }

    public static void main(String[] args) {
        launch(args);
    }
}