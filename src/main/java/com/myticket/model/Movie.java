package com.myticket.model;

public class Movie {

    private String movieID;
    private String title;
    private String genre;
    private String cast;
    private String description;
    private int duration;
    private double rating;

    public Movie(String movieID, String title, String genre,
                 String cast, String description, int duration, double rating) {

        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.description = description;
        this.duration = duration;
        this.rating = rating;
    }

    public String getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getCast() {
        return cast;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }
}
