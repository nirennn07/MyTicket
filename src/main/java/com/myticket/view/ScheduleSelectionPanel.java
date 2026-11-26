package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ScheduleSelectionPanel extends JPanel {

    private final BookingService bookingService;
    private final Movie movie;
    private final Consumer<Schedule> onScheduleSelected;
    private final Runnable onBack;

    // Bound automatically from ScheduleSelectionPanel.form
    private JPanel rootPanel;
    private JPanel headerCardPanel;
    private JButton backButton;
    private JPanel titleBlockPanel;
    private JLabel titleLabel;
    private JLabel movieLabel;
    private JLabel loungeTagLabel;

    private JPanel mainSplitPanel;
    private JPanel scheduleListCardPanel;
    private JScrollPane scheduleScrollPane;
    private JList<Schedule> scheduleList;

    private JPanel detailsCardPanel;
    private JPanel spotlightStripPanel;
    private JLabel previewBadgeLabel;
    private JLabel priceQuickLabel;
    private JScrollPane detailsScrollPane;
    private JTextArea scheduleDetailsArea;
    private JButton selectScheduleButton;

    private DefaultListModel<Schedule> scheduleListModel;

    public ScheduleSelectionPanel(
            BookingService bookingService,
            Movie movie,
            Consumer<Schedule> onScheduleSelected,
            Runnable onBack) {

        this.bookingService = bookingService;
        this.movie = movie;
        this.onScheduleSelected = onScheduleSelected;
        this.onBack = onBack;

        setLayout(new BorderLayout());
        if (rootPanel != null) {
            add(rootPanel, BorderLayout.CENTER);
        }

        configureBoundComponents();
        setupListeners();
        refreshSchedules();
    }

    private void configureBoundComponents() {
        movieLabel.setText(movie.getTitle());

        scheduleListModel = new DefaultListModel<>();
        scheduleList.setModel(scheduleListModel);
        scheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleList.setFixedCellHeight(38);
    }

    private void setupListeners() {
        scheduleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Schedule selectedSchedule = scheduleList.getSelectedValue();
                displayScheduleDetails(selectedSchedule);
            }
        });

        selectScheduleButton.addActionListener(e -> {
            Schedule selectedSchedule = scheduleList.getSelectedValue();

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
                onScheduleSelected.accept(selectedSchedule);
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

    private void displayScheduleDetails(Schedule schedule) {
        if (schedule == null) {
            scheduleDetailsArea.setText("");
            priceQuickLabel.setText("Pick a time slot");
            return;
        }

        priceQuickLabel.setText(
                "₱" + String.format("%.2f", schedule.getTicketPrice())
                        + "  •  " + schedule.getAvailableSeats().size() + " seats left"
        );

        scheduleDetailsArea.setText(
                "Movie: " + schedule.getMovie().getTitle() + "\n\n"
                        + "Schedule ID: " + schedule.getScheduleID() + "\n\n"
                        + "Date: " + schedule.getDate() + "\n\n"
                        + "Time: " + schedule.getTime() + "\n\n"
                        + "Ticket Price: ₱" + String.format("%.2f", schedule.getTicketPrice()) + "\n\n"
                        + "Available Seats: " + schedule.getAvailableSeats().size()
        );
        scheduleDetailsArea.setCaretPosition(0);
    }

    // ==============================
    // REFRESH SCHEDULES
    // ==============================

    public void refreshSchedules() {
        try {
            scheduleListModel.clear();

            ArrayList<Schedule> schedules = bookingService.getSchedulesForMovie(
                    movie.getMovieID()
            );

            for (Schedule schedule : schedules) {
                scheduleListModel.addElement(schedule);
            }

            scheduleDetailsArea.setText("");
            priceQuickLabel.setText("Pick a time slot");

            if (schedules.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No schedules are currently available for this movie.",
                        "No Schedules",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load schedules.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}