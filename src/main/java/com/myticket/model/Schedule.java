package com.myticket.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule {

    private String scheduleID;
    private Movie movie;
    private LocalDate date;
    private LocalTime time;
    private double ticketPrice;
    private ArrayList<Seat> seats;

    // Constructor
    public Schedule(String scheduleID, Movie movie,
                    LocalDate date, LocalTime time,
                    double ticketPrice) {

        setScheduleID(scheduleID);
        setMovie(movie);
        setDate(date);
        setTime(time);
        setTicketPrice(ticketPrice);

        this.seats = new ArrayList<>();
    }

    // Getters
    public String getScheduleID() {
        return scheduleID;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public ArrayList<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    // Setters
    public void setScheduleID(String scheduleID) {
        if (scheduleID == null || scheduleID.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID cannot be empty.");
        }

        this.scheduleID = scheduleID;
    }

    public void setMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }

        this.movie = movie;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Screening date cannot be null.");
        }

        this.date = date;
    }

    public void setTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Screening time cannot be null.");
        }

        this.time = time;
    }

    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative.");
        }

        this.ticketPrice = ticketPrice;
    }

    // Add seat to schedule
    public void addSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat cannot be null.");
        }

        for (Seat existingSeat : seats) {
            if (existingSeat.getSeatNumber().equalsIgnoreCase(
                    seat.getSeatNumber())) {

                throw new IllegalArgumentException(
                        "Seat " + seat.getSeatNumber() + " already exists.");
            }
        }

        seats.add(seat);
    }

    // Find a seat
    public Seat getSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be empty.");
        }

        for (Seat seat : seats) {
            if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }

        return null;
    }

    // Get available seats
    public ArrayList<Seat> getAvailableSeats() {
        ArrayList<Seat> availableSeats = new ArrayList<>();

        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    @Override
    public String toString() {
        return movie.getTitle() + " - " + date + " - " + time;
    }
}