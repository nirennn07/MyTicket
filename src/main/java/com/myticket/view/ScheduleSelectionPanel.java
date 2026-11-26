package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.service.BookingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ScheduleSelectionPanel extends JPanel {

    private BookingService bookingService;

    private Movie movie;

    private Consumer<Schedule> onScheduleSelected;

    private Runnable onBack;

    private DefaultListModel<Schedule> scheduleListModel;

    private JList<Schedule> scheduleList;

    private JTextArea scheduleDetailsArea;

    public ScheduleSelectionPanel(
            BookingService bookingService,
            Movie movie,
            Consumer<Schedule> onScheduleSelected,
            Runnable onBack) {

        this.bookingService = bookingService;

        this.movie = movie;

        this.onScheduleSelected = onScheduleSelected;

        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));
        setBackground(BlueTheme.APP_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        initializeComponents();

        refreshSchedules();

    }

    private void initializeComponents() {

        // ==============================
        // HEADER
        // ==============================

        JPanel headerPanel = new JPanel(
                new BorderLayout(10, 0)
        );
        headerPanel.setBackground(BlueTheme.CARD_BG);
        headerPanel.setBorder(BlueTheme.createCardBorder());

        JLabel titleLabel = new JLabel(
                "Select a Schedule",
                SwingConstants.CENTER
        );

        titleLabel.setFont(BlueTheme.FONT_TITLE);
        titleLabel.setForeground(BlueTheme.NAVY);

        JLabel movieLabel = new JLabel(
                movie.getTitle(),
                SwingConstants.CENTER
        );

        movieLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        movieLabel.setForeground(BlueTheme.ROYAL_BLUE);

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1)
        );
        titlePanel.setBackground(BlueTheme.CARD_BG);

        titlePanel.add(titleLabel);

        titlePanel.add(movieLabel);

        JButton backButton = new JButton(
                "Back to Movies"
        );
        BlueTheme.styleSecondaryButton(backButton);

        headerPanel.add(
                backButton,
                BorderLayout.WEST
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.CENTER
        );

        add(headerPanel, BorderLayout.NORTH);

        // ==============================
        // SCHEDULE LIST
        // ==============================

        scheduleListModel = new DefaultListModel<>();

        scheduleList = new JList<>(
                scheduleListModel
        );
        BlueTheme.styleList(scheduleList);

        scheduleList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        scheduleList.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        JScrollPane scheduleScrollPane = new JScrollPane(
                scheduleList
        );
        BlueTheme.styleScrollPane(scheduleScrollPane);

        scheduleScrollPane.setPreferredSize(
                new Dimension(300, 400)
        );

        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.setBackground(BlueTheme.CARD_BG);
        listContainer.setBorder(BlueTheme.createBlueTitledBorder("Schedules"));
        listContainer.add(scheduleScrollPane, BorderLayout.CENTER);

        // ==============================
        // SCHEDULE DETAILS
        // ==============================

        scheduleDetailsArea = new JTextArea();
        BlueTheme.styleTextArea(scheduleDetailsArea);

        scheduleDetailsArea.setEditable(false);

        scheduleDetailsArea.setLineWrap(true);

        scheduleDetailsArea.setWrapStyleWord(true);

        scheduleDetailsArea.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        scheduleDetailsArea.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        JScrollPane detailsScrollPane = new JScrollPane(
                scheduleDetailsArea
        );
        BlueTheme.styleScrollPane(detailsScrollPane);

        JPanel detailsContainer = new JPanel(new BorderLayout());
        detailsContainer.setBackground(BlueTheme.CARD_BG);
        detailsContainer.setBorder(BlueTheme.createBlueTitledBorder("Schedule Details"));
        detailsContainer.add(detailsScrollPane, BorderLayout.CENTER);

        // ==============================
        // CENTER PANEL
        // ==============================

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );
        centerPanel.setBackground(BlueTheme.APP_BG);

        centerPanel.add(
                listContainer,
                BorderLayout.WEST
        );

        centerPanel.add(
                detailsContainer,
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);

        // ==============================
        // BUTTONS
        // ==============================

        JButton selectScheduleButton = new JButton(
                "Select Schedule"
        );
        BlueTheme.stylePrimaryButton(selectScheduleButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BlueTheme.APP_BG);

        buttonPanel.add(selectScheduleButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ==============================
        // ACTIONS
        // ==============================

        scheduleList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                Schedule selectedSchedule =
                        scheduleList.getSelectedValue();

                displayScheduleDetails(selectedSchedule);

            }

        });

        selectScheduleButton.addActionListener(e -> {

            Schedule selectedSchedule =
                    scheduleList.getSelectedValue();

            if (selectedSchedule == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a schedule.",
                        "No Schedule Selected",
                        JOptionPane.WARNING_MESSAGE
                );

                return;

            }

            if (onScheduleSelected != null) {

                onScheduleSelected.accept(
                        selectedSchedule
                );

            }

        });

        backButton.addActionListener(e -> {

            if (onBack != null) {

                onBack.run();

            }

        });

    }

    // ==============================
    // DISPLAY SCHEDULE DETAILS
    // ==============================

    private void displayScheduleDetails(
            Schedule schedule) {

        if (schedule == null) {

            scheduleDetailsArea.setText("");

            return;

        }

        scheduleDetailsArea.setText(
                "Movie: "
                        + schedule.getMovie().getTitle()
                        + "\n\n"

                        + "Schedule ID: "
                        + schedule.getScheduleID()
                        + "\n\n"

                        + "Date: "
                        + schedule.getDate()
                        + "\n\n"

                        + "Time: "
                        + schedule.getTime()
                        + "\n\n"

                        + "Ticket Price: ₱"
                        + String.format(
                        "%.2f",
                        schedule.getTicketPrice()
                )
                        + "\n\n"

                        + "Available Seats: "
                        + schedule.getAvailableSeats().size()
        );

    }

    // ==============================
    // REFRESH SCHEDULES
    // ==============================

    public void refreshSchedules() {

        try {

            scheduleListModel.clear();

            ArrayList<Schedule> schedules =
                    bookingService.getSchedulesForMovie(
                            movie.getMovieID()
                    );

            for (Schedule schedule : schedules) {

                scheduleListModel.addElement(schedule);

            }

            scheduleDetailsArea.setText("");

            if (schedules.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No schedules are currently available "
                                + "for this movie.",
                        "No Schedules",
                        JOptionPane.INFORMATION_MESSAGE
                );

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load schedules.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

}