package com.hexaware.orderManagement.entities;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private int quantityInStock;
    private String type; 

    public Product(String productName, String description, double price, int quantityInStock, String type) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.type = type;
    }


	public Product(int int1, String string, String string2, double double1, int int2, String string3) {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty.");
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty.");
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        if (quantityInStock < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantityInStock = quantityInStock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (!type.equalsIgnoreCase("Electronics") && !type.equalsIgnoreCase("Clothing")) {
            throw new IllegalArgumentException("Type must be Electronics or Clothing.");
        }
        this.type = type;
    }
    
    // toString
    @Override
   	public String toString() {
   		return "Product [productId=" + productId + ", productName=" + productName + ", description=" + description
   				+ ", price=" + price + ", quantityInStock=" + quantityInStock + ", type=" + type + "]";
   	}
}