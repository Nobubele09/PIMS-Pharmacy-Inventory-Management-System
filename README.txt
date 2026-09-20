============================================================
PHARMACY INVENTORY MANAGEMENT SYSTEM (PIMS)
HEALTHFIRST PHARMACY
PROGRAMMING 732
============================================================

1. SYSTEM OVERVIEW
------------------

The Pharmacy Inventory Management System (PIMS) is a Java-based
desktop application developed for HealthFirst Pharmacy.

The system is designed to assist pharmacy staff with:

- User authentication
- Role-based access control
- Medicine inventory management
- Supplier management
- Cashier point-of-sale operations
- Sales and billing
- Receipt generation
- Stock monitoring
- Expiry monitoring
- Sales reporting

The system uses Java Swing for the graphical user interface,
JDBC for database connectivity, and MySQL for data storage.


2. TECHNOLOGIES USED
--------------------

Programming Language:
Java

GUI Technology:
Java Swing / AWT

Database:
MySQL

Database Connectivity:
JDBC

Development Environment:
Visual Studio Code

Version Control:
Git and GitHub


3. SYSTEM REQUIREMENTS
----------------------

The following software is required to run the system:

- Java Development Kit (JDK)
- MySQL Server
- MySQL Workbench (recommended for database setup)
- MySQL Connector/J JDBC driver

The computer should have sufficient storage and memory to run
Java and MySQL.


4. DATABASE SETUP
-----------------

Step 1:
Open MySQL Workbench.

Step 2:
Open the supplied database.sql file.

Step 3:
Execute the SQL script.

The script creates the PIMS database and the required tables:

- users
- suppliers
- medicines
- sales
- sale_items

The script also provides initial sample data.


5. LOGIN
--------

Start the PIMS application.

The Login Screen requires:

- Username
- Password

The system checks the supplied credentials against the users
stored in the database.

After successful authentication, the system automatically opens
the dashboard associated with the user's role.


6. DEFAULT LOGIN CREDENTIALS
----------------------------

ADMIN ACCOUNT

Username: admin
Password: admin123
Role: Admin


CASHIER ACCOUNT

Username: cashier
Password: cashier123
Role: Cashier


IMPORTANT:
These are the default demonstration credentials supplied with
the system.


7. ADMINISTRATOR FUNCTIONS
-------------------------

The Administrator has access to the management and reporting
functions of the system.

The Admin Dashboard provides access to:

- Medicine Management
- Supplier Management
- User Management
- Sales Report
- Item-Wise Report
- Low Stock Report
- Expiry Report


8. MEDICINE MANAGEMENT
----------------------

The Medicine Management section allows the administrator to
manage medicine inventory.

The administrator can:

- Add medicines
- View medicines
- Search for medicines
- Update medicine information
- Delete medicines
- Select the appropriate supplier
- Record medicine price
- Record stock quantity
- Record reorder level
- Record expiry date

Medicine information includes:

- Medicine ID
- Name
- Company
- Medicine Type
- Price
- Quantity in Stock
- Reorder Level
- Expiry Date
- Supplier


9. SUPPLIER MANAGEMENT
----------------------

The Supplier Management section allows the administrator to
maintain supplier information.

The administrator can:

- Add suppliers
- View suppliers
- Search suppliers
- Update suppliers
- Delete suppliers

Supplier information includes:

- Supplier ID
- Supplier Name
- Contact Person
- Phone
- Email
- Address


10. USER MANAGEMENT
-------------------

The User Management section allows the administrator to manage
system users.

The administrator can:

- Add users
- View users
- Search users
- Update users
- Delete users
- Manage Admin and Cashier accounts

Each user is assigned a role.

Available roles:

- Admin
- Cashier


11. CASHIER FUNCTIONS
---------------------

Cashiers use the Cashier Dashboard to perform sales.

The cashier can:

- Search medicines
- Check available stock
- Add medicines to the cart
- Specify quantities
- View cart items
- Calculate the sale total
- Complete a sale
- Generate a bill
- Save a receipt
- Print a receipt

Cashiers cannot access the administrator's medicine,
supplier, or user management functions.


12. POINT-OF-SALE PROCESS
-------------------------

To process a sale:

Step 1:
Log in using the Cashier account.

Step 2:
Search for the required medicine.

Step 3:
Select the medicine.

Step 4:
Enter the required quantity.

Step 5:
Add the medicine to the cart.

Step 6:
Repeat the process if additional medicines are required.

Step 7:
Review the items in the cart.

Step 8:
Click the button to complete the sale.

Step 9:
The system records the sale in the database.

Step 10:
The system automatically updates the medicine stock.

