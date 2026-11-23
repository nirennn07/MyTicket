
package com.myticket.view;

import com.myticket.model.Ticket;
import com.myticket.model.Booking;
import com.myticket.service.BookingService;
import com.myticket.util.QRCodeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TicketPanel extends JPanel {

    private BookingService bookingService;

    private String username;

    private DefaultListModel<Ticket> ticketListModel;

    private JList<Ticket> ticketList;

    private JTextArea ticketDetailsArea;

    private JLabel qrCodeLabel;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    // Old constructor for compatibility
    public TicketPanel(BookingService bookingService) {
        this(bookingService, "Unknown");
    }

    // New constructor with username
    public TicketPanel(BookingService bookingService, String username) {

        this.bookingService = bookingService;
        this.username = username;

        setLayout(new BorderLayout(10, 10));

        initializeComponents();

        refreshTickets();
    }

    // =====================================
    // INITIALIZE COMPONENTS
    // =====================================

    private void initializeComponents() {

        // ==============================
        // TITLE
        // ==============================

        JLabel titleLabel = new JLabel(
                "My Tickets",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        // ==============================
        // TICKET LIST
        // ==============================

        ticketListModel = new DefaultListModel<>();

        ticketList = new JList<>(
                ticketListModel
        );

        ticketList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane ticketScrollPane =
                new JScrollPane(ticketList);

        ticketScrollPane.setPreferredSize(
                new Dimension(250, 400)
        );

        // ==============================
        // TICKET DETAILS
        // ==============================

        ticketDetailsArea = new JTextArea();

        ticketDetailsArea.setEditable(false);

        ticketDetailsArea.setLineWrap(true);

        ticketDetailsArea.setWrapStyleWord(true);

        ticketDetailsArea.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        ticketDetailsArea.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        JScrollPane detailsScrollPane =
                new JScrollPane(ticketDetailsArea);

        // ==============================
        // QR CODE DISPLAY
        // ==============================

        qrCodeLabel = new JLabel();

        qrCodeLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JLabel qrTitleLabel = new JLabel(
                "Ticket QR Code",
                SwingConstants.CENTER
        );

        qrTitleLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        JPanel qrPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        qrPanel.add(
                qrTitleLabel,
                BorderLayout.NORTH
        );

        qrPanel.add(
                qrCodeLabel,
                BorderLayout.CENTER
        );

        qrPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        // ==============================
        // DETAILS PANEL
        // ==============================

        JPanel detailsPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        detailsPanel.add(
                detailsScrollPane,
                BorderLayout.CENTER
        );

        detailsPanel.add(
                qrPanel,
                BorderLayout.SOUTH
        );

        // ==============================
        // CENTER PANEL
        // ==============================

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.add(
                ticketScrollPane,
                BorderLayout.WEST
        );

        centerPanel.add(
                detailsPanel,
                BorderLayout.CENTER
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // ACTIONS
        // ==============================

        ticketList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                Ticket selectedTicket =
                        ticketList.getSelectedValue();

                displayTicketDetails(
                        selectedTicket
                );
            }
        });
    }

    // =====================================
    // DISPLAY TICKET DETAILS
    // =====================================

    /**
     * Displays the details and QR code
     * of a selected ticket.
     */
    private void displayTicketDetails(Ticket ticket) {

        if (ticket == null) {

            ticketDetailsArea.setText("");

            qrCodeLabel.setIcon(null);

            qrCodeLabel.setText("");

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

        // ==============================
        // TICKET INFORMATION
        // ==============================

        ticketDetailsArea.setText(

                "TICKET DETAILS\n"
                        + "==============================\n\n"

                        + "Ticket ID: "
                        + ticket.getTicketID()
                        + "\n\n"

                        + "Movie: "
                        + ticket.getMovieTitle()
                        + "\n\n"

                        + "Seat: "
                        + ticket.getSeat().getSeatNumber()
                        + "\n\n"

                        + "Screening Date: "
                        + ticket.getScreeningDate()
                        + "\n\n"

                        + "Screening Time: "
                        + ticket.getScreeningTime()
                        + "\n\n"

                        + "Ticket Type: "
                        + ticketType
                        + "\n\n"

                        + "Ticket Price: ₱"
                        + String.format(
                        "%.2f",
                        ticket.getTicketPrice()
                )
                        + "\n\n"

                        + "Discount: ₱"
                        + String.format(
                        "%.2f",
                        ticket.getDiscountAmount()
                )
                        + "\n\n"

                        + "Final Ticket Price: ₱"
                        + String.format(
                        "%.2f",
                        ticket.getFinalPrice()
                )
                        + "\n\n"

                        + "BOOKING DETAILS\n"
                        + "==============================\n\n"

                        + "Booking ID: "
                        + booking.getBookingID()
                        + "\n\n"

                        + "Booking Ticket Total: ₱"
                        + String.format(
                        "%.2f",
                        booking.getTotalAmount()
                )
                        + "\n\n"

                        + "Booking Discount: ₱"
                        + String.format(
                        "%.2f",
                        booking.getDiscountAmount()
                )
                        + "\n\n"

                        + "Booking Add-ons: "
                        + addOns
                        + "\n\n"

                        + "Add-on Amount: ₱"
                        + String.format(
                        "%.2f",
                        booking.getAddOnAmount()
                )
                        + "\n\n"

                        + "Booking Final Amount: ₱"
                        + String.format(
                        "%.2f",
                        booking.getFinalAmount()
                )
        );

        // ==============================
        // GENERATE QR CODE
        // ==============================

        try {

            BufferedImage qrImage =
                    QRCodeGenerator.generateQRCode(
                            ticket.getQRCodeContent(),
                            200,
                            200
                    );

            qrCodeLabel.setIcon(
                    new ImageIcon(qrImage)
            );

            qrCodeLabel.setText("");

        } catch (Exception e) {

            qrCodeLabel.setIcon(null);

            qrCodeLabel.setText(
                    "Unable to generate QR code."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to generate ticket QR code.\n"
                            + e.getMessage(),
                    "QR Code Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================
    // REFRESH TICKETS
    // =====================================

    /**
     * Refreshes the ticket list
     * for the currently logged-in user.
     */
    public void refreshTickets() {

        try {

            ticketListModel.clear();

            ArrayList<Ticket> tickets =
                    bookingService.getTicketsForUser(username);

            for (Ticket ticket : tickets) {

                ticketListModel.addElement(ticket);
            }

            ticketDetailsArea.setText("");

            qrCodeLabel.setIcon(null);

            qrCodeLabel.setText("");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load tickets.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
