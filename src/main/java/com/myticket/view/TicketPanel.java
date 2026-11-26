package com.myticket.view;

import com.myticket.model.Booking;
import com.myticket.model.Ticket;
import com.myticket.service.BookingService;
import com.myticket.util.QRCodeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TicketPanel extends JPanel {

    private final BookingService bookingService;
    private final String username;

    // Bound automatically from TicketPanel.form
    private JPanel rootPanel;
    private JPanel headerCardPanel;
    private JLabel headerTitleLabel;
    private JLabel headerHintLabel;
    private JPanel mainContentPanel;

    private JPanel ticketListCardPanel;
    private JScrollPane ticketScrollPane;
    private JList<Ticket> ticketList;

    private JPanel ticketPassCardPanel;
    private JScrollPane detailsScrollPane;
    private JTextArea ticketDetailsArea;

    private JPanel qrPanel;
    private JLabel qrTitleLabel;
    private JLabel qrCodeLabel;
    private JLabel qrSubLabel;

    private DefaultListModel<Ticket> ticketListModel;

    // =====================================
    // CONSTRUCTORS
    // =====================================

    // Old constructor for compatibility
    public TicketPanel(BookingService bookingService) {
        this(bookingService, "Unknown");
    }

    // New constructor with username
    public TicketPanel(BookingService bookingService, String username) {
        this.bookingService = bookingService;
        this.username = username;

        setLayout(new BorderLayout());
        if (rootPanel != null) {
            add(rootPanel, BorderLayout.CENTER);
        }

        configureBoundComponents();
        setupListeners();
        refreshTickets();
    }

    // =====================================
    // CONFIGURE BOUND COMPONENTS
    // =====================================

    private void configureBoundComponents() {
        ticketListModel = new DefaultListModel<>();
        ticketList.setModel(ticketListModel);
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketList.setFixedCellHeight(36);
    }

    // =====================================
    // LISTENERS
    // =====================================

    private void setupListeners() {
        ticketList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Ticket selectedTicket = ticketList.getSelectedValue();
                displayTicketDetails(selectedTicket);
            }
        });
    }

    // =====================================
    // DISPLAY TICKET DETAILS
    // =====================================

    /**
     * Displays the details and QR code of a selected ticket.
     */
    private void displayTicketDetails(Ticket ticket) {
        if (ticket == null) {
            ticketDetailsArea.setText("");
            qrCodeLabel.setIcon(null);
            qrCodeLabel.setText("No ticket selected");
            return;
        }

        Booking booking = ticket.getBooking();

        String ticketType = ticket.isSeniorCitizen()
                ? "Senior Citizen"
                : "Regular";

        String addOns = booking.getAddOns();
        if (addOns == null || addOns.isBlank()) {
            addOns = "None";
        }

        ticketDetailsArea.setText(
                "TICKET DETAILS\n"
                        + "------------------------------\n"
                        + "Ticket ID: " + ticket.getTicketID() + "\n"
                        + "Movie: " + ticket.getMovieTitle() + "\n"
                        + "Seat: " + ticket.getSeat().getSeatNumber() + "\n"
                        + "Screening Date: " + ticket.getScreeningDate() + "\n"
                        + "Screening Time: " + ticket.getScreeningTime() + "\n"
                        + "Ticket Type: " + ticketType + "\n"
                        + "Ticket Price: ₱" + String.format("%.2f", ticket.getTicketPrice()) + "\n"
                        + "Discount: ₱" + String.format("%.2f", ticket.getDiscountAmount()) + "\n"
                        + "Final Ticket Price: ₱" + String.format("%.2f", ticket.getFinalPrice()) + "\n\n"
                        + "BOOKING DETAILS\n"
                        + "------------------------------\n"
                        + "Booking ID: " + booking.getBookingID() + "\n"
                        + "Booking Ticket Total: ₱" + String.format("%.2f", booking.getTotalAmount()) + "\n"
                        + "Booking Discount: ₱" + String.format("%.2f", booking.getDiscountAmount()) + "\n"
                        + "Booking Add-ons: " + addOns + "\n"
                        + "Add-on Amount: ₱" + String.format("%.2f", booking.getAddOnAmount()) + "\n"
                        + "Booking Final Amount: ₱" + String.format("%.2f", booking.getFinalAmount())
        );
        ticketDetailsArea.setCaretPosition(0);

        try {
            BufferedImage qrImage = QRCodeGenerator.generateQRCode(
                    ticket.getQRCodeContent(),
                    185,
                    185
            );
            qrCodeLabel.setIcon(new ImageIcon(qrImage));
            qrCodeLabel.setText("");
        } catch (Exception e) {
            qrCodeLabel.setIcon(null);
            qrCodeLabel.setText("Unable to generate QR code.");

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to generate ticket QR code.\n" + e.getMessage(),
                    "QR Code Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================
    // REFRESH TICKETS
    // =====================================

    /**
     * Refreshes the ticket list for the currently logged-in user.
     */
    public void refreshTickets() {
        try {
            ticketListModel.clear();

            ArrayList<Ticket> tickets = bookingService.getTicketsForUser(username);
            for (Ticket ticket : tickets) {
                ticketListModel.addElement(ticket);
            }

            ticketDetailsArea.setText("");
            qrCodeLabel.setIcon(null);
            qrCodeLabel.setText("No ticket selected");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load tickets.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}