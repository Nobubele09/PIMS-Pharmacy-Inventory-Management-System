-- =========================================================
-- PHARMACY INVENTORY MANAGEMENT SYSTEM
-- HealthFirst Pharmacy
-- Programming 732
-- =========================================================

-- Create database
CREATE DATABASE IF NOT EXISTS pims_database;

USE pims_database;


-- =========================================================
-- TABLE: users
-- =========================================================

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Cashier') NOT NULL,
    full_name VARCHAR(100) NOT NULL
);


-- =========================================================
-- TABLE: suppliers
-- =========================================================

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT
);


-- =========================================================
-- TABLE: medicines
-- =========================================================

CREATE TABLE IF NOT EXISTS medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    company VARCHAR(100) NOT NULL,
    medicine_type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity_in_stock INT NOT NULL,
    reorder_level INT NOT NULL,
    expiry_date DATE NOT NULL,
    supplier_id INT NOT NULL,

    CONSTRAINT fk_medicine_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(supplier_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- =========================================================
-- TABLE: sales
-- =========================================================

CREATE TABLE IF NOT EXISTS sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    user_id INT NOT NULL,

    CONSTRAINT fk_sale_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- =========================================================
-- TABLE: sale_items
-- =========================================================

CREATE TABLE IF NOT EXISTS sale_items (
    sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity_sold INT NOT NULL,
    price_at_sale DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_sale_item_sale
        FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_sale_item_medicine
        FOREIGN KEY (medicine_id)
        REFERENCES medicines(medicine_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- =========================================================
-- SAMPLE USERS
-- =========================================================

INSERT INTO users
(username, password, role, full_name)
VALUES
('admin', 'admin123', 'Admin', 'System Administrator'),
('cashier', 'cashier123', 'Cashier', 'HealthFirst Cashier');


-- =========================================================
-- SAMPLE SUPPLIERS
-- =========================================================

INSERT INTO suppliers
(name, contact_person, phone, email, address)
VALUES
(
    'MedSupply Distributors',
    'John Mokoena',
    '0115551001',
    'info@medsupply.co.za',
    'Johannesburg, South Africa'
),
(
    'PharmaCare Suppliers',
    'Sarah Dlamini',
    '0115551002',
    'info@pharmacare.co.za',
    'Pretoria, South Africa'
),
(
    'HealthMed Wholesalers',
    'Peter Nkosi',
    '0115551003',
    'info@healthmed.co.za',
    'Midrand, South Africa'
);


-- =========================================================
-- SAMPLE MEDICINES
-- =========================================================

INSERT INTO medicines
(name, company, medicine_type, price, quantity_in_stock,
 reorder_level, expiry_date, supplier_id)
VALUES

(
    'Paracetamol 500mg',
    'Adcock Ingram',
    'Tablet',
    25.50,
    100,
    20,
    '2027-06-30',
    1
),

(
    'Amoxicillin 500mg',
    'Aspen Pharmacare',
    'Capsule',
    75.00,
    50,
    15,
    '2027-04-15',
    2
),

(
    'Cough Syrup 100ml',
    'Johnson & Johnson',
    'Syrup',
    45.99,
    12,
    20,
    '2026-12-20',
    1
),

(
    'Vitamin C 500mg',
    'Clicks Health',
    'Tablet',
    35.00,
    80,
    20,
    '2028-01-10',
    3
),

(
    'Hydrocortisone Cream',
    'Aspen Pharmacare',
    'Cream',
    32.50,
    8,
    15,
    '2026-11-30',
    2
),

(
    'Ibuprofen 400mg',
    'Adcock Ingram',
    'Tablet',
    42.50,
    75,
    20,
    '2027-08-15',
    1
),

(
    'Aspirin 100mg',
    'Bayer',
    'Tablet',
    38.00,
    60,
    15,
    '2027-11-20',
    1
),

(
    'Metformin 500mg',
    'Aspen Pharmacare',
    'Tablet',
    55.00,
    70,
    20,
    '2028-02-28',
    2
);


-- =========================================================
-- VERIFY DATABASE
-- =========================================================

SELECT * FROM users;

SELECT * FROM suppliers;

SELECT * FROM medicines;

SELECT * FROM sales;

SELECT * FROM sale_items;