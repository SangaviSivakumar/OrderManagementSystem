package com.hexaware.orderManagement.entities;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(int userId) {
        this.userId = userId;
    }

	// Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be positive.");
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) throw new IllegalArgumentException("Username cannot be empty.");
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password must be at least 4 characters.");
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("User")) {
            throw new IllegalArgumentException("Role must be Admin or User.");
        }
        this.role = role;
    }
    
    // toString
    @Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}
}
