package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class App extends Application {

    public static HashMap<String, Customer> registeredUsers = new HashMap<>();
    public static Customer currentCustomer;
    public static ArrayList<Product> storeInventory = new ArrayList<>();
    public static PaymentProcessing paymentGateway = new PaymentProcessing();
    
    private Stage mainWindow;

    // --- REUSABLE UI STYLES FOR BEGINNERS ---
    String mainBgStyle = "-fx-background-color: #F4F7F6; -fx-font-family: 'Segoe UI', sans-serif;";
    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;";
    String primaryBtnStyle = "-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
    String secondaryBtnStyle = "-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
    String successBtnStyle = "-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
    String dangerBtnStyle = "-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;";
    String inputStyle = "-fx-padding: 10; -fx-border-color: #BDC3C7; -fx-border-radius: 5; -fx-background-radius: 5;";

    public static void saveDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("database.dat"))) {
            oos.writeObject(registeredUsers);
            System.out.println("Database saved.");
        } catch (Exception e) {
            System.out.println("Error saving database: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadDatabase() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.dat"))) {
            registeredUsers = (HashMap<String, Customer>) ois.readObject();
            System.out.println("Database loaded.");
        } catch (Exception e) {
            registeredUsers = new HashMap<>();
            System.out.println("No database found. Starting fresh.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainWindow = primaryStage;
        mainWindow.setTitle("Modern E-Commerce Store");

        loadDatabase();

        Category category = new Category("Clothing");
        storeInventory.add(new Product("Classic Shirt", 500, 10, category));
        storeInventory.add(new Product("Denim Bottoms", 3000, 50, category));
        storeInventory.add(new Product("Winter Hoodie", 4500, 100, category));

        mainWindow.setScene(createLoginScene());
        mainWindow.show();
    }

    private Scene createLoginScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(mainBgStyle);

        Label titleLabel = new Label("Welcome Back!");
        titleLabel.setStyle(titleStyle);

        Label errorLabel = new Label(""); 
        errorLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("Username");
        nameInput.setMaxWidth(250);
        nameInput.setStyle(inputStyle);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setMaxWidth(250);
        passwordInput.setStyle(inputStyle);

        Button loginButton = new Button("Login to Store");
        loginButton.setStyle(primaryBtnStyle);

        Button goToRegisterButton = new Button("Create an Account");
        goToRegisterButton.setStyle(secondaryBtnStyle);

        loginButton.setOnAction(event -> {
            String username = nameInput.getText().trim();
            String password = passwordInput.getText().trim();

            if (registeredUsers.containsKey(username)) {
                Customer foundCustomer = registeredUsers.get(username);
                if (foundCustomer.password.equals(password)) {
                    currentCustomer = foundCustomer;
                    mainWindow.setScene(createStoreScene());
                } else {
                    errorLabel.setText("Incorrect password!");
                }
            } else {
                errorLabel.setText("Username not found. Please register.");
            }
        });

        goToRegisterButton.setOnAction(event -> mainWindow.setScene(createRegisterScene()));

        layout.getChildren().addAll(titleLabel, errorLabel, nameInput, passwordInput, loginButton, goToRegisterButton);
        return new Scene(layout, 640, 480);
    }

    private Scene createRegisterScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(mainBgStyle);

        Label titleLabel = new Label("Register New Account");
        titleLabel.setStyle(titleStyle);

        Label errorLabel = new Label(""); 
        errorLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("Choose Username");
        nameInput.setMaxWidth(250);
        nameInput.setStyle(inputStyle);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Choose Password");
        passwordInput.setMaxWidth(250);
        passwordInput.setStyle(inputStyle);

        Button registerButton = new Button("Sign Up");
        registerButton.setStyle(successBtnStyle);

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setStyle(secondaryBtnStyle);

        registerButton.setOnAction(event -> {
            String username = nameInput.getText().trim();
            String password = passwordInput.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Fields cannot be empty!");
            } else if (registeredUsers.containsKey(username)) {
                errorLabel.setText("Username already exists!");
            } else {
                Customer newCustomer = new Customer(username, password);
                registeredUsers.put(username, newCustomer);
                saveDatabase();
                mainWindow.setScene(createLoginScene());
            }
        });

        backToLoginButton.setOnAction(event -> mainWindow.setScene(createLoginScene()));

        layout.getChildren().addAll(titleLabel, errorLabel, nameInput, passwordInput, registerButton, backToLoginButton);
        return new Scene(layout, 640, 480);
    }

    private Scene createStoreScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(mainBgStyle);

        Label welcomeLabel = new Label("Hello, " + currentCustomer.name + "! Store Inventory:");
        welcomeLabel.setStyle(titleStyle);
        mainLayout.getChildren().add(welcomeLabel);

        // Product List
        for (Product product : storeInventory) {
            HBox productRow = new HBox(20);
            productRow.setAlignment(Pos.CENTER);
            productRow.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
            productRow.setMaxWidth(450);

            Label productDetails = new Label(product.name + "  |  $" + product.price + "  |  Stock: " + product.stockQuantity);
            productDetails.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495E;");
            
            // Push the button to the right side
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button addButton = new Button("Add to Cart");
            addButton.setStyle(primaryBtnStyle);

            addButton.setOnAction(event -> {
                currentCustomer.cart.addItem(product, 1);
                productDetails.setText(product.name + "  |  $" + product.price + "  |  Stock: " + product.stockQuantity);
            });

            productRow.getChildren().addAll(productDetails, spacer, addButton);
            mainLayout.getChildren().add(productRow);
        }

        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button goToCartButton = new Button("Go to Cart ->");
        goToCartButton.setStyle(successBtnStyle);
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(dangerBtnStyle);

        goToCartButton.setOnAction(event -> mainWindow.setScene(createCartScene()));
        logoutButton.setOnAction(event -> {
            currentCustomer = null;
            mainWindow.setScene(createLoginScene());
        });
        
        actionButtons.getChildren().addAll(logoutButton, goToCartButton);
        mainLayout.getChildren().add(actionButtons);

        return new Scene(mainLayout, 640, 550);
    }

    private Scene createCartScene() {
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle(mainBgStyle);

        Label titleLabel = new Label("Your Shopping Cart");
        titleLabel.setStyle(titleStyle);
        mainLayout.getChildren().add(titleLabel);

        VBox cartItemsBox = new VBox(10);
        cartItemsBox.setAlignment(Pos.CENTER);
        cartItemsBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        cartItemsBox.setMaxWidth(450);

        if (currentCustomer.cart.items.isEmpty()) {
            Label emptyLabel = new Label("Your cart is currently empty.");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7F8C8D;");
            cartItemsBox.getChildren().add(emptyLabel);
        } else {
            for (CartItem item : currentCustomer.cart.items) {
                Label itemLabel = new Label(item.toString());
                itemLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495E;");
                cartItemsBox.getChildren().add(itemLabel);
            }
        }
        mainLayout.getChildren().add(cartItemsBox);

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #E67E22;");

        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setStyle(primaryBtnStyle);

        Button backToStoreButton = new Button("<- Back to Store");
        backToStoreButton.setStyle(secondaryBtnStyle);

        HBox paymentBox = new HBox(10);
        paymentBox.setAlignment(Pos.CENTER);
        
        TextField paymentInput = new TextField();
        paymentInput.setPromptText("Enter exact amount");
        paymentInput.setStyle(inputStyle);
        
        Button payButton = new Button("Pay Now");
        payButton.setStyle(successBtnStyle);
        
        paymentBox.getChildren().addAll(new Label("Payment: $"), paymentInput, payButton);
        paymentBox.setVisible(false);

        final Order[] pendingOrder = new Order[1];

        placeOrderButton.setOnAction(event -> {
            pendingOrder[0] = currentCustomer.checkOut();
            if (pendingOrder[0] != null) {
                statusLabel.setText("Order Placed! Total Due: $" + pendingOrder[0].totalAmount);
                placeOrderButton.setDisable(true);
                paymentBox.setVisible(true);
            } else {
                statusLabel.setText("Cart is empty!");
            }
        });

        payButton.setOnAction(event -> {
            try {
                double enteredAmount = Double.parseDouble(paymentInput.getText());
                String resultMessage = paymentGateway.processPayment(pendingOrder[0], enteredAmount);
                
                if (enteredAmount >= pendingOrder[0].totalAmount) {
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2ECC71;"); // Green success
                    statusLabel.setText(resultMessage);
                    paymentBox.setDisable(true);
                    saveDatabase(); 
                } else {
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #E74C3C;"); // Red error
                    statusLabel.setText(resultMessage);
                }
            } catch (NumberFormatException e) {
                statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #E74C3C;");
                statusLabel.setText("Invalid input! Please enter a number.");
            }
        });

        backToStoreButton.setOnAction(event -> mainWindow.setScene(createStoreScene()));

        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.getChildren().addAll(backToStoreButton, placeOrderButton);

        mainLayout.getChildren().addAll(statusLabel, actionButtons, paymentBox);
        return new Scene(mainLayout, 640, 550);
    }

    public static void main(String[] args) {
        launch(args);
    }
}