package com.hexaware.orderManagement.Main;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hexaware.orderManagement.dao.OrderProcessor;
import com.hexaware.orderManagement.entities.Clothing;
import com.hexaware.orderManagement.entities.Electronics;
import com.hexaware.orderManagement.entities.Product;
import com.hexaware.orderManagement.entities.User;
import com.hexaware.orderManagement.exception.OrderNotFoundException;
import com.hexaware.orderManagement.exception.UserNotFoundException;
import com.hexaware.orderManagement.exception.ProductNotFoundException;

public class OrderManagement {

    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderProcessor orderProcessor = new OrderProcessor();

    public static void main(String[] args) {
        System.out.println("Welcome to the Order Management System!");

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    createProduct();
                    break;
                case 3:
                    createOrder();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    getAllProducts();
                    break;
                case 6:
                    getOrderByUser();
                    break;
                case 7:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Create User");
        System.out.println("2. Create Product");
        System.out.println("3. Create Order");
        System.out.println("4. Cancel Order");
        System.out.println("5. View All Products");
        System.out.println("6. View Orders by User");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!isValidPassword(password)) {
            System.out.println("Password must be at least 6 characters long and contain at least one special character.");
            return;
        }

        System.out.print("Enter role (Admin/User): ");
        String role = scanner.nextLine();
        if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("User")) {
            System.out.println("Invalid role. Please enter either 'Admin' or 'User'.");
            return;
        }

        User user = new User(username, password, role);
        try {
            orderProcessor.createUser(user);
            System.out.println("User created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    private static void createProduct() {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        if (productName.isEmpty()) {
            System.out.println("Product name cannot be empty.");
            return;
        }

        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        if (price <= 0) {
            System.out.println("Price must be a positive number.");
            return;
        }

        System.out.print("Enter quantity in stock: ");
        int quantityInStock = scanner.nextInt();
        if (quantityInStock < 0) {
            System.out.println("Quantity in stock cannot be negative.");
            return;
        }

        System.out.print("Enter type (Electronics/Clothing): ");
        String type = scanner.nextLine();
        if (!type.equalsIgnoreCase("Electronics") && !type.equalsIgnoreCase("Clothing")) {
            System.out.println("Invalid product type. Please enter 'Electronics' or 'Clothing'.");
            return;
        }

        Product product;
        if ("Electronics".equalsIgnoreCase(type)) {
            System.out.print("Enter brand: ");
            String brand = scanner.nextLine();
            System.out.print("Enter warranty period: ");
            int warrantyPeriod = scanner.nextInt();
            scanner.nextLine();
            product = new Electronics(productName, description, price, quantityInStock, type, brand, warrantyPeriod);
        } else {
            System.out.print("Enter size: ");
            String size = scanner.nextLine();
            System.out.print("Enter color: ");
            String color = scanner.nextLine();
            product = new Clothing(productName, description, price, quantityInStock, type, size, color);
        }

        try {
            orderProcessor.createProduct(new User("admin", "admin", "Admin"), product);
            System.out.println("Product created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
        }
    }

    private static void createOrder() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        if (userId <= 0) {
            System.out.println("User ID must be a positive number.");
            return;
        }
    
        System.out.print("Enter product IDs (comma separated): ");
        String productIds = scanner.nextLine();
        String[] productIdArray = productIds.split(",");

        try {
            List<Product> products = orderProcessor.getProductsByIds(productIdArray);
            User user = new User(userId);
            orderProcessor.createOrder(user, products);
            System.out.println("Order created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
    }

    private static void cancelOrder() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();

        if (userId <= 0 || orderId <= 0) {
            System.out.println("User ID and Order ID must be positive numbers.");
            return;
        }

        try {
            orderProcessor.cancelOrder(userId, orderId);
            System.out.println("Order canceled successfully.");
        } catch (UserNotFoundException | OrderNotFoundException | SQLException e) {
            System.err.println("Error canceling order: " + e.getMessage());
        }
    }

    private static void getAllProducts() {
        try {
            List<Product> products = orderProcessor.getAllProducts();
            System.out.println("Products in the system:");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }
    }

    private static void getOrderByUser() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();

        if (userId <= 0) {
            System.out.println("User ID must be a positive number.");
            return;
        }

        try {
            User user = new User(userId);
            List<Product> products = orderProcessor.getOrderByUser(user);
            System.out.println("Orders by user ID " + userId + ":");
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.err.println("Error fetching orders for user: " + e.getMessage());
        }
    }

    private static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
