package com.myticket;

import com.myticket.service.BookingService;
import com.myticket.view.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                // Set the system's native look and feel
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

                // Create the application's central service
                BookingService bookingService = new BookingService();

                // Create the login window
                LoginFrame loginFrame = new LoginFrame(bookingService);

                // Display the login window
                loginFrame.setVisible(true);

            } catch (Exception e) {

                // Handle errors during application startup
                System.err.println("Failed to start MyTicket.");
                e.printStackTrace();

            }

        });

    }

}