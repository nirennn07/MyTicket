package com.myticket.view;

import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private BookingService bookingService;

    // Bound automatically from LoginFrame.form
    private JPanel mainPanel;
    private JPanel brandPanel;
    private JPanel formPanel;
    private JLabel logoIconLabel;
    private JLabel titleLabel;
    private JLabel taglineLabel;
    private JLabel footerBadgeLabel;
    private JLabel welcomeHeaderLabel;
    private JLabel welcomeSubLabel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;

    public LoginFrame(BookingService bookingService) {
        this.bookingService = bookingService;

        setTitle("MyTicket - Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        char defaultEchoChar = passwordField.getEchoChar();
        showPasswordCheckBox.addActionListener(e ->
                passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : defaultEchoChar)
        );

        loginButton.addActionListener(this::handleLogin);
        getRootPane().setDefaultButton(loginButton);
    }

    // ==============================
    // LOGIN
    // ==============================

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

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

        boolean isCustomer = username.equals("user") && password.equals("1234");
        boolean isAdmin = username.equals("admin") && password.equals("admin");

        // ==============================
        // SUCCESSFUL LOGIN
        // ==============================

        if (isCustomer || isAdmin) {
            MainFrame mainFrame = new MainFrame(
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