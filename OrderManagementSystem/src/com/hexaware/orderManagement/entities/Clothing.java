package com.hexaware.orderManagement.entities;

public class Clothing extends Product {
    private String size;
    private String color;

    public Clothing(String productName, String description, double price, int quantityInStock, String type, String size, String color) {
        super(productName, description, price, quantityInStock, type);
        this.size = size;
        this.color = color;
    }

	// Getters and Setters
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (size == null || size.trim().isEmpty()) throw new IllegalArgumentException("Size cannot be empty.");
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.trim().isEmpty()) throw new IllegalArgumentException("Color cannot be empty.");
        this.color = color;
    }
    
    // toString
    @Override
   	public String toString() {
   		return "Clothing [size=" + size + ", color=" + color + "]";
   	}
}