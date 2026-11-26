package com.myticket.view;

import com.myticket.model.Movie;
import com.myticket.proxy.MoviePoster;
import com.myticket.proxy.MoviePosterProxy;
import com.myticket.service.BookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MoviePanel extends JPanel {

    private final BookingService bookingService;
    private final Consumer<Movie> onMovieSelected;

    // Bound automatically from MoviePanel.form
    private JPanel rootPanel;
    private JPanel movieListPanel;
    private JScrollPane movieListScrollPane;
    private JList<Movie> movieList;

    private JPanel movieInfoPanel;
    private JPanel posterPanel;
    private JLabel posterLabel;
    private JLabel posterHintLabel;
    private JScrollPane detailsScrollPane;
    private JTextArea movieDetails;
    private JButton selectMovieButton;

    private DefaultListModel<Movie> movieListModel;
    private Movie selectedMovie;
    private final ArrayList<MoviePoster> moviePosters;

    public MoviePanel(
            BookingService bookingService,
            Consumer<Movie> onMovieSelected) {

        this.bookingService = bookingService;
        this.onMovieSelected = onMovieSelected;
        this.moviePosters = new ArrayList<>();

        setLayout(new BorderLayout());
        add(rootPanel, BorderLayout.CENTER);

        configureBoundComponents();
        setupListeners();
        refreshMovies();
    }

    // Backward-compatible constructor
    public MoviePanel(BookingService bookingService) {
        this(bookingService, movie -> JOptionPane.showMessageDialog(
                null,
                "Schedule selection will be implemented."
        ));
    }

    // =========================================================
    // COMPONENT BEHAVIOR (NON-LAYOUT)
    // =========================================================

    private void configureBoundComponents() {
        movieListModel = new DefaultListModel<>();
        movieList.setModel(movieListModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setFixedCellHeight(38);

        posterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectMovieButton.setEnabled(false);
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
            if (selectedMovie != null && onMovieSelected != null) {
                onMovieSelected.accept(selectedMovie);
            }
        });

        posterLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (selectedMovie != null) {
                    showHighResolutionPoster();
                }
            }
        });
    }

    // =========================================================
    // MOVIE DISPLAY
    // =========================================================

    public void refreshMovies() {
        movieListModel.clear();
        moviePosters.clear();

        ArrayList<Movie> movies = bookingService.getMovies();

        for (Movie movie : movies) {
            movieListModel.addElement(movie);

            String movieID = movie.getMovieID();
            String thumbnailPath = "src/main/resources/posters/" + movieID + "_thumbnail.jpg";
            String highResolutionPath = "src/main/resources/posters/" + movieID + ".jpg";

            MoviePoster poster = new MoviePosterProxy(thumbnailPath, highResolutionPath);
            moviePosters.add(poster);
        }

        selectedMovie = null;
        movieDetails.setText("");
        posterLabel.setIcon(null);
        posterLabel.setText("Select a movie to view its poster");
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
            posterLabel.setText("<html><center>POSTER GOES HERE<br>(" + movie.getTitle() + ")</center></html>");
            return;
        }

        int targetW = Math.max(150, posterLabel.getWidth() > 0 ? posterLabel.getWidth() - 12 : 190);
        int targetH = Math.max(130, posterLabel.getHeight() > 0 ? posterLabel.getHeight() - 12 : 160);

        Image scaledImage = thumbnail.getImage().getScaledInstance(
                targetW,
                targetH,
                Image.SCALE_SMOOTH
        );

        posterLabel.setIcon(new ImageIcon(scaledImage));
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
        ImageIcon highResolutionPoster = poster.getHighResolutionPoster();

        if (highResolutionPoster == null || highResolutionPoster.getIconWidth() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "High-resolution poster is unavailable.",
                    "Poster Not Found",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int originalWidth = highResolutionPoster.getIconWidth();
        int originalHeight = highResolutionPoster.getIconHeight();

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
                .getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        JOptionPane.showMessageDialog(
                this,
                imageLabel,
                selectedMovie.getTitle(),
                JOptionPane.PLAIN_MESSAGE
        );
    }
}