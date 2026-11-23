package com.myticket.model;

public class Movie {

    private String movieID;
    private String title;
    private String genre;
    private String description;
    private int duration;
    private String rating;

    // Constructor
    public Movie(String movieID, String title, String genre,
                 String description, int duration, String rating) {

        setMovieID(movieID);
        setTitle(title);
        setGenre(genre);
        setDescription(description);
        setDuration(duration);
        setRating(rating);
    }

    // Getters
    public String getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getRating() {
        return rating;
    }

    // Setters
    public void setMovieID(String movieID) {
        if (movieID == null || movieID.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie ID cannot be empty.");
        }

        this.movieID = movieID;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be empty.");
        }

        this.title = title;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }

        this.genre = genre;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null.");
        }

        this.description = description;
    }

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }

        this.duration = duration;
    }

    public void setRating(String rating) {
        if (rating == null || rating.trim().isEmpty()) {
            throw new IllegalArgumentException("Rating cannot be empty.");
        }

        this.rating = rating;
    }

    // Display movie information
    @Override
    public String toString() {
        return title + " (" + genre + ")";
    }
}