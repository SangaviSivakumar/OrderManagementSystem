create database ordermanagement;
use ordermanagement;

CREATE TABLE Product (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    productName VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    quantityInStock INT NOT NULL,
    type ENUM('Electronics', 'Clothing') NOT NULL
);

CREATE TABLE Electronics (
    productId INT PRIMARY KEY,
    brand VARCHAR(255),
    warrantyPeriod INT,
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

CREATE TABLE Clothing (
    productId INT PRIMARY KEY,
    size VARCHAR(50),
    color VARCHAR(50),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

CREATE TABLE Users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'User') NOT NULL
);

CREATE TABLE orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totalAmount DOUBLE,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

CREATE TABLE order_details (
    orderId INT,
    productId INT,
    quantity INT NOT NULL,
    priceAtPurchase DOUBLE NOT NULL,
    PRIMARY KEY (orderId, productId),
    FOREIGN KEY (orderId) REFERENCES OrderTable(orderId),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);
CREATE TABLE OrderStatus (
    orderId INT,
    status ENUM('Pending', 'Shipped', 'Delivered', 'Canceled') NOT NULL,
    updateDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (orderId),
    FOREIGN KEY (orderId) REFERENCES OrderTable(orderId)
);

INSERT INTO Users (username, password, role) 
VALUES
('Arun', 'arun@123', 'Admin'),
('RaviKumar', 'ravi@456', 'User'),
('Vani', 'vani@789', 'User'),
('Manoj', 'manoj@321', 'Admin'),
('Deepa', 'deepa@123', 'User'),
('Saravanan', 'sara@456', 'Admin'),
('Priya', 'priya@789', 'User'),
('Krishna', 'krishna@321', 'User');

INSERT INTO Products (productName, description, price, quantityInStock, type) 
VALUES 
('Samsung Galaxy S22', 'Smartphone with 128GB storage, AMOLED display', 79999.99, 50, 'Electronics'),
('Dell Inspiron 15', 'Laptop with 16GB RAM, 512GB SSD', 54999.99, 30, 'Electronics'),
('Puma Men\'s Running Shoes', 'Comfortable sports shoes for men', 2499.99, 100, 'Clothing'),
('Levi\'s Slim Fit Jeans', 'High-quality denim jeans', 2999.99, 75, 'Clothing'),
('Sony WH-1000XM4', 'Noise-canceling headphones', 29999.99, 20, 'Electronics'),
('Nike Women\'s Sports T-Shirt', 'Breathable cotton T-shirt for women', 1599.99, 150, 'Clothing');


INSERT INTO orders (userId, totalAmount) 
VALUES 
(1, 84998.98),   
(2, 64999.99),   
(3, 2499.99),    
(4, 64999.99),  
(5, 29999.99),   
(6, 44999.99),   
(7, 1599.99),    
(8, 2999.99); 


INSERT INTO order_details (orderId, productId, quantity, priceAtPurchase) 
VALUES
(1, 1, 1, 79999.99),  
(2, 2, 1, 54999.99),  
(3, 3, 1, 2499.99),   
(4, 4, 2, 2999.99),   
(5, 5, 1, 29999.99),  
(6, 6, 2, 1599.99),   
(7, 3, 1, 2499.99),   
(8, 4, 1, 2999.99);   
   

