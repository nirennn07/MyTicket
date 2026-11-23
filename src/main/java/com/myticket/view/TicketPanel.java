package com.myticket.view;

import com.myticket.model.Ticket;
import com.myticket.model.Booking;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TicketPanel extends JPanel {

    private BookingService bookingService;

    private DefaultListModel<Ticket> ticketListModel;

    private JList<Ticket> ticketList;

    private JTextArea ticketDetailsArea;

    public TicketPanel(BookingService bookingService) {

        this.bookingService = bookingService;

        setLayout(new BorderLayout(10, 10));

        initializeComponents();

        refreshTickets();
    }

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
                detailsScrollPane,
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);

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

    /**
     * Displays the details of a selected ticket.
     */
    private void displayTicketDetails(Ticket ticket) {

        if (ticket == null) {

            ticketDetailsArea.setText("");

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
    }

    /**
     * Refreshes the ticket list.
     */
    public void refreshTickets() {

        try {

            ticketListModel.clear();

            ArrayList<Ticket> tickets =
                    bookingService.getTickets();

            for (Ticket ticket : tickets) {

                ticketListModel.addElement(ticket);
            }

            ticketDetailsArea.setText("");

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