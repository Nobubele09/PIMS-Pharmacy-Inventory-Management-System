package gui;

import dao.MedicineDAO;
import dao.SaleDAO;
import models.Medicine;
import models.SaleItem;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CashierPOSPanel extends JPanel {

    private User loggedInUser;

    private MedicineDAO medicineDAO;
    private SaleDAO saleDAO;

    private JTextField searchField;
    private JComboBox<Medicine> medicineComboBox;
    private JTextField quantityField;

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JLabel totalLabel;

    private List<SaleItem> cartItems;

    public CashierPOSPanel(User user) {

        this.loggedInUser = user;

        medicineDAO = new MedicineDAO();
        saleDAO = new SaleDAO();

        cartItems = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));

        createTopPanel();
        createCartPanel();
        createBottomPanel();

        loadMedicines();
    }

    // ============================================================
    // TOP PANEL
    // ============================================================

    private void createTopPanel() {

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        topPanel.setBackground(Color.WHITE);

        topPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Add Medicine to Sale"
                )
        );

        // Search field

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");

        JPanel searchPanel =
                new JPanel(new BorderLayout(5, 5));

        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(
                new JLabel("Search:"),
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchButton,
                BorderLayout.EAST
        );

        topPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        // Medicine selection

        JPanel selectionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        selectionPanel.setBackground(Color.WHITE);

        selectionPanel.add(
                new JLabel("Medicine:")
        );

        medicineComboBox = new JComboBox<>();

        medicineComboBox.setPreferredSize(
                new Dimension(300, 30)
        );

        selectionPanel.add(
                medicineComboBox
        );

        selectionPanel.add(
                new JLabel("Quantity:")
        );

        quantityField =
                new JTextField("1");

        quantityField.setPreferredSize(
                new Dimension(80, 30)
        );

        selectionPanel.add(
                quantityField
        );

        JButton addButton =
                new JButton("Add to Cart");

        selectionPanel.add(
                addButton
        );

        topPanel.add(
                selectionPanel,
                BorderLayout.CENTER
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // Button actions

        searchButton.addActionListener(
                e -> searchMedicines()
        );

        searchField.addActionListener(
                e -> searchMedicines()
        );

        addButton.addActionListener(
                e -> addToCart()
        );
    }

    // ============================================================
    // CART TABLE
    // ============================================================

    private void createCartPanel() {

        String[] columns = {
                "Medicine ID",
                "Medicine",
                "Quantity",
                "Price",
                "Subtotal"
        };

        cartModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        cartTable =
                new JTable(cartModel);

        cartTable.setRowHeight(28);

        JScrollPane scrollPane =
                new JScrollPane(cartTable);

        JPanel cartPanel =
                new JPanel(
                        new BorderLayout()
                );

        cartPanel.setBackground(Color.WHITE);

        cartPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Current Sale"
                )
        );

        cartPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                cartPanel,
                BorderLayout.CENTER
        );
    }

    // ============================================================
    // BOTTOM PANEL
    // ============================================================

    private void createBottomPanel() {

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setBackground(Color.WHITE);

        totalLabel =
                new JLabel(
                        "Total: R0.00"
                );

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        bottomPanel.add(
                totalLabel,
                BorderLayout.WEST
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.setBackground(Color.WHITE);

        JButton removeButton =
                new JButton("Remove Item");

        JButton clearButton =
                new JButton("Clear Sale");

        JButton checkoutButton =
                new JButton("Complete Sale");

        buttons.add(removeButton);
        buttons.add(clearButton);
        buttons.add(checkoutButton);

        bottomPanel.add(
                buttons,
                BorderLayout.EAST
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        removeButton.addActionListener(
                e -> removeSelectedItem()
        );

        clearButton.addActionListener(
                e -> clearSale()
        );

        checkoutButton.addActionListener(
                e -> completeSale()
        );
    }

    // ============================================================
    // LOAD MEDICINES
    // ============================================================

    private void loadMedicines() {

        medicineComboBox.removeAllItems();

        List<Medicine> medicines =
                medicineDAO.getAllMedicines();

        for (Medicine medicine : medicines) {

            if (medicine.getQuantityInStock() > 0) {

                medicineComboBox.addItem(
                        medicine
                );
            }
        }
    }

    // ============================================================
    // SEARCH MEDICINES
    // ============================================================

    private void searchMedicines() {

        String searchTerm =
                searchField.getText().trim();

        medicineComboBox.removeAllItems();

        if (searchTerm.isEmpty()) {

            loadMedicines();

            return;
        }

        List<Medicine> medicines =
                medicineDAO.searchMedicines(
                        searchTerm
                );

        for (Medicine medicine : medicines) {

            if (medicine.getQuantityInStock() > 0) {

                medicineComboBox.addItem(
                        medicine
                );
            }
        }

        if (medicineComboBox.getItemCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No medicines found."
            );
        }
    }

    // ============================================================
    // ADD MEDICINE TO CART
    // ============================================================

    private void addToCart() {

        Medicine medicine =
                (Medicine) medicineComboBox
                        .getSelectedItem();

        if (medicine == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine."
            );

            return;
        }

        int quantity;

        try {

            quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity."
            );

            return;
        }

        if (quantity <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Quantity must be greater than zero."
            );

            return;
        }

        if (quantity >
                medicine.getQuantityInStock()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Insufficient stock.\n\n"
                            + "Available stock: "
                            + medicine.getQuantityInStock()
            );

            return;
        }

        // Check if medicine already exists
        // in the cart

        for (SaleItem item : cartItems) {

            if (item.getMedicineId()
                    == medicine.getMedicineId()) {

                int newQuantity =
                        item.getQuantitySold()
                                + quantity;

                if (newQuantity >
                        medicine.getQuantityInStock()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "The quantity exceeds available stock."
                    );

                    return;
                }

                item.setQuantitySold(
                        newQuantity
                );

                refreshCart();

                return;
            }
        }

        SaleItem item =
                new SaleItem(
                        medicine.getMedicineId(),
                        medicine.getName(),
                        quantity,
                        medicine.getPrice()
                );

        cartItems.add(item);

        refreshCart();

        quantityField.setText("1");
    }

    // ============================================================
    // REFRESH CART
    // ============================================================

    private void refreshCart() {

        cartModel.setRowCount(0);

        double total = 0;

        for (SaleItem item : cartItems) {

            double subtotal =
                    item.getSubtotal();

            Object[] row = {

                    item.getMedicineId(),

                    item.getMedicineName(),

                    item.getQuantitySold(),

                    String.format(
                            "R%.2f",
                            item.getPriceAtSale()
                    ),

                    String.format(
                            "R%.2f",
                            subtotal
                    )
            };

            cartModel.addRow(row);

            total += subtotal;
        }

        totalLabel.setText(
                String.format(
                        "Total: R%.2f",
                        total
                )
        );
    }

    // ============================================================
    // REMOVE ITEM
    // ============================================================

    private void removeSelectedItem() {

        int selectedRow =
                cartTable.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item to remove."
            );

            return;
        }

        cartItems.remove(
                selectedRow
        );

        refreshCart();
    }

    // ============================================================
    // CLEAR SALE
    // ============================================================

    private void clearSale() {

        if (cartItems.isEmpty()) {

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Clear the current sale?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation ==
                JOptionPane.YES_OPTION) {

            cartItems.clear();

            refreshCart();
        }
    }

    // ============================================================
    // COMPLETE SALE
    // ============================================================

    private void completeSale() {

        if (cartItems.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please add at least one medicine."
            );

            return;
        }

        double total = 0;

        for (SaleItem item : cartItems) {

            total += item.getSubtotal();
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,

                        String.format(
                                "Complete sale for R%.2f?",
                                total
                        ),

                        "Confirm Sale",

                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        /*
         * Make a copy of the items before clearing
         * the cart. The copy will be used to create
         * the receipt.
         */

        List<SaleItem> completedItems =
                new ArrayList<>(
                        cartItems
                );

        /*
         * createSale() now returns:
         *
         * Sale ID if successful
         * -1 if unsuccessful
         */

        int saleId =
                saleDAO.createSale(
                        total,
                        loggedInUser.getUserId(),
                        completedItems
                );

        if (saleId > 0) {

            /*
             * Clear current sale.
             */

            cartItems.clear();

            refreshCart();

            loadMedicines();

            searchField.setText("");

            /*
             * Automatically open receipt.
             */

            showReceipt(
                    saleId,
                    completedItems,
                    total
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,

                    "The sale could not be completed.\n\n"
                            + "Please check the stock and try again.",

                    "Sale Error",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // GENERATE RECEIPT
    // ============================================================

    private String generateReceipt(
            int saleId,
            List<SaleItem> items,
            double total
    ) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm:ss"
                );

        String dateTime =
                LocalDateTime.now()
                        .format(formatter);

        StringBuilder receipt =
                new StringBuilder();

        receipt.append(
                "========================================\n"
        );

        receipt.append(
                "          HEALTHFIRST PHARMACY\n"
        );

        receipt.append(
                "            CUSTOMER RECEIPT\n"
        );

        receipt.append(
                "========================================\n"
        );

        receipt.append(
                "Sale ID: "
        );

        receipt.append(saleId);

        receipt.append("\n");

        receipt.append(
                "Date/Time: "
        );

        receipt.append(dateTime);

        receipt.append("\n");

        receipt.append(
                "Cashier: "
        );

        receipt.append(
                loggedInUser.getFullName()
        );

        receipt.append("\n");

        receipt.append(
                "----------------------------------------\n"
        );

        receipt.append(
                String.format(
                        "%-22s %5s %11s%n",
                        "Medicine",
                        "Qty",
                        "Amount"
                )
        );

        receipt.append(
                "----------------------------------------\n"
        );

        for (SaleItem item : items) {

            String medicineName =
                    item.getMedicineName();

            /*
             * Keep long medicine names
             * within the receipt width.
             */

            if (medicineName.length() > 22) {

                medicineName =
                        medicineName.substring(
                                0,
                                22
                        );
            }

            receipt.append(
                    String.format(
                            "%-22s %5d R%10.2f%n",
                            medicineName,
                            item.getQuantitySold(),
                            item.getSubtotal()
                    )
            );
        }

        receipt.append(
                "----------------------------------------\n"
        );

        receipt.append(
                String.format(
                        "%-28s R%10.2f%n",
                        "TOTAL:",
                        total
                )
        );

        receipt.append(
                "========================================\n"
        );

        receipt.append(
                "        Thank you for your purchase!\n"
        );

        receipt.append(
                "========================================\n"
        );

        return receipt.toString();
    }

    // ============================================================
    // SHOW RECEIPT WINDOW
    // ============================================================

    private void showReceipt(
            int saleId,
            List<SaleItem> items,
            double total
    ) {

        String receiptText =
                generateReceipt(
                        saleId,
                        items,
                        total
                );

        JDialog receiptDialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Sale Receipt",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        receiptDialog.setSize(
                550,
                650
        );

        receiptDialog.setLocationRelativeTo(this);

        receiptDialog.setLayout(
                new BorderLayout(10, 10)
        );

        // Receipt text area

        JTextArea receiptArea =
                new JTextArea();

        receiptArea.setText(
                receiptText
        );

        receiptArea.setEditable(false);

        receiptArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        14
                )
        );

        receiptArea.setMargin(
                new Insets(
                        15,
                        15,
                        15,
                        15
                )
        );

        JScrollPane receiptScroll =
                new JScrollPane(
                        receiptArea
                );

        receiptDialog.add(
                receiptScroll,
                BorderLayout.CENTER
        );

        // Buttons

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        JButton saveButton =
                new JButton(
                        "Save Receipt"
                );

        JButton printButton =
                new JButton(
                        "Print Receipt"
                );

        JButton closeButton =
                new JButton(
                        "Close"
                );

        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        receiptDialog.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // Save button

        saveButton.addActionListener(
                e -> saveReceipt(
                        receiptText,
                        saleId
                )
        );

        // Print button

        printButton.addActionListener(
                e -> printReceipt(
                        receiptArea
                )
        );

        // Close button

        closeButton.addActionListener(
                e -> receiptDialog.dispose()
        );

        receiptDialog.setVisible(true);
    }

    // ============================================================
    // SAVE RECEIPT
    // ============================================================

    private void saveReceipt(
            String receiptText,
            int saleId
    ) {

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Save Receipt"
        );

        fileChooser.setSelectedFile(
                new File(
                        "Receipt_Sale_" +
                                saleId +
                                ".txt"
                )
        );

        int result =
                fileChooser.showSaveDialog(
                        this
                );

        if (result !=
                JFileChooser.APPROVE_OPTION) {

            return;
        }

        File file =
                fileChooser.getSelectedFile();

        /*
         * Add .txt automatically if the
         * user did not enter it.
         */

        if (!file.getName()
                .toLowerCase()
                .endsWith(".txt")) {

            file = new File(
                    file.getAbsolutePath()
                            + ".txt"
            );
        }

        try (
                FileWriter writer =
                        new FileWriter(file)
        ) {

            writer.write(
                    receiptText
            );

            JOptionPane.showMessageDialog(
                    this,

                    "Receipt saved successfully!\n\n"
                            + file.getAbsolutePath(),

                    "Receipt Saved",

                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,

                    "Unable to save the receipt.\n\n"
                            + e.getMessage(),

                    "Save Error",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // PRINT RECEIPT
    // ============================================================

    private void printReceipt(
            JTextArea receiptArea
    ) {

        try {

            boolean printed =
                    receiptArea.print();

            if (printed) {

                JOptionPane.showMessageDialog(
                        this,

                        "Receipt sent to the printer.",

                        "Print Receipt",

                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException e) {

            JOptionPane.showMessageDialog(
                    this,

                    "Unable to print the receipt.\n\n"
                            + e.getMessage(),

                    "Print Error",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}