package com.hexaware.orderManagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/OrderManagement?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "San#2003*"; // Replace with your actual DB password

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Failed to establish connection to the database.");
            }
            conn.setAutoCommit(true); 
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

}
