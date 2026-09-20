package gui;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LowStockReportPanel extends JPanel {

    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public LowStockReportPanel() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel =
                new JLabel("Low Stock Report");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        add(
                titleLabel,
                BorderLayout.NORTH
        );


        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "Medicine ID",
                "Medicine Name",
                "Company",
                "Current Stock",
                "Reorder Level",
                "Status"
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


        stockTable =
                new JTable(tableModel);

        stockTable.setRowHeight(25);


        JScrollPane scrollPane =
                new JScrollPane(stockTable);

        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =========================
        // BOTTOM PANEL
        // =========================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );


        countLabel =
                new JLabel(
                        "Low Stock Items: 0"
                );

        countLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        bottomPanel.add(
                countLabel,
                BorderLayout.WEST
        );


        JButton refreshButton =
                new JButton(
                        "Refresh Report"
                );


        bottomPanel.add(
                refreshButton,
                BorderLayout.EAST
        );


        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // REFRESH ACTION
        // =========================

        refreshButton.addActionListener(
                e -> loadLowStockReport()
        );


        // Load report automatically
        loadLowStockReport();
    }


    // =========================
    // LOAD LOW STOCK REPORT
    // =========================

    private void loadLowStockReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT medicine_id, " +
                "name, " +
                "company, " +
                "quantity_in_stock, " +
                "reorder_level " +
                "FROM medicines " +
                "WHERE quantity_in_stock <= reorder_level " +
                "ORDER BY quantity_in_stock ASC";


        int lowStockCount = 0;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {


            while (resultSet.next()) {

                int medicineId =
                        resultSet.getInt(
                                "medicine_id"
                        );

                String medicineName =
                        resultSet.getString(
                                "name"
                        );

                String company =
                        resultSet.getString(
                                "company"
                        );

                int currentStock =
                        resultSet.getInt(
                                "quantity_in_stock"
                        );

                int reorderLevel =
                        resultSet.getInt(
                                "reorder_level"
                        );


                String status =
                        "REORDER REQUIRED";


                tableModel.addRow(
                        new Object[]{
                                medicineId,
                                medicineName,
                                company,
                                currentStock,
                                reorderLevel,
                                status
                        }
                );


                lowStockCount++;
            }


            countLabel.setText(
                    "Low Stock Items: "
                            + lowStockCount
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load Low Stock Report.\n\n"
                            + ex.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}

