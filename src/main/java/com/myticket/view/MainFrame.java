package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.service.BookingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private BookingService bookingService;

    private boolean admin;
    private String username;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private MoviePanel moviePanel;
    private TicketPanel ticketPanel;
    private AdminPanel adminPanel;

    private ScheduleSelectionPanel scheduleSelectionPanel;
    private SeatSelectionPanel seatSelectionPanel;

    // ==============================
    // CONSTRUCTORS
    // ==============================

    // Original constructor retained for compatibility
    public MainFrame(
            BookingService bookingService,
            boolean admin) {

        this(
                bookingService,
                admin,
                "Unknown"
        );
    }

    // New constructor with username
    public MainFrame(
            BookingService bookingService,
            boolean admin,
            String username) {

        this.bookingService = bookingService;
        this.admin = admin;
        this.username = username;

        setTitle("MyTicket");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    // ==============================
    // INITIALIZE COMPONENTS
    // ==============================

    private void initializeComponents() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(BlueTheme.APP_BG);

        // ==============================
        // NAVIGATION PANEL
        // ==============================

        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(BlueTheme.NAVY);
        navigationPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 0, 1, BlueTheme.DEEP_NAVY),
                        new EmptyBorder(15, 10, 15, 10)
                )
        );

        navigationPanel.setLayout(
                new BoxLayout(
                        navigationPanel,
                        BoxLayout.Y_AXIS
                )
        );

        navigationPanel.setPreferredSize(
                new Dimension(180, 600)
        );

        JButton moviesButton =
                new JButton("Movies");
        BlueTheme.styleNavButton(moviesButton, false);

        JButton ticketsButton =
                new JButton("My Tickets");
        BlueTheme.styleNavButton(ticketsButton, false);

        JButton logoutButton =
                new JButton("Logout");
        BlueTheme.styleNavButton(logoutButton, true);

        moviesButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        ticketsButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        logoutButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        navigationPanel.add(moviesButton);

        navigationPanel.add(
                Box.createVerticalStrut(10)
        );

        navigationPanel.add(ticketsButton);

        navigationPanel.add(
                Box.createVerticalStrut(10)
        );

        // ==============================
        // ADMIN BUTTON
        // ==============================

        if (admin) {

            JButton adminButton =
                    new JButton("Admin Panel");
            BlueTheme.styleNavButton(adminButton, false);

            adminButton.setMaximumSize(
                    new Dimension(
                            Integer.MAX_VALUE,
                            40
                    )
            );

            navigationPanel.add(adminButton);

            navigationPanel.add(
                    Box.createVerticalStrut(10)
            );

            adminButton.addActionListener(e -> {

                cardLayout.show(
                        contentPanel,
                        "ADMIN"
                );

                adminPanel.refreshMovies();
            });
        }

        navigationPanel.add(
                Box.createVerticalGlue()
        );

        navigationPanel.add(logoutButton);

        add(
                navigationPanel,
                BorderLayout.WEST
        );

        // ==============================
        // CONTENT PANEL
        // ==============================

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BlueTheme.APP_BG);

        // ==============================
        // MOVIE PANEL
        // ==============================

        moviePanel = new MoviePanel(
                bookingService,
                this::showScheduleSelection
        );

        // ==============================
        // TICKET PANEL
        // ==============================

        ticketPanel = new TicketPanel(
                bookingService,
                username
        );

        // ==============================
        // ADMIN PANEL
        // ==============================

        adminPanel = new AdminPanel(
                bookingService
        );

        // ==============================
        // ADD PANELS
        // ==============================

        contentPanel.add(
                moviePanel,
                "MOVIES"
        );

        contentPanel.add(
                ticketPanel,
                "TICKETS"
        );

        contentPanel.add(
                adminPanel,
                "ADMIN"
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // NAVIGATION ACTIONS
        // ==============================

        moviesButton.addActionListener(e -> {

            moviePanel.refreshMovies();

            cardLayout.show(
                    contentPanel,
                    "MOVIES"
            );
        });

        ticketsButton.addActionListener(e -> {

            ticketPanel.refreshTickets();

            cardLayout.show(
                    contentPanel,
                    "TICKETS"
            );
        });

        // ==============================
        // LOGOUT
        // ==============================

        logoutButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to logout?",
                            "Logout",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice == JOptionPane.YES_OPTION) {

                LoginFrame loginFrame =
                        new LoginFrame(
                                bookingService
                        );

                loginFrame.setVisible(true);

                dispose();
            }
        });

        // ==============================
        // INITIAL SCREEN
        // ==============================

        cardLayout.show(
                contentPanel,
                "MOVIES"
        );
    }

    // ==============================
    // SCHEDULE SELECTION
    // ==============================

    public void showScheduleSelection(Movie movie) {

        // Remove previous schedule panel

        if (scheduleSelectionPanel != null) {

            contentPanel.remove(
                    scheduleSelectionPanel
            );
        }

        scheduleSelectionPanel =
                new ScheduleSelectionPanel(
                        bookingService,
                        movie,

                        // Schedule selected
                        selectedSchedule ->
                                showSeatSelection(
                                        selectedSchedule
                                ),

                        // Back button
                        () -> {

                            moviePanel.refreshMovies();

                            cardLayout.show(
                                    contentPanel,
                                    "MOVIES"
                            );
                        }
                );

        contentPanel.add(
                scheduleSelectionPanel,
                "SCHEDULE_SELECTION"
        );

        cardLayout.show(
                contentPanel,
                "SCHEDULE_SELECTION"
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==============================
    // SEAT SELECTION
    // ==============================

    public void showSeatSelection(Schedule schedule) {

        // Remove previous seat selection panel

        if (seatSelectionPanel != null) {

            contentPanel.remove(
                    seatSelectionPanel
            );
        }

        seatSelectionPanel =
                new SeatSelectionPanel(
                        bookingService,
                        schedule,
                        username,

                        // Booking completed
                        () -> {

                            ticketPanel.refreshTickets();

                            cardLayout.show(
                                    contentPanel,
                                    "TICKETS"
                            );
                        },

                        // Back to schedule selection
                        () -> showScheduleSelection(
                                schedule.getMovie()
                        )
                );

        contentPanel.add(
                seatSelectionPanel,
                "SEAT_SELECTION"
        );

        cardLayout.show(
                contentPanel,
                "SEAT_SELECTION"
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}