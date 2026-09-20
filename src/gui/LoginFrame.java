package gui;

import dao.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton clearButton;

    private UserDAO userDAO;

    public LoginFrame() {

        userDAO = new UserDAO();

        // =========================================
        // WINDOW SETTINGS
        // =========================================

        setTitle("HealthFirst Pharmacy - Login");

        setSize(450, 350);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        // =========================================
        // MAIN PANEL
        // =========================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        // =========================================
        // HEADER
        // =========================================

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel pharmacyLabel =
                new JLabel(
                        "HealthFirst Pharmacy"
                );

        pharmacyLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        pharmacyLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        JLabel loginLabel =
                new JLabel(
                        "System Login"
                );

        loginLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        headerPanel.add(
                pharmacyLabel
        );

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(
                loginLabel
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // =========================================
        // LOGIN FORM
        // =========================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        5,
                        8,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Username label
        JLabel usernameLabel =
                new JLabel("Username:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(
                usernameLabel,
                gbc
        );

        // Username field
        usernameField =
                new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;

        formPanel.add(
                usernameField,
                gbc
        );

        // Password label
        JLabel passwordLabel =
                new JLabel("Password:");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        formPanel.add(
                passwordLabel,
                gbc
        );

        // Password field
        passwordField =
                new JPasswordField();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;

        formPanel.add(
                passwordField,
                gbc
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // =========================================
        // BUTTONS
        // =========================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        loginButton =
                new JButton("Login");

        clearButton =
                new JButton("Clear");

        buttonPanel.add(
                loginButton
        );

        buttonPanel.add(
                clearButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // =========================================
        // LOGIN BUTTON
        // =========================================

        loginButton.addActionListener(e -> login());

        // =========================================
        // CLEAR BUTTON
        // =========================================

        clearButton.addActionListener(e -> {

            usernameField.setText("");

            passwordField.setText("");

            usernameField.requestFocus();
        });

        // =========================================
        // ENTER KEY
        // =========================================

        passwordField.addActionListener(e -> login());

        usernameField.addActionListener(e -> login());
    }

    // =============================================
    // LOGIN METHOD
    // =============================================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // =========================================
        // VALIDATION
        // =========================================

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your username.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE
            );

            usernameField.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE
            );

            passwordField.requestFocus();

            return;
        }

        // =========================================
        // CHECK DATABASE
        // =========================================

        try {

            User user =
                    userDAO.login(
                            username,
                            password
                    );

            // =====================================
            // INVALID LOGIN
            // =====================================

            if (user == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                passwordField.setText("");

                passwordField.requestFocus();

                return;
            }

            // =====================================
            // ADMIN LOGIN
            // =====================================

            if (user.getRole().equalsIgnoreCase("Admin")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, "
                                + user.getFullName()
                                + "!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                AdminDashboard adminDashboard =
                        new AdminDashboard(user);

                adminDashboard.setVisible(true);

            }

            // =====================================
            // CASHIER LOGIN
            // =====================================

            else if (
                    user.getRole()
                            .equalsIgnoreCase("Cashier")
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, "
                                + user.getFullName()
                                + "!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                CashierDashboard cashierDashboard =
                        new CashierDashboard(user);

                cashierDashboard.setVisible(true);

            }

            // =====================================
            // UNKNOWN ROLE
            // =====================================

            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Your account has an invalid role.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while logging in.\n\n"
                            + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =============================================
    // MAIN METHOD
    // =============================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);
        });
    }
}