package gui;

import models.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private User loggedInUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public AdminDashboard(User user) {

        loggedInUser = user;

        setTitle("HealthFirst Pharmacy - Administrator Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());


        // =====================================================
        // LEFT SIDEBAR
        // =====================================================

        JPanel sidebar = new JPanel();

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        10,
                        20,
                        10
                )
        );

        sidebar.setPreferredSize(
                new Dimension(220, 0)
        );


        // =====================================================
        // PHARMACY NAME
        // =====================================================

        JLabel pharmacyLabel =
                new JLabel(
                        "HealthFirst Pharmacy"
                );

        pharmacyLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        pharmacyLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(
                pharmacyLabel
        );

        sidebar.add(
                Box.createVerticalStrut(10)
        );


        // =====================================================
        // ADMINISTRATOR NAME
        // =====================================================

        String fullName =
                user.getFullName();

        if (fullName == null ||
                fullName.trim().isEmpty()) {

            fullName =
                    user.getUsername();
        }


        JLabel administratorLabel =
                new JLabel(
                        "Administrator"
                );

        administratorLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        administratorLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel nameLabel =
                new JLabel(
                        fullName
                );

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        nameLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        sidebar.add(
                administratorLabel
        );

        sidebar.add(
                nameLabel
        );

        sidebar.add(
                Box.createVerticalStrut(30)
        );


        // =====================================================
        // NAVIGATION BUTTONS
        // =====================================================

        JButton dashboardButton =
                new JButton(
                        "Dashboard"
                );

        JButton medicinesButton =
                new JButton(
                        "Medicines"
                );

        JButton suppliersButton =
                new JButton(
                        "Suppliers"
                );

        JButton usersButton =
                new JButton(
                        "Users"
                );

        JButton reportsButton =
                new JButton(
                        "Reports"
                );


        JButton[] buttons = {
                dashboardButton,
                medicinesButton,
                suppliersButton,
                usersButton,
                reportsButton
        };


        for (JButton button : buttons) {

            button.setMaximumSize(
                    new Dimension(
                            190,
                            45
                    )
            );

            button.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            sidebar.add(
                    button
            );

            sidebar.add(
                    Box.createVerticalStrut(8)
            );
        }


        // =====================================================
        // LOGOUT
        // =====================================================

        sidebar.add(
                Box.createVerticalGlue()
        );


        JButton logoutButton =
                new JButton(
                        "Logout"
                );

        logoutButton.setMaximumSize(
                new Dimension(
                        190,
                        45
                )
        );

        logoutButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(
                logoutButton
        );


        add(
                sidebar,
                BorderLayout.WEST
        );


        // =====================================================
        // CONTENT PANEL
        // =====================================================

        cardLayout =
                new CardLayout();

        contentPanel =
                new JPanel(
                        cardLayout
                );


        // =====================================================
        // INITIAL DASHBOARD
        // =====================================================

        contentPanel.add(
                createDashboardPanel(),
                "DASHBOARD"
        );


        // Empty panels are added initially.
        // Actual panels will load when their buttons are clicked.

        contentPanel.add(
                new JPanel(),
                "MEDICINES"
        );

        contentPanel.add(
                new JPanel(),
                "SUPPLIERS"
        );

        contentPanel.add(
                new JPanel(),
                "USERS"
        );

        contentPanel.add(
                createReportsPanel(),
                "REPORTS"
        );


        add(
                contentPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // DASHBOARD BUTTON
        // =====================================================

        dashboardButton.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "DASHBOARD"
            );
        });


        // =====================================================
        // MEDICINES BUTTON
        // =====================================================

        medicinesButton.addActionListener(e -> {

            try {

                JPanel medicinePanel =
                        new MedicineManagementPanel();

                contentPanel.add(
                        medicinePanel,
                        "MEDICINE_CONTENT"
                );

                cardLayout.show(
                        contentPanel,
                        "MEDICINE_CONTENT"
                );

                contentPanel.revalidate();
                contentPanel.repaint();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open Medicine Management.\n\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // =====================================================
        // SUPPLIERS BUTTON
        // =====================================================

        suppliersButton.addActionListener(e -> {

            try {

                JPanel supplierPanel =
                        new SupplierManagementPanel();

                contentPanel.add(
                        supplierPanel,
                        "SUPPLIER_CONTENT"
                );

                cardLayout.show(
                        contentPanel,
                        "SUPPLIER_CONTENT"
                );

                contentPanel.revalidate();
                contentPanel.repaint();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open Supplier Management.\n\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // =====================================================
        // USERS BUTTON
        // =====================================================

        usersButton.addActionListener(e -> {

            try {

                JPanel userPanel =
                        new UserManagementPanel();

                contentPanel.add(
                        userPanel,
                        "USER_CONTENT"
                );

                cardLayout.show(
                        contentPanel,
                        "USER_CONTENT"
                );

                contentPanel.revalidate();
                contentPanel.repaint();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open User Management.\n\n"
                                + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // =====================================================
        // REPORTS BUTTON
        // =====================================================

        reportsButton.addActionListener(e -> {

            cardLayout.show(
                    contentPanel,
                    "REPORTS"
            );
        });


        // =====================================================
        // LOGOUT BUTTON
        // =====================================================

        logoutButton.addActionListener(e -> {

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to logout?",
                            "Confirm Logout",
                            JOptionPane.YES_NO_OPTION
                    );

            if (result ==
                    JOptionPane.YES_OPTION) {

                dispose();

                new LoginFrame().setVisible(true);
            }
        });


        // Show dashboard when application starts

        cardLayout.show(
                contentPanel,
                "DASHBOARD"
        );
    }


    // =====================================================
    // DASHBOARD PANEL
    // =====================================================

    private JPanel createDashboardPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        35,
                        30,
                        35
                )
        );


        JLabel titleLabel =
                new JLabel(
                        "Welcome to the Administrator Dashboard"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );


        JLabel descriptionLabel =
                new JLabel(
                        "Manage pharmacy inventory, suppliers, users and business reports."
                );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );


        JPanel headingPanel =
                new JPanel();

        headingPanel.setLayout(
                new BoxLayout(
                        headingPanel,
                        BoxLayout.Y_AXIS
                )
        );


        headingPanel.add(
                titleLabel
        );

        headingPanel.add(
                Box.createVerticalStrut(10)
        );

        headingPanel.add(
                descriptionLabel
        );


        panel.add(
                headingPanel,
                BorderLayout.NORTH
        );


        // =====================================================
        // INFORMATION CARDS
        // =====================================================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );


        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        0,
                        0,
                        0
                )
        );


        cardsPanel.add(
                createInfoPanel(
                        "Medicine Inventory",
                        "Manage medicines, stock quantities, prices and expiry dates."
                )
        );


        cardsPanel.add(
                createInfoPanel(
                        "Supplier Management",
                        "Manage pharmacy suppliers and their contact information."
                )
        );


        cardsPanel.add(
                createInfoPanel(
                        "User Management",
                        "Create and manage Administrator and Cashier accounts."
                )
        );


        cardsPanel.add(
                createInfoPanel(
                        "Business Reports",
                        "View sales, item-wise, low-stock and expiry reports."
                )
        );


        panel.add(
                cardsPanel,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =====================================================
    // INFORMATION PANEL
    // =====================================================

    private JPanel createInfoPanel(
            String heading,
            String description
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.LIGHT_GRAY
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );


        JLabel headingLabel =
                new JLabel(
                        heading
                );

        headingLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );


        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );


        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );


        panel.add(
                headingLabel,
                BorderLayout.NORTH
        );


        panel.add(
                descriptionLabel,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =====================================================
    // REPORTS PANEL
    // =====================================================

    private JPanel createReportsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        50,
                        70,
                        50,
                        70
                )
        );


        JButton salesButton =
                new JButton(
                        "Sales Report"
                );


        JButton itemWiseButton =
                new JButton(
                        "Item-Wise Report"
                );


        JButton lowStockButton =
                new JButton(
                        "Low Stock Report"
                );


        JButton expiryButton =
                new JButton(
                        "Expiry Report"
                );


        salesButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        itemWiseButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        lowStockButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        expiryButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );


        panel.add(
                salesButton
        );

        panel.add(
                itemWiseButton
        );

        panel.add(
                lowStockButton
        );

        panel.add(
                expiryButton
        );


        // =====================================================
        // SALES REPORT
        // =====================================================

        salesButton.addActionListener(e -> {

            openReportWindow(
                    "Sales Report",
                    new SalesReportPanel()
            );
        });


        // =====================================================
        // ITEM-WISE REPORT
        // =====================================================

        itemWiseButton.addActionListener(e -> {

            openReportWindow(
                    "Item-Wise Sales Report",
                    new ItemWiseReportPanel()
            );
        });


        // =====================================================
        // LOW STOCK REPORT
        // =====================================================

        lowStockButton.addActionListener(e -> {

            openReportWindow(
                    "Low Stock Report",
                    new LowStockReportPanel()
            );
        });


        // =====================================================
        // EXPIRY REPORT
        // =====================================================

        expiryButton.addActionListener(e -> {

            openReportWindow(
                    "Expiry Report",
                    new ExpiryReportPanel()
            );
        });


        return panel;
    }


    // =====================================================
    // OPEN REPORT WINDOW
    // =====================================================

    private void openReportWindow(
            String title,
            JPanel reportPanel
    ) {

        JFrame reportFrame =
                new JFrame(
                        title
                );


        reportFrame.setSize(
                1000,
                650
        );


        reportFrame.setLocationRelativeTo(
                this
        );


        reportFrame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        reportFrame.add(
                reportPanel
        );


        reportFrame.setVisible(
                true
        );
    }
}

