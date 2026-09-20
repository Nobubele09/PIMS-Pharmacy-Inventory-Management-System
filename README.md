# Pharmacy Inventory Management System (PIMS)

## HealthFirst Pharmacy

### 1. Project Overview

The Pharmacy Inventory Management System (PIMS) is a Java-based desktop application developed for HealthFirst Pharmacy.

The system is designed to assist pharmacy staff with managing medicines, suppliers, users, inventory, sales and reports.

The application uses Java Swing for the graphical user interface, JDBC for database connectivity and MySQL for data storage.

---

## 2. Objectives

The main objectives of the system are to:

- Manage medicine information.
- Manage supplier information.
- Manage system users.
- Provide secure user authentication.
- Separate Admin and Cashier functionality.
- Allow cashiers to process medicine sales.
- Automatically update medicine stock after sales.
- Generate and save customer receipts.
- Provide useful management reports.
- Identify medicines with low stock.
- Identify medicines that have expired or are approaching expiry.

---

## 3. Technologies Used

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Workbench
- Visual Studio Code
- Git
- GitHub

---

## 4. System Users

### Administrator

The Administrator can:

- Log into the system.
- Manage medicines.
- Manage suppliers.
- Manage users.
- View sales reports.
- View item-wise sales reports.
- View low-stock reports.
- View expiry reports.
- Log out of the system.

### Cashier

The Cashier can:

- Log into the system.
- Search available medicines.
- Add medicines to the shopping cart.
- Calculate the sale total.
- Complete sales.
- Generate receipts.
- Save receipts.
- Print receipts.
- Log out of the system.

The Cashier does not have access to the administrative management functions.

---

## 5. Main System Features

### Authentication

The system provides username and password authentication.

Users are redirected to the appropriate dashboard according to their assigned role.

### Medicine Management

Administrators can:

- Add medicines.
- View medicines.
- Search medicines.
- Update medicines.
- Delete medicines.

Medicine information includes:

- Medicine name
- Company
- Medicine type
- Price
- Quantity in stock
- Reorder level
- Expiry date
- Supplier

### Supplier Management

Administrators can manage supplier records including:

- Supplier name
- Contact person
- Phone
- Email
- Address

### User Management

Administrators can manage system users and their assigned roles.

The system supports:

- Admin users
- Cashier users

### Point of Sale

Cashiers can select medicines and quantities when processing customer purchases.

The system calculates the total amount and records the completed sale.

### Stock Management

When a sale is completed, the quantity sold is automatically deducted from the medicine stock.

The system also prevents a sale from being completed when sufficient stock is not available.

### Receipt Generation

After a successful sale, the system generates a receipt containing information such as:

- Sale ID
- Date and time
- Cashier
- Medicines purchased
- Quantities
- Prices
- Total amount

Receipts can be saved and printed.

---

## 6. Reports

The system provides the following reports:

### Sales Report

Displays recorded sales and relevant sale information.

### Item-Wise Sales Report

Displays medicines sold, quantities sold and revenue generated.

### Low Stock Report

Identifies medicines where the available quantity is at or below the reorder level.

### Expiry Report

Identifies medicines that have expired or are approaching their expiry date.

---

## 7. Database

The system uses a MySQL database named:

`pims_database`

The main tables are:

- users
- suppliers
- medicines
- sales
- sale_items

Relationships between the tables are implemented using primary keys and foreign keys.

---

## 8. Default Login Credentials

### Administrator

Username:

`admin`

Password:

`admin123`

### Cashier

Username:

`cashier`

Password:

`cashier123`

> These credentials are provided for demonstration and testing purposes.

---

## 9. Project Structure

```text
PIMS
│
├── src
│   ├── database
│   ├── models
│   ├── dao
│   ├── gui
│   └── utils
│
├── screenshots
│
├── README.md
│
└── .gitignore