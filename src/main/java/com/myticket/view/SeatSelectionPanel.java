package com.myticket.view;

import com.myticket.decorator.BaseBookingCost;
import com.myticket.decorator.BookingCost;
import com.myticket.decorator.DrinkDecorator;
import com.myticket.decorator.PopcornComboDecorator;
import com.myticket.decorator.SnackDecorator;
import com.myticket.model.Booking;
import com.myticket.model.Schedule;
import com.myticket.model.Seat;
import com.myticket.model.TicketType;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeatSelectionPanel extends JPanel {

    private final BookingService bookingService;
    private final Schedule schedule;
    private final String username;

    private final Runnable bookingCompleteCallback;
    private final Runnable backCallback;

    private final ArrayList<Seat> selectedSeats;
    private final ArrayList<Seat> seniorCitizenSeats;

    // Bound automatically from SeatSelectionPanel.form
    private JPanel rootPanel;
    private JPanel topHeaderPanel;
    private JButton backButton;
    private JPanel headerTitleGroup;
    private JLabel titleLabel;
    private JLabel showtimeInfoLabel;
    private JLabel seatCounterBadge;

    private JPanel mainSplitPanel;
    private JPanel theaterCardPanel;
    private JPanel screenStagePanel;
    private JLabel screenLabel;
    private JPanel seatsPanel;

    private JPanel legendFooterPanel;
    private JLabel legendAvailableLabel;
    private JLabel legendSelectedLabel;
    private JLabel legendSeniorLabel;
    private JLabel legendUnavailableLabel;
    private JButton clearSelectionButton;

    private JPanel rightDrawerPanel;
    private JLabel selectedSeatsTitle;
    private JScrollPane selectedSeatsScrollPane;
    private JPanel selectedSeatsPanel;

    private JPanel addOnsCardPanel;
    private JLabel addOnsTitle;
    private JCheckBox popcornCheckBox;
    private JCheckBox drinkCheckBox;
    private JCheckBox snackCheckBox;

    private JPanel totalCardPanel;
    private JLabel totalTitle;
    private JLabel totalLabel;
    private JLabel savingsLabel;
    private JButton bookButton;

    private static final Color AVAILABLE_COLOR = new Color(46, 204, 113);
    private static final Color SELECTED_COLOR = new Color(241, 196, 15);
    private static final Color UNAVAILABLE_COLOR = new Color(231, 76, 60);
    private static final Color SENIOR_COLOR = new Color(52, 152, 219);

    // =========================
    // CONSTRUCTORS
    // =========================

    // Original constructor retained for compatibility
    public SeatSelectionPanel(
            BookingService bookingService,
            Schedule schedule,
            Runnable bookingCompleteCallback,
            Runnable backCallback) {
        this(bookingService, schedule, "Unknown", bookingCompleteCallback, backCallback);
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

        setLayout(new BorderLayout());
        if (rootPanel != null) {
            add(rootPanel, BorderLayout.CENTER);
        }

        configureBoundComponents();
        setupListeners();
        refreshSeats();
        updateSelectedSeatsPanel();
        updateTotal();
    }

    // =========================
    // CONFIGURE BOUND COMPONENTS
    // =========================

    private void configureBoundComponents() {
        showtimeInfoLabel.setText(
                schedule.getMovie().getTitle()
                        + "  •  " + schedule.getDate()
                        + "  •  " + schedule.getTime()
                        + "  •  ₱" + String.format("%.2f", schedule.getTicketPrice()) + "/seat"
        );
    }

    private void setupListeners() {
        backButton.addActionListener(e -> {
            if (backCallback != null) {
                backCallback.run();
            }
        });

        clearSelectionButton.addActionListener(e -> {
            selectedSeats.clear();
            seniorCitizenSeats.clear();
            refreshSeats();
            updateSelectedSeatsPanel();
            updateTotal();
        });

        popcornCheckBox.addActionListener(e -> updateTotal());
        drinkCheckBox.addActionListener(e -> updateTotal());
        snackCheckBox.addActionListener(e -> updateTotal());

        bookButton.addActionListener(e -> handleBooking());
    }

    // =========================
    // SEAT DISPLAY
    // =========================

    private void refreshSeats() {
        seatsPanel.removeAll();

        ArrayList<Seat> seats = schedule.getSeats();
        int cols = Math.max(5, (int) Math.ceil(Math.sqrt(seats.size())));
        seatsPanel.setLayout(new GridLayout(0, cols, 10, 10));

        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatNumber());
            seatButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            seatButton.setFocusPainted(false);

            if (!seat.isAvailable()) {
                seatButton.setBackground(UNAVAILABLE_COLOR);
                seatButton.setForeground(Color.WHITE);
                seatButton.setEnabled(false);
            } else if (seniorCitizenSeats.contains(seat)) {
                seatButton.setBackground(SENIOR_COLOR);
                seatButton.setForeground(Color.WHITE);
            } else if (selectedSeats.contains(seat)) {
                seatButton.setBackground(SELECTED_COLOR);
                seatButton.setForeground(Color.BLACK);
            } else {
                seatButton.setBackground(AVAILABLE_COLOR);
                seatButton.setForeground(Color.WHITE);
            }

            seatButton.setOpaque(true);
            seatButton.setBorderPainted(false);

            seatButton.addActionListener(e -> toggleSeatSelection(seat));
            seatsPanel.add(seatButton);
        }

        seatCounterBadge.setText(
                selectedSeats.size() + (selectedSeats.size() == 1 ? " SEAT SELECTED" : " SEATS SELECTED")
        );

        seatsPanel.revalidate();
        seatsPanel.repaint();
    }

    // =========================
    // SEAT SELECTION
    // =========================

    private void toggleSeatSelection(Seat seat) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            seniorCitizenSeats.remove(seat);
        } else if (seat.isAvailable()) {
            selectedSeats.add(seat);
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
            selectedSeatsPanel.setLayout(new GridLayout(1, 1));
            JLabel emptyLabel = new JLabel("No seats selected yet", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            emptyLabel.setForeground(new Color(0x64748B));
            selectedSeatsPanel.add(emptyLabel);
        } else {
            selectedSeatsPanel.setLayout(new GridLayout(selectedSeats.size(), 1, 0, 4));
            for (Seat seat : selectedSeats) {
                JPanel seatRow = new JPanel(new BorderLayout(8, 0));
                seatRow.setBackground(new Color(0xF8FAFC));
                seatRow.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

                JLabel seatLabel = new JLabel("Seat " + seat.getSeatNumber());
                seatLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                seatLabel.setForeground(new Color(0x0F172A));

                JCheckBox seniorCheckBox = new JCheckBox("Senior (20% off)");
                seniorCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
                seniorCheckBox.setBackground(new Color(0xF8FAFC));
                seniorCheckBox.setFocusPainted(false);
                seniorCheckBox.setSelected(seniorCitizenSeats.contains(seat));

                seniorCheckBox.addActionListener(e -> {
                    if (seniorCheckBox.isSelected()) {
                        if (!seniorCitizenSeats.contains(seat)) {
                            seniorCitizenSeats.add(seat);
                        }
                    } else {
                        seniorCitizenSeats.remove(seat);
                    }
                    refreshSeats();
                    updateTotal();
                });

                seatRow.add(seatLabel, BorderLayout.WEST);
                seatRow.add(seniorCheckBox, BorderLayout.EAST);
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
        double discountTotal = 0;
        double ticketPrice = schedule.getTicketPrice();

        for (Seat seat : selectedSeats) {
            if (seniorCitizenSeats.contains(seat)) {
                double discount = ticketPrice * 0.20;
                discountTotal += discount;
                ticketCost += (ticketPrice - discount);
            } else {
                ticketCost += ticketPrice;
            }
        }

        BookingCost bookingCost = new BaseBookingCost(ticketCost);

        if (popcornCheckBox.isSelected()) {
            bookingCost = new PopcornComboDecorator(bookingCost);
        }
        if (drinkCheckBox.isSelected()) {
            bookingCost = new DrinkDecorator(bookingCost);
        }
        if (snackCheckBox.isSelected()) {
            bookingCost = new SnackDecorator(bookingCost);
        }

        savingsLabel.setText("Senior discount: ₱" + String.format("%.2f", discountTotal));
        totalLabel.setText(String.format("₱%.2f", bookingCost.getCost()));
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
            Map<String, TicketType> ticketTypes = new HashMap<>();
            for (Seat seat : seniorCitizenSeats) {
                ticketTypes.put(seat.getSeatNumber(), TicketType.SENIOR);
            }

            Booking booking = bookingService.createBooking(
                    schedule,
                    selectedSeats,
                    ticketTypes,
                    popcornCheckBox.isSelected(),
                    drinkCheckBox.isSelected(),
                    snackCheckBox.isSelected(),
                    username
            );

            String confirmationMessage =
                    "Booking Successful!\n\n"
                            + "Booking ID: " + booking.getBookingID()
                            + "\nMovie: " + schedule.getMovie().getTitle()
                            + "\nSchedule: " + schedule.getDate() + " " + schedule.getTime()
                            + "\nSeats: " + booking.getSelectedSeats()
                            + "\n\nTicket Total: ₱" + String.format("%.2f", booking.getTotalAmount())
                            + "\nDiscount: ₱" + String.format("%.2f", booking.getDiscountAmount())
                            + "\nAdd-ons: " + booking.getAddOns()
                            + "\nAdd-on Amount: ₱" + String.format("%.2f", booking.getAddOnAmount())
                            + "\n\nFinal Amount: ₱" + String.format("%.2f", booking.getFinalAmount());

            JOptionPane.showMessageDialog(
                    this,
                    confirmationMessage,
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            selectedSeats.clear();
            seniorCitizenSeats.clear();
            popcornCheckBox.setSelected(false);
            drinkCheckBox.setSelected(false);
            snackCheckBox.setSelected(false);

            refreshSeats();
            updateSelectedSeatsPanel();
            updateTotal();

            if (bookingCompleteCallback != null) {
                bookingCompleteCallback.run();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed:\n" + ex.getMessage(),
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE
            );
            refreshSeats();
        }
    }
}