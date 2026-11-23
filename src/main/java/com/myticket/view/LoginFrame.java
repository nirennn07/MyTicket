package com.myticket.view;

import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private BookingService bookingService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginButton;

    public LoginFrame(BookingService bookingService) {

        this.bookingService = bookingService;

        setTitle("MyTicket - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
    }

    // ==============================
    // INITIALIZE COMPONENTS
    // ==============================

    private void initializeComponents() {

        JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(
                "MYTICKET",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // ==============================
        // USERNAME
        // ==============================

        JLabel usernameLabel =
                new JLabel("Username:");

        gbc.gridx = 0;
        gbc.gridy = 1;

        mainPanel.add(usernameLabel, gbc);

        usernameField =
                new JTextField(15);

        gbc.gridx = 1;
        gbc.gridy = 1;

        mainPanel.add(usernameField, gbc);

        // ==============================
        // PASSWORD
        // ==============================

        JLabel passwordLabel =
                new JLabel("Password:");

        gbc.gridx = 0;
        gbc.gridy = 2;

        mainPanel.add(passwordLabel, gbc);

        passwordField =
                new JPasswordField(15);

        gbc.gridx = 1;
        gbc.gridy = 2;

        mainPanel.add(passwordField, gbc);

        // ==============================
        // LOGIN BUTTON
        // ==============================

        loginButton =
                new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(
                this::handleLogin
        );

        add(mainPanel);
    }

    // ==============================
    // LOGIN
    // ==============================

    private void handleLogin(ActionEvent e) {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ==============================
        // SIMULATED LOGIN CREDENTIALS
        // ==============================

        boolean isCustomer =
                username.equals("user")
                        && password.equals("1234");

        boolean isAdmin =
                username.equals("admin")
                        && password.equals("admin");

        // ==============================
        // SUCCESSFUL LOGIN
        // ==============================

        if (isCustomer || isAdmin) {

            MainFrame mainFrame =
                    new MainFrame(
                            bookingService,
                            isAdmin,
                            username
                    );

            mainFrame.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}