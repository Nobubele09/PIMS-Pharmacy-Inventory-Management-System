package gui;

import dao.MedicineDAO;
import dao.SupplierDAO;
import models.Medicine;
import models.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MedicineManagementPanel extends JPanel {

    private MedicineDAO medicineDAO;
    private SupplierDAO supplierDAO;

    private JTable medicineTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JTextField nameField;
    private JTextField companyField;
    private JComboBox<String> typeComboBox;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField reorderLevelField;
    private JTextField expiryDateField;

    // Supplier is now selected from a dropdown
    private JComboBox<Supplier> supplierComboBox;

    private int selectedMedicineId = -1;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public MedicineManagementPanel() {

        medicineDAO = new MedicineDAO();
        supplierDAO = new SupplierDAO();

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        createInterface();

        loadSuppliers();

        loadMedicines();
    }

    // ============================================================
    // CREATE MAIN INTERFACE
    // ============================================================

    private void createInterface() {

        add(
                createTopPanel(),
                BorderLayout.NORTH
        );

        add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        add(
                createFormPanel(),
                BorderLayout.SOUTH
        );
    }

    // ============================================================
    // TOP SEARCH PANEL
    // ============================================================

    private JPanel createTopPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Medicine Management"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.WEST
        );

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        searchField =
                new JTextField(20);

        JButton searchButton =
                new JButton("Search");

        JButton refreshButton =
                new JButton("Refresh");

        searchPanel.add(
                new JLabel("Search:")
        );

        searchPanel.add(
                searchField
        );

        searchPanel.add(
                searchButton
        );

        searchPanel.add(
                refreshButton
        );

        panel.add(
                searchPanel,
                BorderLayout.EAST
        );

        // Search button
        searchButton.addActionListener(e -> {
            searchMedicines();
        });

        // Refresh button
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadMedicines();
            loadSuppliers();
        });

        // Press Enter in search field
        searchField.addActionListener(e -> {
            searchMedicines();
        });

        return panel;
    }

    // ============================================================
    // MEDICINE TABLE
    // ============================================================

    private JScrollPane createTablePanel() {

        String[] columns = {

                "ID",
                "Name",
                "Company",
                "Type",
                "Price",
                "Stock",
                "Reorder Level",
                "Expiry Date",
                "Supplier ID"
        };

        tableModel =
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

        medicineTable =
                new JTable(
                        tableModel
                );

        medicineTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        medicineTable.setRowHeight(25);

        medicineTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedMedicine();
                    }

                });

        return new JScrollPane(
                medicineTable
        );
    }

    // ============================================================
    // MEDICINE FORM
    // ============================================================

    private JPanel createFormPanel() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                4,
                                10,
                                8
                        )
                );

        // --------------------------------------------------------
        // CREATE INPUT FIELDS
        // --------------------------------------------------------

        nameField =
                new JTextField();

        companyField =
                new JTextField();

        typeComboBox =
                new JComboBox<>(
                        new String[]{

                                "Tablet",
                                "Capsule",
                                "Syrup",
                                "Injection",
                                "Cream",
                                "Ointment",
                                "Drops",
                                "Other"
                        }
                );

        priceField =
                new JTextField();

        quantityField =
                new JTextField();

        reorderLevelField =
                new JTextField();

        expiryDateField =
                new JTextField();

        // Supplier dropdown
        supplierComboBox =
                new JComboBox<>();

        // --------------------------------------------------------
        // ADD LABELS AND FIELDS
        // --------------------------------------------------------

        formPanel.add(
                new JLabel(
                        "Medicine Name:"
                )
        );

        formPanel.add(
                nameField
        );

        formPanel.add(
                new JLabel(
                        "Company:"
                )
        );

        formPanel.add(
                companyField
        );

        formPanel.add(
                new JLabel(
                        "Medicine Type:"
                )
        );

        formPanel.add(
                typeComboBox
        );

        formPanel.add(
                new JLabel(
                        "Price:"
                )
        );

        formPanel.add(
                priceField
        );

        formPanel.add(
                new JLabel(
                        "Quantity:"
                )
        );

        formPanel.add(
                quantityField
        );

        formPanel.add(
                new JLabel(
                        "Reorder Level:"
                )
        );

        formPanel.add(
                reorderLevelField
        );

        formPanel.add(
                new JLabel(
                        "Expiry Date:"
                )
        );

        formPanel.add(
                expiryDateField
        );

        formPanel.add(
                new JLabel(
                        "Supplier:"
                )
        );

        formPanel.add(
                supplierComboBox
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // ========================================================
        // BUTTON PANEL
        // ========================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        JButton addButton =
                new JButton(
                        "Add Medicine"
                );

        JButton updateButton =
                new JButton(
                        "Update Medicine"
                );

        JButton deleteButton =
                new JButton(
                        "Delete Medicine"
                );

        JButton clearButton =
                new JButton(
                        "Clear"
                );

        buttonPanel.add(
                addButton
        );

        buttonPanel.add(
                updateButton
        );

        buttonPanel.add(
                deleteButton
        );

        buttonPanel.add(
                clearButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // ========================================================
        // BUTTON ACTIONS
        // ========================================================

        addButton.addActionListener(e -> {
            addMedicine();
        });

        updateButton.addActionListener(e -> {
            updateMedicine();
        });

        deleteButton.addActionListener(e -> {
            deleteMedicine();
        });

        clearButton.addActionListener(e -> {
            clearForm();
        });

        return mainPanel;
    }

    // ============================================================
    // LOAD SUPPLIERS
    // ============================================================

    private void loadSuppliers() {

        if (supplierComboBox == null) {
            return;
        }

        supplierComboBox.removeAllItems();

        List<Supplier> suppliers =
                supplierDAO.getAllSuppliers();

        for (Supplier supplier : suppliers) {

            supplierComboBox.addItem(
                    supplier
            );
        }
    }

    // ============================================================
    // LOAD ALL MEDICINES
    // ============================================================

    private void loadMedicines() {

        tableModel.setRowCount(0);

        List<Medicine> medicines =
                medicineDAO.getAllMedicines();

        for (Medicine medicine : medicines) {

            addMedicineToTable(
                    medicine
            );
        }
    }

    // ============================================================
    // ADD MEDICINE TO TABLE
    // ============================================================

    private void addMedicineToTable(
            Medicine medicine
    ) {

        tableModel.addRow(
                new Object[]{

                        medicine.getMedicineId(),

                        medicine.getName(),

                        medicine.getCompany(),

                        medicine.getMedicineType(),

                        String.format(
                                "%.2f",
                                medicine.getPrice()
                        ),

                        medicine.getQuantityInStock(),

                        medicine.getReorderLevel(),

                        medicine.getExpiryDate(),

                        medicine.getSupplierId()
                }
        );
    }

    // ============================================================
    // SEARCH MEDICINES
    // ============================================================

    private void searchMedicines() {

        String searchTerm =
                searchField.getText().trim();

        if (searchTerm.isEmpty()) {

            loadMedicines();

            return;
        }

        tableModel.setRowCount(0);

        List<Medicine> medicines =
                medicineDAO.searchMedicines(
                        searchTerm
                );

        for (Medicine medicine : medicines) {

            addMedicineToTable(
                    medicine
            );
        }
    }

    // ============================================================
    // LOAD SELECTED MEDICINE
    // ============================================================

    private void loadSelectedMedicine() {

        int selectedRow =
                medicineTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedMedicineId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        nameField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString()
        );

        companyField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                2
                        )
                        .toString()
        );

        typeComboBox.setSelectedItem(
                tableModel
                        .getValueAt(
                                selectedRow,
                                3
                        )
                        .toString()
        );

        priceField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                4
                        )
                        .toString()
        );

        quantityField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                5
                        )
                        .toString()
        );

        reorderLevelField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                6
                        )
                        .toString()
        );

        expiryDateField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                7
                        )
                        .toString()
        );

        // --------------------------------------------------------
        // SELECT CORRECT SUPPLIER
        // --------------------------------------------------------

        int supplierId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        8
                                )
                                .toString()
                );

        for (int i = 0;
             i < supplierComboBox.getItemCount();
             i++) {

            Supplier supplier =
                    supplierComboBox.getItemAt(i);

            if (supplier.getSupplierId()
                    == supplierId) {

                supplierComboBox.setSelectedIndex(i);

                break;
            }
        }
    }

    // ============================================================
    // ADD MEDICINE
    // ============================================================

    private void addMedicine() {

        try {

            Medicine medicine =
                    getMedicineFromForm();

            boolean success =
                    medicineDAO.addMedicine(
                            medicine
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();

                loadMedicines();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine could not be added.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Information",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ============================================================
    // UPDATE MEDICINE
    // ============================================================

    private void updateMedicine() {

        if (selectedMedicineId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Medicine medicine =
                    getMedicineFromForm();

            medicine.setMedicineId(
                    selectedMedicineId
            );

            boolean success =
                    medicineDAO.updateMedicine(
                            medicine
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();

                loadMedicines();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine could not be updated.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Information",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ============================================================
    // DELETE MEDICINE
    // ============================================================

    private void deleteMedicine() {

        if (selectedMedicineId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this medicine?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        boolean success =
                medicineDAO.deleteMedicine(
                        selectedMedicineId
                );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

            loadMedicines();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine could not be deleted.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // GET MEDICINE DATA FROM FORM
    // ============================================================

    private Medicine getMedicineFromForm()
            throws Exception {

        String name =
                nameField.getText().trim();

        String company =
                companyField.getText().trim();

        String type =
                typeComboBox
                        .getSelectedItem()
                        .toString();

        String priceText =
                priceField.getText().trim();

        String quantityText =
                quantityField.getText().trim();

        String reorderText =
                reorderLevelField.getText().trim();

        String expiryText =
                expiryDateField.getText().trim();

        // --------------------------------------------------------
        // CHECK EMPTY FIELDS
        // --------------------------------------------------------

        if (name.isEmpty()
                || company.isEmpty()
                || priceText.isEmpty()
                || quantityText.isEmpty()
                || reorderText.isEmpty()
                || expiryText.isEmpty()) {

            throw new Exception(
                    "Please complete all medicine fields."
            );
        }

        // --------------------------------------------------------
        // CHECK SUPPLIER
        // --------------------------------------------------------

        Supplier selectedSupplier =
                (Supplier) supplierComboBox
                        .getSelectedItem();

        if (selectedSupplier == null) {

            throw new Exception(
                    "Please select a supplier."
            );
        }

        int supplierId =
                selectedSupplier.getSupplierId();

        double price;

        int quantity;

        int reorderLevel;

        LocalDate expiryDate;

        // --------------------------------------------------------
        // PRICE VALIDATION
        // --------------------------------------------------------

        try {

            price =
                    Double.parseDouble(
                            priceText
                    );

        } catch (NumberFormatException e) {

            throw new Exception(
                    "Price must be a valid number."
            );
        }

        // --------------------------------------------------------
        // QUANTITY VALIDATION
        // --------------------------------------------------------

        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            throw new Exception(
                    "Quantity must be a whole number."
            );
        }

        // --------------------------------------------------------
        // REORDER LEVEL VALIDATION
        // --------------------------------------------------------

        try {

            reorderLevel =
                    Integer.parseInt(
                            reorderText
                    );

        } catch (NumberFormatException e) {

            throw new Exception(
                    "Reorder level must be a whole number."
            );
        }

        // --------------------------------------------------------
        // EXPIRY DATE VALIDATION
        // --------------------------------------------------------

        try {

            expiryDate =
                    LocalDate.parse(
                            expiryText,
                            dateFormatter
                    );

        } catch (DateTimeParseException e) {

            throw new Exception(
                    "Expiry date must use YYYY-MM-DD format."
            );
        }

        // --------------------------------------------------------
        // NUMBER VALIDATION
        // --------------------------------------------------------

        if (price < 0) {

            throw new Exception(
                    "Price cannot be negative."
            );
        }

        if (quantity < 0) {

            throw new Exception(
                    "Quantity cannot be negative."
            );
        }

        if (reorderLevel < 0) {

            throw new Exception(
                    "Reorder level cannot be negative."
            );
        }

        // --------------------------------------------------------
        // CREATE MEDICINE OBJECT
        // --------------------------------------------------------

        return new Medicine(
                name,
                company,
                type,
                price,
                quantity,
                reorderLevel,
                expiryDate,
                supplierId
        );
    }

    // ============================================================
    // CLEAR FORM
    // ============================================================

    private void clearForm() {

        selectedMedicineId = -1;

        nameField.setText("");

        companyField.setText("");

        typeComboBox.setSelectedIndex(0);

        priceField.setText("");

        quantityField.setText("");

        reorderLevelField.setText("");

        expiryDateField.setText("");

        if (supplierComboBox.getItemCount() > 0) {

            supplierComboBox.setSelectedIndex(0);
        }

        medicineTable.clearSelection();

        nameField.requestFocus();
    }
}