package com.hexaware.orderManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.orderManagement.entities.Clothing;
import com.hexaware.orderManagement.entities.Electronics;
import com.hexaware.orderManagement.entities.Product;
import com.hexaware.orderManagement.entities.User;
import com.hexaware.orderManagement.exception.InvalidAccessException;
import com.hexaware.orderManagement.exception.OrderNotFoundException;
import com.hexaware.orderManagement.exception.ProductNotFoundException;
import com.hexaware.orderManagement.exception.UserNotFoundException;
import com.hexaware.orderManagement.util.DBUtil;

public class OrderProcessor implements IOrderManagementRepository {

    private Connection conn;

    public OrderProcessor() {
        try {
            this.conn = DBUtil.getConnection(); // Make sure DB connection is set up
        } catch (SQLException e) {
            System.err.println("Error initializing OrderProcessor: " + e.getMessage());
            this.conn = null;  // Set to null or handle failure accordingly
        }
    }

    @Override
    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (userId, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        }
    }

    @Override
    public void createProduct(User admin, Product product) throws Exception {
        if (!admin.getRole().equalsIgnoreCase("Admin")) {
            throw new InvalidAccessException("Only admin can add products.");
        }

        String sql = "INSERT INTO products (productId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantityInStock());
            stmt.setString(6, product.getType());
            stmt.executeUpdate();
        }
    }

    @Override
    public void createOrder(User user, List<Product> productList) throws Exception {
        // Check if user exists
        String userCheck = "SELECT * FROM users WHERE userId=?";
        try (PreparedStatement checkStmt = conn.prepareStatement(userCheck)) {
            checkStmt.setInt(1, user.getUserId());
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // user not found -> create user first
                createUser(user);
            }
        }

        // Insert into orders table
        String orderInsert = "INSERT INTO orders (userId, orderDate) VALUES (?, CURRENT_DATE)";
        int orderId;
        try (PreparedStatement stmt = conn.prepareStatement(orderInsert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, user.getUserId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                orderId = keys.getInt(1);
            } else {
                throw new SQLException("Failed to get order ID.");
            }
        }

        // Insert products into order_details
        String orderDetail = "INSERT INTO order_details (orderId, productId) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(orderDetail)) {
            for (Product p : productList) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, p.getProductId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException, SQLException {
        // Check if user exists
        String userQuery = "SELECT * FROM users WHERE userId=?";
        try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                throw new UserNotFoundException("User with ID " + userId + " not found.");
            }
        }

        // Check if order exists for that user
        String orderQuery = "SELECT * FROM orders WHERE orderId=? AND userId=?";
        try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery)) {
            orderStmt.setInt(1, orderId);
            orderStmt.setInt(2, userId);
            ResultSet orderRs = orderStmt.executeQuery();

            if (!orderRs.next()) {
                throw new OrderNotFoundException("Order with ID " + orderId + " not found for user ID " + userId + ".");
            }
        }

        // Delete order details first (foreign key constraint)
        String deleteOrderDetails = "DELETE FROM order_details WHERE orderId=?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteOrderDetails)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }

        // Delete the order itself
        String deleteOrder = "DELETE FROM orders WHERE orderId=?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteOrder)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }

        System.out.println("Order ID " + orderId + " cancelled successfully.");
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("quantityInStock"),
                    rs.getString("type")
                );
                products.add(p);
            }
        }
        return products;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws Exception {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.* FROM products p " +
                     "JOIN order_details od ON p.productId = od.productId " +
                     "JOIN orders o ON od.orderId = o.orderId " +
                     "WHERE o.userId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("productId"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("quantityInStock"),
                    rs.getString("type")
                );
                products.add(p);
            }
        }
        return products;
    }
    @Override
    public List<Product> getProductsByIds(String[] productIds) throws SQLException, ProductNotFoundException {
        List<Product> products = new ArrayList<>();
        Connection conn = DBUtil.getConnection();

        String sql = "SELECT * FROM product WHERE productId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (String idStr : productIds) {
            int productId = Integer.parseInt(idStr.trim());
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type");
                Product product;
                if ("Electronics".equalsIgnoreCase(type)) {
                    product = new Electronics(
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type"),
                        rs.getString("brand"),
                        rs.getInt("warrantyPeriod")
                    );
                } else if ("Clothing".equalsIgnoreCase(type)) {
                    product = new Clothing(
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type"),
                        rs.getString("size"),
                        rs.getString("color")
                    );
                } else {
                    throw new ProductNotFoundException("Unknown product type for productId " + productId);
                }

                product.setProductId(productId);  // optional
                products.add(product);
            } else {
                throw new ProductNotFoundException("Product with ID " + productId + " not found.");
            }
        }

        return products;
    }

   
}
