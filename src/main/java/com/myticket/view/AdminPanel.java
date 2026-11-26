package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AdminPanel extends JPanel {

    private final BookingService bookingService;

    // Bound automatically from AdminPanel.form
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JLabel headerTitleLabel;
    private JLabel headerSubtitleLabel;
    private JTabbedPane adminTabbedPane;

    // Movie Management bindings
    private JPanel movieTabPanel;
    private JPanel addMovieCardPanel;
    private JTextField titleField;
    private JTextField genreField;
    private JTextField durationField;
    private JTextField ratingField;
    private JScrollPane descriptionScrollPane;
    private JTextArea descriptionArea;
    private JButton addMovieButton;
    private JButton clearButton;

    private JPanel movieCatalogCardPanel;
    private JScrollPane movieScrollPane;
    private JList<Movie> movieList;
    private JButton removeMovieButton;

    // Schedule Management bindings
    private JPanel scheduleTabPanel;
    private JPanel createScheduleCardPanel;
    private JComboBox<Movie> scheduleMovieCombo;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField priceField;
    private JButton createScheduleButton;
    private JButton clearScheduleButton;

    private JPanel scheduleListCardPanel;
    private JScrollPane scheduleScrollPane;
    private JList<Schedule> scheduleList;

    private DefaultListModel<Movie> movieListModel;
    private DefaultListModel<Schedule> scheduleListModel;

    public AdminPanel(BookingService bookingService) {
        this.bookingService = bookingService;

        setLayout(new BorderLayout());
        add(rootPanel, BorderLayout.CENTER);

        configureBoundComponents();
        setupListeners();
        refreshMovies();
    }

    private void configureBoundComponents() {
        movieListModel = new DefaultListModel<>();
        movieList.setModel(movieListModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setFixedCellHeight(30);

        scheduleListModel = new DefaultListModel<>();
        scheduleList.setModel(scheduleListModel);
        scheduleList.setFixedCellHeight(28);
    }

    private void setupListeners() {
        addMovieButton.addActionListener(e -> handleAddMovie());
        removeMovieButton.addActionListener(e -> handleRemoveMovie());
        clearButton.addActionListener(e -> clearFields());

        createScheduleButton.addActionListener(e -> handleCreateSchedule());
        clearScheduleButton.addActionListener(e -> clearScheduleFields());
        scheduleMovieCombo.addActionListener(e -> refreshSchedules());
    }

    // ==============================
    // ADD MOVIE
    // ==============================

    private void handleAddMovie() {
        try {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String description = descriptionArea.getText().trim();
            String rating = ratingField.getText().trim();
            String durationText = durationField.getText().trim();

            if (durationText.isEmpty()) {
                throw new IllegalArgumentException("Duration cannot be empty.");
            }

            int duration = Integer.parseInt(durationText);

            Movie movie = bookingService.addMovie(
                    title,
                    genre,
                    description,
                    duration,
                    rating
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Movie added successfully!\n\n"
                            + "Movie ID: " + movie.getMovieID()
                            + "\nTitle: " + movie.getTitle(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            refreshMovies();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Duration must be a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Movie",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add movie.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==============================
    // REMOVE MOVIE
    // ==============================

    private void handleRemoveMovie() {
        Movie selectedMovie = movieList.getSelectedValue();

        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a movie to remove.",
                    "No Movie Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove " + selectedMovie.getTitle() + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            bookingService.removeMovie(selectedMovie.getMovieID());

            JOptionPane.showMessageDialog(
                    this,
                    "Movie removed successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshMovies();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to remove movie.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==============================
    // CREATE SCHEDULE
    // ==============================

    private void handleCreateSchedule() {
        try {
            Movie selectedMovie = (Movie) scheduleMovieCombo.getSelectedItem();

            if (selectedMovie == null) {
                throw new IllegalArgumentException("Please select a movie.");
            }

            String dateText = dateField.getText().trim();
            String timeText = timeField.getText().trim();
            String priceText = priceField.getText().trim();

            if (dateText.isEmpty()) {
                throw new IllegalArgumentException("Date cannot be empty.");
            }

            if (timeText.isEmpty()) {
                throw new IllegalArgumentException("Time cannot be empty.");
            }

            if (priceText.isEmpty()) {
                throw new IllegalArgumentException("Ticket price cannot be empty.");
            }

            LocalDate date = LocalDate.parse(dateText);
            LocalTime time = LocalTime.parse(timeText);
            double ticketPrice = Double.parseDouble(priceText);

            Schedule schedule = bookingService.createSchedule(
                    selectedMovie.getMovieID(),
                    date,
                    time,
                    ticketPrice
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Schedule created successfully!\n\n"
                            + "Schedule ID: " + schedule.getScheduleID()
                            + "\nMovie: " + selectedMovie.getTitle()
                            + "\nDate: " + schedule.getDate()
                            + "\nTime: " + schedule.getTime()
                            + "\nTicket Price: ₱" + String.format("%.2f", schedule.getTicketPrice()),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearScheduleFields();
            refreshSchedules();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ticket price must be a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid schedule information.\n" + e.getMessage(),
                    "Invalid Schedule",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to create schedule.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==============================
    // CLEAR FIELDS
    // ==============================

    private void clearFields() {
        titleField.setText("");
        genreField.setText("");
        durationField.setText("");
        ratingField.setText("");
        descriptionArea.setText("");
    }

    private void clearScheduleFields() {
        dateField.setText("");
        timeField.setText("");
        priceField.setText("");
    }

    // ==============================
    // REFRESH MOVIES & SCHEDULES
    // ==============================

    public void refreshMovies() {
        try {
            movieListModel.clear();
            scheduleMovieCombo.removeAllItems();

            ArrayList<Movie> movies = bookingService.getMovies();

            for (Movie movie : movies) {
                movieListModel.addElement(movie);
                scheduleMovieCombo.addItem(movie);
            }

            refreshSchedules();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to refresh movie list.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshSchedules() {
        try {
            scheduleListModel.clear();

            Movie selectedMovie = (Movie) scheduleMovieCombo.getSelectedItem();
            if (selectedMovie == null) {
                return;
            }

            ArrayList<Schedule> schedules = bookingService.getSchedulesForMovie(
                    selectedMovie.getMovieID()
            );

            for (Schedule schedule : schedules) {
                scheduleListModel.addElement(schedule);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to refresh schedules.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}