Step 11:
A receipt/bill is generated.


13. RECEIPT MANAGEMENT
---------------------

After completing a sale, the system displays the generated
receipt.

The receipt contains information such as:

- Sale ID
- Date and time
- Cashier
- Medicine name
- Quantity sold
- Price
- Subtotal
- Total amount

The receipt can be:

- Saved as a text file
- Printed using the system's print function


14. SALES REPORT
----------------

The Sales Report displays recorded sales transactions.

The report provides information including:

- Sale ID
- Sale Date
- Total Amount
- Cashier

The report also provides the total sales amount.


15. ITEM-WISE REPORT
--------------------

The Item-Wise Report provides a summary of medicines sold.

The report includes:

- Medicine
- Quantity Sold
- Revenue

This report helps the pharmacy review sales activity for
individual medicines.


16. LOW STOCK REPORT
--------------------

The Low Stock Report identifies medicines whose available
quantity is at or below their configured reorder level.

This allows the administrator to identify medicines that may
need to be reordered.


17. EXPIRY REPORT
-----------------

The Expiry Report monitors medicine expiry dates.

The system identifies medicines that are:

- Already expired
- Expiring within 30 days
- Expiring soon

The report helps the pharmacy monitor medicines approaching
their expiry dates.


18. ROLE-BASED ACCESS
---------------------

The system implements role-based access control.

ADMIN:
The administrator has access to inventory management,
supplier management, user management and reports.

CASHIER:
The cashier has access to point-of-sale and billing functions.

The cashier does not have access to administrator management
functions.


19. VALIDATION AND ERROR HANDLING
---------------------------------

The system performs validation to reduce invalid data entry.

Examples include:

- Required fields must be completed.
- Invalid quantities are rejected.
- Invalid prices are rejected.
- Invalid login credentials are rejected.
- A cashier cannot sell more medicine than the available stock.
- Database errors are handled using error messages.
- Database transactions are used when processing sales.


20. LOGGING OUT
---------------

To exit the current user session:

Step 1:
Click the Logout button.

Step 2:
The current dashboard closes.

Step 3:
The Login Screen is displayed again.

A different user can then log into the system.


21. TROUBLESHOOTING
-------------------

PROBLEM:
The application cannot connect to MySQL.

SOLUTION:
1. Confirm that MySQL Server is running.
2. Confirm that the PIMS database exists.
3. Confirm the database username and connection settings.
4. Confirm that the JDBC driver is available.


PROBLEM:
Login fails.

SOLUTION:
1. Check the username.
2. Check the password.
3. Confirm that the user exists in the users table.


PROBLEM:
Medicines cannot be added.

SOLUTION:
1. Confirm that all required fields are completed.
2. Confirm that a supplier has been selected.
3. Check that the entered values are valid.


PROBLEM:
A sale cannot be completed.

SOLUTION:
1. Check that the cart contains medicine.
2. Check the requested quantity.
3. Confirm that sufficient stock is available.
4. Confirm that the database connection is active.


22. PROJECT STRUCTURE
---------------------

PIMS/
|
|-- src/
|   |
|   |-- database/
|   |-- models/
|   |-- dao/
|   |-- gui/
|   |-- utils/
|
|-- screenshots/
|
|-- database.sql
|
|-- README.txt
|
|-- README.md
|
|-- .gitignore


23. SECURITY NOTE
-----------------

Database credentials used by the application should be kept
private and should not be published in public repositories.

The supplied source code should be configured with the correct
database connection details before running the application.


24. GITHUB VERSION CONTROL
--------------------------

The project is maintained using Git and GitHub.

The GitHub repository contains the source code and project
development history.

Multiple commits were made during development to demonstrate
the progression of the system.


25. TESTING
-----------

The following major system functions were tested:

- User login
- Role-based access
- Admin dashboard
- Medicine management
- Supplier management
- User management
- Cashier POS
- Adding items to cart
- Stock validation
- Sales processing
- Receipt generation
- Receipt saving
- Receipt printing
- Sales Report
- Item-Wise Report
- Low Stock Report
- Expiry Report


26. CONCLUSION
--------------

The Pharmacy Inventory Management System provides HealthFirst
Pharmacy with a computerized solution for managing medicines,
suppliers, users, sales and inventory information.

The system combines a Java Swing graphical interface with a
MySQL database through JDBC.

Role-based access ensures that administrators and cashiers
receive access to the functions appropriate to their roles.

The reporting functions provide useful information about sales,
medicine quantities and expiry dates, while the point-of-sale
function supports cashier billing and stock updates.


============================================================
END OF USER MANUAL
============================================================