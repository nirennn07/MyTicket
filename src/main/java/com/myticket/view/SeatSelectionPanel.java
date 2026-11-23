package com.myticket.view;

import com.myticket.model.Booking;
import com.myticket.model.Schedule;
import com.myticket.model.Seat;
import com.myticket.model.TicketType;
import com.myticket.service.BookingService;

import com.myticket.decorator.BookingCost;
import com.myticket.decorator.BaseBookingCost;
import com.myticket.decorator.PopcornComboDecorator;
import com.myticket.decorator.DrinkDecorator;
import com.myticket.decorator.SnackDecorator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeatSelectionPanel extends JPanel {

    private BookingService bookingService;
    private Schedule schedule;
    private String username;

    private Runnable bookingCompleteCallback;
    private Runnable backCallback;

    private ArrayList<Seat> selectedSeats;
    private ArrayList<Seat> seniorCitizenSeats;

    private JPanel seatsPanel;
    private JPanel selectedSeatsPanel;

    private JLabel totalLabel;

    private JCheckBox popcornCheckBox;
    private JCheckBox drinkCheckBox;
    private JCheckBox snackCheckBox;

    private static final Color AVAILABLE_COLOR =
            new Color(46, 204, 113);

    private static final Color SELECTED_COLOR =
            new Color(241, 196, 15);

    private static final Color UNAVAILABLE_COLOR =
            new Color(231, 76, 60);

    private static final Color SENIOR_COLOR =
            new Color(52, 152, 219);

    // =========================
    // CONSTRUCTORS
    // =========================

    // Original constructor retained for compatibility
    public SeatSelectionPanel(
            BookingService bookingService,
            Schedule schedule,
            Runnable bookingCompleteCallback,
            Runnable backCallback) {

        this(
                bookingService,
                schedule,
                "Unknown",
                bookingCompleteCallback,
                backCallback
        );
    }

    // New constructor with username
    public SeatSelectionPanel(
            BookingService bookingService,
            Schedule schedule,
            String username,
            Runnable bookingCompleteCallback,
            Runnable backCallback) {

        this.bookingService = bookingService;
        this.schedule = schedule;
        this.username = username;
        this.bookingCompleteCallback = bookingCompleteCallback;
        this.backCallback = backCallback;

        this.selectedSeats = new ArrayList<>();
        this.seniorCitizenSeats = new ArrayList<>();

        initializePanel();
    }

    // =========================
    // INITIALIZE PANEL
    // =========================

    private void initializePanel() {

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // =========================
        // TOP PANEL
        // =========================

        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("← Back");

        backButton.addActionListener(e -> {
            if (backCallback != null) {
                backCallback.run();
            }
        });

        JLabel titleLabel = new JLabel("Select Your Seats");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // CENTER PANEL
        // =========================

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));

        JLabel screenLabel = new JLabel(
                "SCREEN",
                SwingConstants.CENTER
        );

        screenLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        screenLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 20, 10
                )
        );

        centerPanel.add(screenLabel, BorderLayout.NORTH);

        seatsPanel = new JPanel(
                new GridLayout(0, 5, 10, 10)
        );

        seatsPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        refreshSeats();

        centerPanel.add(seatsPanel, BorderLayout.CENTER);

        // =========================
        // SEAT LEGEND
        // =========================

        JPanel legendPanel = new JPanel(
                new FlowLayout()
        );

        legendPanel.add(
                createLegendItem(
                        AVAILABLE_COLOR,
                        "Available"
                )
        );

        legendPanel.add(
                createLegendItem(
                        SELECTED_COLOR,
                        "Selected"
                )
        );

        legendPanel.add(
                createLegendItem(
                        SENIOR_COLOR,
                        "Senior Citizen"
                )
        );

        legendPanel.add(
                createLegendItem(
                        UNAVAILABLE_COLOR,
                        "Unavailable"
                )
        );

        centerPanel.add(
                legendPanel,
                BorderLayout.SOUTH
        );

        add(centerPanel, BorderLayout.CENTER);

        // =========================
        // RIGHT PANEL
        // =========================

        JPanel rightPanel = new JPanel();

        rightPanel.setLayout(
                new BoxLayout(
                        rightPanel,
                        BoxLayout.Y_AXIS
                )
        );

        rightPanel.setBorder(
                new EmptyBorder(
                        10, 20, 10, 20
                )
        );

        // =========================
        // SELECTED SEATS
        // =========================

        JLabel selectedSeatsTitle =
                new JLabel("Selected Seats");

        selectedSeatsTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        rightPanel.add(selectedSeatsTitle);

        rightPanel.add(
                Box.createVerticalStrut(10)
        );

        selectedSeatsPanel = new JPanel();

        selectedSeatsPanel.setLayout(
                new BoxLayout(
                        selectedSeatsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        rightPanel.add(selectedSeatsPanel);

        rightPanel.add(
                Box.createVerticalStrut(20)
        );

        // =========================
        // ADD-ONS
        // =========================

        JLabel addOnsTitle =
                new JLabel("Food & Drinks");

        addOnsTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        popcornCheckBox =
                new JCheckBox("Popcorn Combo - ₱150");

        drinkCheckBox =
                new JCheckBox("Drink - ₱80");

        snackCheckBox =
                new JCheckBox("Snack - ₱100");

        popcornCheckBox.addActionListener(
                e -> updateTotal()
        );

        drinkCheckBox.addActionListener(
                e -> updateTotal()
        );

        snackCheckBox.addActionListener(
                e -> updateTotal()
        );

        rightPanel.add(addOnsTitle);

        rightPanel.add(
                Box.createVerticalStrut(10)
        );

        rightPanel.add(popcornCheckBox);
        rightPanel.add(drinkCheckBox);
        rightPanel.add(snackCheckBox);

        rightPanel.add(
                Box.createVerticalStrut(25)
        );

        // =========================
        // TOTAL
        // =========================

        JLabel totalTitle =
                new JLabel("Total Amount");

        totalTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        totalLabel = new JLabel("₱0.00");

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        rightPanel.add(totalTitle);

        rightPanel.add(
                Box.createVerticalStrut(10)
        );

        rightPanel.add(totalLabel);

        rightPanel.add(
                Box.createVerticalStrut(25)
        );

        // =========================
        // BOOK BUTTON
        // =========================

        JButton bookButton =
                new JButton("Confirm Booking");

        bookButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        bookButton.addActionListener(
                e -> handleBooking()
        );

        rightPanel.add(bookButton);

        add(rightPanel, BorderLayout.EAST);

        updateSelectedSeatsPanel();
        updateTotal();
    }

    // =========================
    // SEAT DISPLAY
    // =========================

    private void refreshSeats() {

        seatsPanel.removeAll();

        ArrayList<Seat> seats =
                schedule.getSeats();

        for (Seat seat : seats) {

            JButton seatButton =
                    new JButton(
                            seat.getSeatNumber()
                    );

            seatButton.setPreferredSize(
                    new Dimension(60, 40)
            );

            if (!seat.isAvailable()) {

                seatButton.setBackground(
                        UNAVAILABLE_COLOR
                );

                seatButton.setForeground(
                        Color.WHITE
                );

                seatButton.setEnabled(false);

            } else if (
                    seniorCitizenSeats.contains(seat)
            ) {

                seatButton.setBackground(
                        SENIOR_COLOR
                );

                seatButton.setForeground(
                        Color.WHITE
                );

            } else if (
                    selectedSeats.contains(seat)
            ) {

                seatButton.setBackground(
                        SELECTED_COLOR
                );

                seatButton.setForeground(
                        Color.BLACK
                );

            } else {

                seatButton.setBackground(
                        AVAILABLE_COLOR
                );

                seatButton.setForeground(
                        Color.WHITE
                );
            }

            seatButton.setOpaque(true);
            seatButton.setBorderPainted(false);

            seatButton.addActionListener(
                    e -> toggleSeatSelection(seat)
            );

            seatsPanel.add(seatButton);
        }

        seatsPanel.revalidate();
        seatsPanel.repaint();
    }

    // =========================
    // SEAT SELECTION
    // =========================

    private void toggleSeatSelection(Seat seat) {

        if (selectedSeats.contains(seat)) {

            selectedSeats.remove(seat);

            // Remove senior citizen designation
            seniorCitizenSeats.remove(seat);

        } else {

            if (seat.isAvailable()) {

                selectedSeats.add(seat);
            }
        }

        refreshSeats();

        updateSelectedSeatsPanel();

        updateTotal();
    }

    // =========================
    // SELECTED SEATS PANEL
    // =========================

    private void updateSelectedSeatsPanel() {

        selectedSeatsPanel.removeAll();

        if (selectedSeats.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel("No seats selected");

            selectedSeatsPanel.add(emptyLabel);

        } else {

            for (Seat seat : selectedSeats) {

                JPanel seatRow =
                        new JPanel(
                                new FlowLayout(
                                        FlowLayout.LEFT
                                )
                        );

                JLabel seatLabel =
                        new JLabel(
                                seat.getSeatNumber()
                        );

                JCheckBox seniorCheckBox =
                        new JCheckBox(
                                "Senior Citizen"
                        );

                seniorCheckBox.setSelected(
                        seniorCitizenSeats.contains(seat)
                );

                seniorCheckBox.addActionListener(
                        e -> {

                            if (
                                    seniorCheckBox.isSelected()
                            ) {

                                if (
                                        !seniorCitizenSeats
                                                .contains(seat)
                                ) {

                                    seniorCitizenSeats
                                            .add(seat);
                                }

                            } else {

                                seniorCitizenSeats
                                        .remove(seat);
                            }

                            refreshSeats();

                            updateTotal();
                        }
                );

                seatRow.add(seatLabel);
                seatRow.add(seniorCheckBox);

                selectedSeatsPanel.add(seatRow);
            }
        }

        selectedSeatsPanel.revalidate();
        selectedSeatsPanel.repaint();
    }

    // =========================
    // TOTAL CALCULATION
    // =========================

    private void updateTotal() {

        double ticketCost = 0;

        double ticketPrice =
                schedule.getTicketPrice();

        for (Seat seat : selectedSeats) {

            if (
                    seniorCitizenSeats.contains(seat)
            ) {

                // Senior Citizen Discount: 20%
                ticketCost +=
                        ticketPrice * 0.80;

            } else {

                ticketCost += ticketPrice;
            }
        }

        // Base cost
        BookingCost bookingCost =
                new BaseBookingCost(
                        ticketCost
                );

        // Decorator Pattern

        if (
                popcornCheckBox.isSelected()
        ) {

            bookingCost =
                    new PopcornComboDecorator(
                            bookingCost
                    );
        }

        if (
                drinkCheckBox.isSelected()
        ) {

            bookingCost =
                    new DrinkDecorator(
                            bookingCost
                    );
        }

        if (
                snackCheckBox.isSelected()
        ) {

            bookingCost =
                    new SnackDecorator(
                            bookingCost
                    );
        }

        totalLabel.setText(
                String.format(
                        "₱%.2f",
                        bookingCost.getCost()
                )
        );
    }

    // =========================
    // BOOKING
    // =========================

    private void handleBooking() {

        if (selectedSeats.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one seat.",
                    "No Seats Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            // Create a map of ticket types.
            // Seats not included in this map are REGULAR.

            Map<String, TicketType> ticketTypes =
                    new HashMap<>();

            for (Seat seat : seniorCitizenSeats) {

                ticketTypes.put(
                        seat.getSeatNumber(),
                        TicketType.SENIOR
                );
            }

            // Create booking using the revised
            // BookingService method.

            Booking booking =
                    bookingService.createBooking(
                            schedule,
                            selectedSeats,
                            ticketTypes,
                            popcornCheckBox.isSelected(),
                            drinkCheckBox.isSelected(),
                            snackCheckBox.isSelected(),
                            username
                    );

            // =========================
            // CONFIRMATION
            // =========================

            String confirmationMessage =
                    "Booking Successful!\n\n"
                            + "Booking ID: "
                            + booking.getBookingID()

                            + "\nMovie: "
                            + schedule.getMovie().getTitle()

                            + "\nSchedule: "
                            + schedule.getDate()
                            + " "
                            + schedule.getTime()

                            + "\nSeats: "
                            + booking.getSelectedSeats()

                            + "\n\nTicket Total: ₱"
                            + String.format(
                            "%.2f",
                            booking.getTotalAmount()
                    )

                            + "\nDiscount: ₱"
                            + String.format(
                            "%.2f",
                            booking.getDiscountAmount()
                    )

                            + "\nAdd-ons: "
                            + booking.getAddOns()

                            + "\nAdd-on Amount: ₱"
                            + String.format(
                            "%.2f",
                            booking.getAddOnAmount()
                    )

                            + "\n\nFinal Amount: ₱"
                            + String.format(
                            "%.2f",
                            booking.getFinalAmount()
                    );

            JOptionPane.showMessageDialog(
                    this,
                    confirmationMessage,
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Clear selections

            selectedSeats.clear();
            seniorCitizenSeats.clear();

            popcornCheckBox.setSelected(false);
            drinkCheckBox.setSelected(false);
            snackCheckBox.setSelected(false);

            // Update UI

            refreshSeats();
            updateSelectedSeatsPanel();
            updateTotal();

            // Callback

            if (
                    bookingCompleteCallback != null
            ) {

                bookingCompleteCallback.run();
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed:\n"
                            + ex.getMessage(),
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE
            );

            refreshSeats();
        }
    }

    // =========================
    // LEGEND
    // =========================

    private JPanel createLegendItem(
            Color color,
            String text) {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JLabel colorLabel =
                new JLabel("  ");

        colorLabel.setOpaque(true);

        colorLabel.setBackground(color);

        colorLabel.setPreferredSize(
                new Dimension(20, 20)
        );

        JLabel textLabel =
                new JLabel(text);

        panel.add(colorLabel);
        panel.add(textLabel);

        return panel;
    }
}