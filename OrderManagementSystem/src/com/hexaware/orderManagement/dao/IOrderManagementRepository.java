package com.hexaware.orderManagement.dao;

import java.sql.SQLException;
import java.util.List;

import com.hexaware.orderManagement.entities.Product;
import com.hexaware.orderManagement.entities.User;
import com.hexaware.orderManagement.exception.ProductNotFoundException;

public interface IOrderManagementRepository {

    void createUser(User user) throws Exception;
    void createProduct(User admin, Product product) throws Exception;
    void createOrder(User user, List<Product> productList) throws Exception;
    void cancelOrder(int userId, int orderId) throws Exception;
    
    List<Product> getAllProducts() throws Exception;
    List<Product> getOrderByUser(User user) throws Exception;
    List<Product> getProductsByIds(String[] productIds) throws SQLException, ProductNotFoundException;
}
