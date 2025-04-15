package com.hexaware.orderManagement.entities;

public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;

    public Electronics(String productName, String description, double price, int quantityInStock, String type, String brand, int warrantyPeriod) {
        super(productName, description, price, quantityInStock, type);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }
    
	// Getters and Setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) throw new IllegalArgumentException("Brand cannot be empty.");
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        if (warrantyPeriod < 0) throw new IllegalArgumentException("Warranty must be non-negative.");
        this.warrantyPeriod = warrantyPeriod;
    }
    
    // toString
    @Override
	public String toString() {
		return "Electronics [brand=" + brand + ", warrantyPeriod=" + warrantyPeriod + "]";
	}

}
