package com.myticket.view;

import com.myticket.service.BookingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(420, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
    }

    // ==============================
    // INITIALIZE COMPONENTS
    // ==============================

    private void initializeComponents() {

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(BlueTheme.APP_BG);
        outerPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BlueTheme.CARD_BG);
        mainPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BlueTheme.createCardBorder(),
                        new EmptyBorder(10, 16, 10, 16)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(
                "MYTICKET",
                SwingConstants.CENTER
        );

        titleLabel.setFont(BlueTheme.FONT_TITLE);
        titleLabel.setForeground(BlueTheme.NAVY);

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
        usernameLabel.setFont(BlueTheme.FONT_BODY_BOLD);
        usernameLabel.setForeground(BlueTheme.NAVY);

        gbc.gridx = 0;
        gbc.gridy = 1;

        mainPanel.add(usernameLabel, gbc);

        usernameField =
                new JTextField(15);
        BlueTheme.styleTextField(usernameField);

        gbc.gridx = 1;
        gbc.gridy = 1;

        mainPanel.add(usernameField, gbc);

        // ==============================
        // PASSWORD
        // ==============================

        JLabel passwordLabel =
                new JLabel("Password:");
        passwordLabel.setFont(BlueTheme.FONT_BODY_BOLD);
        passwordLabel.setForeground(BlueTheme.NAVY);

        gbc.gridx = 0;
        gbc.gridy = 2;

        mainPanel.add(passwordLabel, gbc);

        passwordField =
                new JPasswordField(15);
        BlueTheme.styleTextField(passwordField);

        gbc.gridx = 1;
        gbc.gridy = 2;

        mainPanel.add(passwordField, gbc);

        // ==============================
        // LOGIN BUTTON
        // ==============================

        loginButton =
                new JButton("Login");
        BlueTheme.stylePrimaryButton(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 4, 8);

        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(
                this::handleLogin
        );

        outerPanel.add(mainPanel);
        add(outerPanel);
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