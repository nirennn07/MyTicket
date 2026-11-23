package com.myticket.model;

public class Seat {

    private String seatNumber;
    private boolean available;

    // Constructor
    public Seat(String seatNumber) {
        setSeatNumber(seatNumber);
        this.available = true;
    }

    // Getters
    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be empty.");
        }

        this.seatNumber = seatNumber;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Reserve seat
    public void reserveSeat() {
        if (!available) {
            throw new IllegalStateException("Seat " + seatNumber + " is already taken.");
        }

        available = false;
    }

    // Release seat
    public void releaseSeat() {
        available = true;
    }

    @Override
    public String toString() {
        return seatNumber;
    }
}