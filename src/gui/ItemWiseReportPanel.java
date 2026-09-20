package gui;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

public class ItemWiseReportPanel extends JPanel {

    private JTable itemTable;
    private DefaultTableModel tableModel;

    private JLabel totalItemsLabel;
    private JLabel totalQuantityLabel;
    private JLabel totalRevenueLabel;

    private DecimalFormat decimalFormat =
            new DecimalFormat("R #,##0.00");

    public ItemWiseReportPanel() {

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
                new JLabel("Item-Wise Sales Report");

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
                "Quantity Sold",
                "Price",
                "Revenue"
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


        itemTable =
                new JTable(tableModel);

        itemTable.setRowHeight(25);


        JScrollPane scrollPane =
                new JScrollPane(itemTable);

        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =========================
        // SUMMARY
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


        totalItemsLabel =
                new JLabel(
                        "Items Sold: 0"
                );

        totalQuantityLabel =
                new JLabel(
                        "Quantity Sold: 0"
                );

        totalRevenueLabel =
                new JLabel(
                        "Revenue: R 0.00"
                );


        totalItemsLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        totalQuantityLabel.setFont(
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
                totalItemsLabel
        );

        summaryPanel.add(
                Box.createHorizontalStrut(20)
        );

        summaryPanel.add(
                totalQuantityLabel
        );

        summaryPanel.add(
                Box.createHorizontalStrut(20)
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
        // BUTTON ACTION
        // =========================

        refreshButton.addActionListener(
                e -> loadItemWiseReport()
        );


        // Load report automatically
        loadItemWiseReport();
    }


    // =========================
    // LOAD REPORT
    // =========================

    private void loadItemWiseReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT " +
                "m.medicine_id, " +
                "m.name, " +
                "SUM(si.quantity_sold) AS quantity_sold, " +
                "m.price, " +
                "SUM(si.quantity_sold * si.price_at_sale) AS revenue " +
                "FROM sale_items si " +
                "INNER JOIN medicines m " +
                "ON si.medicine_id = m.medicine_id " +
                "GROUP BY m.medicine_id, m.name, m.price " +
                "ORDER BY revenue DESC";


        int totalItems = 0;
        int totalQuantity = 0;
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

                int medicineId =
                        resultSet.getInt(
                                "medicine_id"
                        );

                String medicineName =
                        resultSet.getString(
                                "name"
                        );

                int quantitySold =
                        resultSet.getInt(
                                "quantity_sold"
                        );

                double price =
                        resultSet.getDouble(
                                "price"
                        );

                double revenue =
                        resultSet.getDouble(
                                "revenue"
                        );


                tableModel.addRow(
                        new Object[]{
                                medicineId,
                                medicineName,
                                quantitySold,
                                decimalFormat.format(
                                        price
                                ),
                                decimalFormat.format(
                                        revenue
                                )
                        }
                );


                totalItems++;

                totalQuantity += quantitySold;

                totalRevenue += revenue;
            }


            totalItemsLabel.setText(
                    "Items Sold: " + totalItems
            );

            totalQuantityLabel.setText(
                    "Quantity Sold: " + totalQuantity
            );

            totalRevenueLabel.setText(
                    "Revenue: "
                            + decimalFormat.format(
                            totalRevenue
                    )
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load Item-Wise Report.\n\n"
                            + ex.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}


