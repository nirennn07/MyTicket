package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.proxy.MoviePoster;
import com.myticket.proxy.MoviePosterProxy;
import com.myticket.service.BookingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MoviePanel extends JPanel {

    private BookingService bookingService;

    private JList<Movie> movieList;
    private DefaultListModel<Movie> movieListModel;

    private JTextArea movieDetails;
    private JLabel posterLabel;
    private JButton selectMovieButton;

    private Movie selectedMovie;

    private Consumer<Movie> onMovieSelected;

    private ArrayList<MoviePoster> moviePosters;

    public MoviePanel(
            BookingService bookingService,
            Consumer<Movie> onMovieSelected) {

        this.bookingService = bookingService;
        this.onMovieSelected = onMovieSelected;

        moviePosters = new ArrayList<>();

        initializeComponents();
        setupLayout();
        setupListeners();

        refreshMovies();
    }

    // Backward-compatible constructor
    public MoviePanel(BookingService bookingService) {
        this(bookingService, movie -> {
            JOptionPane.showMessageDialog(
                    null,
                    "Schedule selection will be implemented."
            );
        });
    }

    // =========================================================
    // INITIALIZATION
    // =========================================================

    private void initializeComponents() {

        movieListModel = new DefaultListModel<>();

        movieList = new JList<>(movieListModel);

        movieList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        movieList.setFont(new Font("Arial", Font.PLAIN, 16));

        movieList.setFixedCellHeight(40);

        movieDetails = new JTextArea();

        movieDetails.setEditable(false);
        movieDetails.setLineWrap(true);
        movieDetails.setWrapStyleWord(true);
        movieDetails.setFont(new Font("Arial", Font.PLAIN, 14));

        movieDetails.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        posterLabel = new JLabel(
                "Select a movie to view its poster",
                SwingConstants.CENTER
        );

        posterLabel.setPreferredSize(
                new Dimension(250, 350)
        );

        posterLabel.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        posterLabel.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        selectMovieButton = new JButton("Select Movie");

        selectMovieButton.setEnabled(false);
    }

    // =========================================================
    // LAYOUT
    // =========================================================

    private void setupLayout() {

        setLayout(new BorderLayout(15, 15));

        setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        // Movie list panel

        JPanel movieListPanel = new JPanel(new BorderLayout());

        movieListPanel.setBorder(
                BorderFactory.createTitledBorder("Available Movies")
        );

        movieListPanel.add(
                new JScrollPane(movieList),
                BorderLayout.CENTER
        );

        movieListPanel.setPreferredSize(
                new Dimension(250, 0)
        );

        // Movie information panel

        JPanel movieInfoPanel = new JPanel(new BorderLayout(10, 10));

        movieInfoPanel.setBorder(
                BorderFactory.createTitledBorder("Movie Details")
        );

        JPanel posterPanel = new JPanel(new BorderLayout());

        posterPanel.add(
                posterLabel,
                BorderLayout.CENTER
        );

        JLabel posterHint = new JLabel(
                "Click poster to view high resolution",
                SwingConstants.CENTER
        );

        posterHint.setFont(
                new Font("Arial", Font.ITALIC, 12)
        );

        posterPanel.add(
                posterHint,
                BorderLayout.SOUTH
        );

        movieInfoPanel.add(
                posterPanel,
                BorderLayout.WEST
        );

        movieInfoPanel.add(
                new JScrollPane(movieDetails),
                BorderLayout.CENTER
        );

        movieInfoPanel.add(
                selectMovieButton,
                BorderLayout.SOUTH
        );

        // Add panels

        add(movieListPanel, BorderLayout.WEST);

        add(movieInfoPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // LISTENERS
    // =========================================================

    private void setupListeners() {

        movieList.addListSelectionListener(event -> {

            if (!event.getValueIsAdjusting()) {

                Movie movie = movieList.getSelectedValue();

                if (movie != null) {
                    displayMovieDetails(movie);
                }
            }
        });

        selectMovieButton.addActionListener(event -> {

            if (selectedMovie != null) {

                if (onMovieSelected != null) {
                    onMovieSelected.accept(selectedMovie);
                }
            }
        });

        posterLabel.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent event) {

                        if (selectedMovie != null) {
                            showHighResolutionPoster();
                        }
                    }
                }
        );
    }

    // =========================================================
    // MOVIE DISPLAY
    // =========================================================

    public void refreshMovies() {

        movieListModel.clear();

        moviePosters.clear();

        ArrayList<Movie> movies =
                bookingService.getMovies();

        for (Movie movie : movies) {

            movieListModel.addElement(movie);

            // Create a Proxy for each movie poster

            String movieID = movie.getMovieID();

            String thumbnailPath =
                    "src/main/resources/posters/"
                            + movieID
                            + "_thumbnail.jpg";

            String highResolutionPath =
                    "src/main/resources/posters/"
                            + movieID
                            + ".jpg";

            MoviePoster poster = new MoviePosterProxy(
                    thumbnailPath,
                    highResolutionPath
            );

            moviePosters.add(poster);
        }

        selectedMovie = null;

        movieDetails.setText("");

        posterLabel.setIcon(null);

        posterLabel.setText(
                "Select a movie to view its poster"
        );

        selectMovieButton.setEnabled(false);
    }

    private void displayMovieDetails(Movie movie) {

        selectedMovie = movie;

        movieDetails.setText(
                "Movie Title: " + movie.getTitle()
                        + "\n\nGenre: " + movie.getGenre()
                        + "\n\nDuration: " + movie.getDuration() + " minutes"
                        + "\n\nRating: " + movie.getRating()
                        + "\n\nDescription:\n" + movie.getDescription()
        );

        selectMovieButton.setEnabled(true);

        displayThumbnail(movie);
    }

    // =========================================================
    // PROXY PATTERN - THUMBNAIL
    // =========================================================

    private void displayThumbnail(Movie movie) {

        int index = movieListModel.indexOf(movie);

        if (index < 0 || index >= moviePosters.size()) {
            return;
        }

        MoviePoster poster = moviePosters.get(index);

        ImageIcon thumbnail = poster.getThumbnail();

        if (thumbnail == null || thumbnail.getIconWidth() <= 0) {

            posterLabel.setIcon(null);

            posterLabel.setText(
                    "Poster unavailable"
            );

            return;
        }

        Image scaledImage = thumbnail.getImage().getScaledInstance(
                250,
                350,
                Image.SCALE_SMOOTH
        );

        posterLabel.setIcon(
                new ImageIcon(scaledImage)
        );

        posterLabel.setText("");
    }

    // =========================================================
    // PROXY PATTERN - HIGH RESOLUTION POSTER
    // =========================================================

    private void showHighResolutionPoster() {

        int index = movieListModel.indexOf(selectedMovie);

        if (index < 0 || index >= moviePosters.size()) {
            return;
        }

        MoviePoster poster = moviePosters.get(index);

        // High-resolution image is loaded only when clicked

        ImageIcon highResolutionPoster =
                poster.getHighResolutionPoster();

        if (highResolutionPoster == null
                || highResolutionPoster.getIconWidth() <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "High-resolution poster is unavailable.",
                    "Poster Not Found",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // Resize image for dialog display

        int originalWidth =
                highResolutionPoster.getIconWidth();

        int originalHeight =
                highResolutionPoster.getIconHeight();

        int maxWidth = 600;
        int maxHeight = 800;

        double scale = Math.min(
                (double) maxWidth / originalWidth,
                (double) maxHeight / originalHeight
        );

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        Image scaledImage = highResolutionPoster
                .getImage()
                .getScaledInstance(
                        newWidth,
                        newHeight,
                        Image.SCALE_SMOOTH
                );

        JLabel imageLabel = new JLabel(
                new ImageIcon(scaledImage)
        );

        JOptionPane.showMessageDialog(
                this,
                imageLabel,
                selectedMovie.getTitle(),
                JOptionPane.PLAIN_MESSAGE
        );
    }
}