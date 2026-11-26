package com.myticket;

import com.myticket.service.BookingService;
import com.myticket.view.BlueTheme;
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

                // Cohesive blue UI defaults across dialogs and controls
                UIManager.put("Panel.background", BlueTheme.APP_BG);
                UIManager.put("OptionPane.background", BlueTheme.APP_BG);
                UIManager.put("OptionPane.messageForeground", BlueTheme.TEXT_DARK);
                UIManager.put("TabbedPane.background", BlueTheme.SOFT_ICE);
                UIManager.put("TabbedPane.foreground", BlueTheme.NAVY);
                UIManager.put("TabbedPane.selected", BlueTheme.SKY_BLUE);

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