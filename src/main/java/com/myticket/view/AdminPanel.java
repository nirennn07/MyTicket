
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

    private BookingService bookingService;

    // Movie fields
    private JTextField titleField;
    private JTextField genreField;
    private JTextField durationField;
    private JTextField ratingField;
    private JTextArea descriptionArea;

    private DefaultListModel<Movie> movieListModel;
    private JList<Movie> movieList;

    // Schedule fields
    private JComboBox<Movie> scheduleMovieCombo;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField priceField;

    private DefaultListModel<Schedule> scheduleListModel;
    private JList<Schedule> scheduleList;

    public AdminPanel(BookingService bookingService) {

        this.bookingService = bookingService;

        setLayout(new BorderLayout(10, 10));

        initializeComponents();

        refreshMovies();

    }

    private void initializeComponents() {

        // ==============================
        // TITLE
        // ==============================

        JLabel titleLabel = new JLabel(
                "Admin Panel",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        // ==============================
        // TABS
        // ==============================

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(
                "Movie Management",
                createMovieManagementPanel()
        );

        tabbedPane.addTab(
                "Schedule Management",
                createScheduleManagementPanel()
        );

        add(tabbedPane, BorderLayout.CENTER);

    }

    // ==============================
    // MOVIE MANAGEMENT PANEL
    // ==============================

    private JPanel createMovieManagementPanel() {

        JPanel mainPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        // ==============================
        // INPUT FORM
        // ==============================

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Add Movie"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel("Title:"),
                gbc
        );

        titleField = new JTextField(20);

        gbc.gridx = 1;

        formPanel.add(
                titleField,
                gbc
        );

        // Genre

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                new JLabel("Genre:"),
                gbc
        );

        genreField = new JTextField(20);

        gbc.gridx = 1;

        formPanel.add(
                genreField,
                gbc
        );

        // Duration

        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                new JLabel("Duration (minutes):"),
                gbc
        );

        durationField = new JTextField(20);

        gbc.gridx = 1;

        formPanel.add(
                durationField,
                gbc
        );

        // Rating

        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                new JLabel("Rating:"),
                gbc
        );

        ratingField = new JTextField(20);

        gbc.gridx = 1;

        formPanel.add(
                ratingField,
                gbc
        );

        // Description

        gbc.gridx = 0;
        gbc.gridy = 4;

        formPanel.add(
                new JLabel("Description:"),
                gbc
        );

        descriptionArea = new JTextArea(
                4,
                20
        );

        descriptionArea.setLineWrap(true);

        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScrollPane =
                new JScrollPane(descriptionArea);

        gbc.gridx = 1;

        gbc.fill = GridBagConstraints.BOTH;

        formPanel.add(
                descriptionScrollPane,
                gbc
        );

        // ==============================
        // BUTTONS
        // ==============================

        JButton addMovieButton = new JButton(
                "Add Movie"
        );

        JButton removeMovieButton = new JButton(
                "Remove Selected Movie"
        );

        JButton clearButton = new JButton(
                "Clear Fields"
        );

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(addMovieButton);

        buttonPanel.add(removeMovieButton);

        buttonPanel.add(clearButton);

        // ==============================
        // MOVIE LIST
        // ==============================

        movieListModel = new DefaultListModel<>();

        movieList = new JList<>(
                movieListModel
        );

        movieList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane movieScrollPane = new JScrollPane(
                movieList
        );

        movieScrollPane.setPreferredSize(
                new Dimension(300, 300)
        );

        JPanel movieListPanel = new JPanel(
                new BorderLayout()
        );

        movieListPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Movie Catalog"
                )
        );

        movieListPanel.add(
                movieScrollPane,
                BorderLayout.CENTER
        );

        // ==============================
        // CENTER PANEL
        // ==============================

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                movieListPanel,
                BorderLayout.EAST
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // ==============================
        // BUTTON ACTIONS
        // ==============================

        addMovieButton.addActionListener(e -> {

            handleAddMovie();

        });

        removeMovieButton.addActionListener(e -> {

            handleRemoveMovie();

        });

        clearButton.addActionListener(e -> {

            clearFields();

        });

        return mainPanel;

    }

    // ==============================
    // SCHEDULE MANAGEMENT PANEL
    // ==============================

    private JPanel createScheduleManagementPanel() {

        JPanel mainPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        // ==============================
        // SCHEDULE FORM
        // ==============================

        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Create Movie Schedule"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Movie selection

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel("Movie:"),
                gbc
        );

        scheduleMovieCombo = new JComboBox<>();

        gbc.gridx = 1;

        formPanel.add(
                scheduleMovieCombo,
                gbc
        );

        // Date

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                new JLabel("Date (YYYY-MM-DD):"),
                gbc
        );

        dateField = new JTextField(15);

        gbc.gridx = 1;

        formPanel.add(
                dateField,
                gbc
        );

        // Time

        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                new JLabel("Time (HH:MM):"),
                gbc
        );

        timeField = new JTextField(15);

        gbc.gridx = 1;

        formPanel.add(
                timeField,
                gbc
        );

        // Price

        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                new JLabel("Ticket Price:"),
                gbc
        );

        priceField = new JTextField(15);

        gbc.gridx = 1;

        formPanel.add(
                priceField,
                gbc
        );

        // ==============================
        // CREATE BUTTON
        // ==============================

        JButton createScheduleButton = new JButton(
                "Create Schedule"
        );

        JButton clearScheduleButton = new JButton(
                "Clear Fields"
        );

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(createScheduleButton);

        buttonPanel.add(clearScheduleButton);

        gbc.gridx = 0;
        gbc.gridy = 4;

        gbc.gridwidth = 2;

        formPanel.add(
                buttonPanel,
                gbc
        );

        // ==============================
        // SCHEDULE LIST
        // ==============================

        scheduleListModel = new DefaultListModel<>();

        scheduleList = new JList<>(
                scheduleListModel
        );

        JScrollPane scheduleScrollPane = new JScrollPane(
                scheduleList
        );

        scheduleScrollPane.setPreferredSize(
                new Dimension(350, 300)
        );

        JPanel scheduleListPanel = new JPanel(
                new BorderLayout()
        );

        scheduleListPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Existing Schedules"
                )
        );

        scheduleListPanel.add(
                scheduleScrollPane,
                BorderLayout.CENTER
        );

        // ==============================
        // LAYOUT
        // ==============================

        mainPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                scheduleListPanel,
                BorderLayout.CENTER
        );

        // ==============================
        // BUTTON ACTIONS
        // ==============================

        createScheduleButton.addActionListener(e -> {

            handleCreateSchedule();

        });

        clearScheduleButton.addActionListener(e -> {

            clearScheduleFields();

        });

        // ==============================
        // MOVIE SELECTION
        // ==============================

        scheduleMovieCombo.addActionListener(e -> {

            refreshSchedules();

        });

        return mainPanel;

    }

    // ==============================
    // ADD MOVIE
    // ==============================

    private void handleAddMovie() {

        try {

            String title = titleField.getText().trim();

            String genre = genreField.getText().trim();

            String description =
                    descriptionArea.getText().trim();

            String rating = ratingField.getText().trim();

            String durationText =
                    durationField.getText().trim();

            if (durationText.isEmpty()) {

                throw new IllegalArgumentException(
                        "Duration cannot be empty."
                );

            }

            int duration = Integer.parseInt(
                    durationText
            );

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
                            + "Movie ID: "
                            + movie.getMovieID()
                            + "\n"
                            + "Title: "
                            + movie.getTitle(),
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
                    "Failed to add movie.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    // ==============================
    // REMOVE MOVIE
    // ==============================

    private void handleRemoveMovie() {

        Movie selectedMovie =
                movieList.getSelectedValue();

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
                "Are you sure you want to remove "
                        + selectedMovie.getTitle()
                        + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {

            return;

        }

        try {

            bookingService.removeMovie(
                    selectedMovie.getMovieID()
            );

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
                    "Failed to remove movie.\n"
                            + e.getMessage(),
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

            Movie selectedMovie =
                    (Movie) scheduleMovieCombo.getSelectedItem();

            if (selectedMovie == null) {

                throw new IllegalArgumentException(
                        "Please select a movie."
                );

            }

            String dateText =
                    dateField.getText().trim();

            String timeText =
                    timeField.getText().trim();

            String priceText =
                    priceField.getText().trim();

            if (dateText.isEmpty()) {

                throw new IllegalArgumentException(
                        "Date cannot be empty."
                );

            }

            if (timeText.isEmpty()) {

                throw new IllegalArgumentException(
                        "Time cannot be empty."
                );

            }

            if (priceText.isEmpty()) {

                throw new IllegalArgumentException(
                        "Ticket price cannot be empty."
                );

            }

            // Parse date

            LocalDate date = LocalDate.parse(
                    dateText
            );

            // Parse time

            LocalTime time = LocalTime.parse(
                    timeText
            );

            // Parse ticket price

            double ticketPrice = Double.parseDouble(
                    priceText
            );

            // Create schedule through BookingService

            Schedule schedule =
                    bookingService.createSchedule(
                            selectedMovie.getMovieID(),
                            date,
                            time,
                            ticketPrice
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Schedule created successfully!\n\n"
                            + "Schedule ID: "
                            + schedule.getScheduleID()
                            + "\n"
                            + "Movie: "
                            + selectedMovie.getTitle()
                            + "\n"
                            + "Date: "
                            + schedule.getDate()
                            + "\n"
                            + "Time: "
                            + schedule.getTime()
                            + "\n"
                            + "Ticket Price: ₱"
                            + String.format(
                            "%.2f",
                            schedule.getTicketPrice()
                    ),
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
                    "Invalid schedule information.\n"
                            + e.getMessage(),
                    "Invalid Schedule",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to create schedule.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    // ==============================
    // CLEAR MOVIE FIELDS
    // ==============================

    private void clearFields() {

        titleField.setText("");

        genreField.setText("");

        durationField.setText("");

        ratingField.setText("");

        descriptionArea.setText("");

    }

    // ==============================
    // CLEAR SCHEDULE FIELDS
    // ==============================

    private void clearScheduleFields() {

        dateField.setText("");

        timeField.setText("");

        priceField.setText("");

    }

    // ==============================
    // REFRESH MOVIES
    // ==============================

    public void refreshMovies() {

        try {

            movieListModel.clear();

            scheduleMovieCombo.removeAllItems();

            ArrayList<Movie> movies =
                    bookingService.getMovies();

            for (Movie movie : movies) {

                movieListModel.addElement(movie);

                scheduleMovieCombo.addItem(movie);

            }

            refreshSchedules();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to refresh movie list.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    // ==============================
    // REFRESH SCHEDULES
    // ==============================

    private void refreshSchedules() {

        try {

            scheduleListModel.clear();

            Movie selectedMovie =
                    (Movie) scheduleMovieCombo.getSelectedItem();

            if (selectedMovie == null) {

                return;

            }

            ArrayList<Schedule> schedules =
                    bookingService.getSchedulesForMovie(
                            selectedMovie.getMovieID()
                    );

            for (Schedule schedule : schedules) {

                scheduleListModel.addElement(schedule);

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to refresh schedules.\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

}
