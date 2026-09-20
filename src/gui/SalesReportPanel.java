package gui;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

public class SalesReportPanel extends JPanel {

    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JLabel totalSalesLabel;
    private JLabel totalRevenueLabel;

    private DecimalFormat decimalFormat =
            new DecimalFormat("R #,##0.00");

    public SalesReportPanel() {

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
                new JLabel("Sales Report");

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
                "Sale ID",
                "Sale Date",
                "Total Amount",
                "Cashier"
        };

        tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        salesTable =
                new JTable(tableModel);

        salesTable.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(salesTable);

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

        JPanel summaryPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        totalSalesLabel =
                new JLabel(
                        "Total Sales: 0"
                );

        totalRevenueLabel =
                new JLabel(
                        "Total Revenue: R 0.00"
                );

        totalSalesLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        totalRevenueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        summaryPanel.add(
                totalSalesLabel
        );

        summaryPanel.add(
                Box.createHorizontalStrut(30)
        );

        summaryPanel.add(
                totalRevenueLabel
        );

        bottomPanel.add(
                summaryPanel,
                BorderLayout.WEST
        );


        // =========================
        // REFRESH BUTTON
        // =========================

        JButton refreshButton =
                new JButton("Refresh Report");

        bottomPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // BUTTON ACTION
        // =========================

        refreshButton.addActionListener(
                e -> loadSalesReport()
        );


        // Load data when report opens
        loadSalesReport();
    }


    // =========================
    // LOAD SALES REPORT
    // =========================

    private void loadSalesReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT s.sale_id, " +
                "s.sale_date, " +
                "s.total_amount, " +
                "u.full_name " +
                "FROM sales s " +
                "LEFT JOIN users u " +
                "ON s.user_id = u.user_id " +
                "ORDER BY s.sale_date DESC";


        int totalSales = 0;
        double totalRevenue = 0.0;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int saleId =
                        resultSet.getInt("sale_id");

                String saleDate =
                        resultSet.getString("sale_date");

                double totalAmount =
                        resultSet.getDouble(
                                "total_amount"
                        );

                String cashier =
                        resultSet.getString(
                                "full_name"
                        );


                if (cashier == null ||
                        cashier.trim().isEmpty()) {

                    cashier = "Unknown";
                }


                tableModel.addRow(
                        new Object[]{
                                saleId,
                                saleDate,
                                decimalFormat.format(
                                        totalAmount
                                ),
                                cashier
                        }
                );


                totalSales++;

                totalRevenue += totalAmount;
            }


            totalSalesLabel.setText(
                    "Total Sales: " + totalSales
            );

            totalRevenueLabel.setText(
                    "Total Revenue: "
                            + decimalFormat.format(
                            totalRevenue
                    )
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load Sales Report.\n\n"
                            + ex.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}


