package gui;

import models.User;

import javax.swing.*;
import java.awt.*;

public class CashierDashboard extends JFrame {

    private User loggedInUser;

    public CashierDashboard(User user) {

        loggedInUser = user;

        setTitle("HealthFirst Pharmacy - Cashier Dashboard");

        setSize(1100, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // =========================
        // TOP HEADER
        // =========================

        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel titleLabel =
                new JLabel("HealthFirst Pharmacy");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel userLabel =
                new JLabel(
                        "Cashier: " +
                        user.getFullName()
                );

        userLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        headerPanel.add(
                userLabel,
                BorderLayout.EAST
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );


        // =========================
        // CASHIER POS
        // =========================

        CashierPOSPanel posPanel =
                new CashierPOSPanel(user);

        add(
                posPanel,
                BorderLayout.CENTER
        );


        // =========================
        // BOTTOM PANEL
        // =========================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton logoutButton =
                new JButton("Logout");

        bottomPanel.add(logoutButton);

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // LOGOUT
        // =========================

        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame().setVisible(true);

        });
    }
}