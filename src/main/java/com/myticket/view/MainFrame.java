package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final BookingService bookingService;
    private final boolean admin;
    private final String username;

    // Bound automatically from MainFrame.form (Left Sidebar + Featured Banner)
    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JLabel brandTitleLabel;
    private JLabel userBadgeLabel;
    private JLabel menuSectionLabel;
    private JButton moviesButton;
    private JButton ticketsButton;
    private JButton adminButton;
    private JButton logoutButton;

    private JPanel rightWorkspacePanel;
    private JPanel headerBarPanel;
    private JLabel pageTitleLabel;
    private JLabel pageSubtitleLabel;

    private JPanel featuredBannerPanel;
    private JPanel proxyPosterCard;
    private JLabel proxyPosterLabel;
    private JPanel spotlightInfoPanel;
    private JLabel spotlightTagLabel;
    private JLabel spotlightTitleLabel;
    private JLabel spotlightDescLabel;

    private JPanel contentPanel;

    private MoviePanel moviePanel;
    private TicketPanel ticketPanel;
    private AdminPanel adminPanel;

    private ScheduleSelectionPanel scheduleSelectionPanel;
    private SeatSelectionPanel seatSelectionPanel;

    // ==============================
    // CONSTRUCTORS
    // ==============================

    public MainFrame(BookingService bookingService, boolean admin) {
        this(bookingService, admin, "Unknown");
    }

    public MainFrame(BookingService bookingService, boolean admin, String username) {
        this.bookingService = bookingService;
        this.admin = admin;
        this.username = username;

        setTitle("MyTicket");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        initializeViews();
    }

    // ==============================
    // INITIALIZE VIEWS & ACTIONS
    // ==============================

    private void initializeViews() {
        userBadgeLabel.setText("●  " + username + (admin ? " (Admin)" : ""));
        adminButton.setVisible(admin);

        moviePanel = new MoviePanel(bookingService, this::showScheduleSelection);
        ticketPanel = new TicketPanel(bookingService, username);
        adminPanel = new AdminPanel(bookingService);

        contentPanel.add(moviePanel, "MOVIES");
        contentPanel.add(ticketPanel, "TICKETS");
        contentPanel.add(adminPanel, "ADMIN");

        moviesButton.addActionListener(e -> {
            moviePanel.refreshMovies();
            showCard("MOVIES", "Now Showing", true, moviesButton);
        });

        ticketsButton.addActionListener(e -> {
            ticketPanel.refreshTickets();
            showCard("TICKETS", "My Tickets", false, ticketsButton);
        });

        adminButton.addActionListener(e -> {
            adminPanel.refreshMovies();
            showCard("ADMIN", "Admin Dashboard", false, adminButton);
        });

        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                LoginFrame loginFrame = new LoginFrame(bookingService);
                loginFrame.setVisible(true);
                dispose();
            }
        });

        showCard("MOVIES", "Now Showing", true, moviesButton);
    }

    private void showCard(String cardName, String title, boolean showBanner, JButton activeButton) {
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, cardName);

        pageTitleLabel.setText(title);
        featuredBannerPanel.setVisible(showBanner);

        Color activeBg = new Color(0x2563EB);
        Color inactiveBg = new Color(0x1E293B);
        Color activeFg = Color.WHITE;
        Color inactiveFg = new Color(0xE2E8F0);

        JButton[] navButtons = {moviesButton, ticketsButton, adminButton};
        for (JButton btn : navButtons) {
            boolean isActive = (btn == activeButton);
            btn.setBackground(isActive ? activeBg : inactiveBg);
            btn.setForeground(isActive ? activeFg : inactiveFg);
            btn.setFont(btn.getFont().deriveFont(isActive ? Font.BOLD : Font.PLAIN));
        }
    }

    // ==============================
    // SCHEDULE SELECTION
    // ==============================

    public void showScheduleSelection(Movie movie) {
        if (scheduleSelectionPanel != null) {
            contentPanel.remove(scheduleSelectionPanel);
        }

        scheduleSelectionPanel = new ScheduleSelectionPanel(
                bookingService,
                movie,
                this::showSeatSelection,
                () -> {
                    moviePanel.refreshMovies();
                    showCard("MOVIES", "Now Showing", true, moviesButton);
                }
        );

        contentPanel.add(scheduleSelectionPanel, "SCHEDULE_SELECTION");
        showCard("SCHEDULE_SELECTION", "Select Schedule — " + movie.getTitle(), false, moviesButton);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ==============================
    // SEAT SELECTION
    // ==============================

    public void showSeatSelection(Schedule schedule) {
        if (seatSelectionPanel != null) {
            contentPanel.remove(seatSelectionPanel);
        }

        seatSelectionPanel = new SeatSelectionPanel(
                bookingService,
                schedule,
                username,
                () -> {
                    ticketPanel.refreshTickets();
                    showCard("TICKETS", "My Tickets", false, ticketsButton);
                },
                () -> showScheduleSelection(schedule.getMovie())
        );

        contentPanel.add(seatSelectionPanel, "SEAT_SELECTION");
        showCard("SEAT_SELECTION", "Choose Your Seats", false, moviesButton);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}