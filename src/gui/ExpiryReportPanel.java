package gui;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ExpiryReportPanel extends JPanel {

    private JTable expiryTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public ExpiryReportPanel() {

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
                new JLabel("Expiry Report");

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
                "Expiry Date",
                "Days Remaining",
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


        expiryTable =
                new JTable(tableModel);

        expiryTable.setRowHeight(25);


        JScrollPane scrollPane =
                new JScrollPane(expiryTable);

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
                        "Expiry Alerts: 0"
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
                e -> loadExpiryReport()
        );


        // Load report automatically
        loadExpiryReport();
    }


    // =========================
    // LOAD EXPIRY REPORT
    // =========================

    private void loadExpiryReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT medicine_id, " +
                "name, " +
                "company, " +
                "expiry_date " +
                "FROM medicines " +
                "WHERE expiry_date <= DATE_ADD(CURDATE(), INTERVAL 90 DAY) " +
                "ORDER BY expiry_date ASC";


        int expiryCount = 0;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {


            LocalDate today =
                    LocalDate.now();


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

                java.sql.Date sqlExpiryDate =
                        resultSet.getDate(
                                "expiry_date"
                        );


                if (sqlExpiryDate == null) {
                    continue;
                }


                LocalDate expiryDate =
                        sqlExpiryDate.toLocalDate();


                long daysRemaining =
                        ChronoUnit.DAYS.between(
                                today,
                                expiryDate
                        );


                String status;


                if (daysRemaining < 0) {

                    status = "EXPIRED";

                } else if (daysRemaining <= 30) {

                    status = "EXPIRES WITHIN 30 DAYS";

                } else {

                    status = "EXPIRING SOON";
                }


                tableModel.addRow(
                        new Object[]{
                                medicineId,
                                medicineName,
                                company,
                                expiryDate,
                                daysRemaining,
                                status
                        }
                );


                expiryCount++;
            }


            countLabel.setText(
                    "Expiry Alerts: "
                            + expiryCount
            );


        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load Expiry Report.\n\n"
                            + ex.getMessage(),
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}

