package com.myticket.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Booking {

    private String bookingID;
    private Schedule schedule;
    private ArrayList<Seat> selectedSeats;
    private LocalDateTime bookingDate;

    private double totalAmount;
    private double discountAmount;
    private double addOnAmount;
    private double finalAmount;

    private String addOns;
    private String status;

    // Original constructor preserved for compatibility
    public Booking(String bookingID, Schedule schedule,
                   ArrayList<Seat> selectedSeats,
                   double totalAmount,
                   double discountAmount,
                   double finalAmount) {

        this(bookingID, schedule, selectedSeats,
                totalAmount, discountAmount,
                0.0, "None", finalAmount);
    }

    // Revised constructor with add-ons
    public Booking(String bookingID, Schedule schedule,
                   ArrayList<Seat> selectedSeats,
                   double totalAmount,
                   double discountAmount,
                   double addOnAmount,
                   String addOns,
                   double finalAmount) {

        this.bookingID = bookingID;
        this.schedule = schedule;
        this.selectedSeats = new ArrayList<>(selectedSeats);
        this.bookingDate = LocalDateTime.now();

        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.addOnAmount = addOnAmount;
        this.addOns = addOns;
        this.finalAmount = finalAmount;

        this.status = "CONFIRMED";
    }

    // Getters

    public String getBookingID() {
        return bookingID;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public ArrayList<Seat> getSelectedSeats() {
        return new ArrayList<>(selectedSeats);
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getAddOnAmount() {
        return addOnAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public String getAddOns() {
        return addOns;
    }

    public String getStatus() {
        return status;
    }

    // Setters

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setAddOnAmount(double addOnAmount) {
        this.addOnAmount = addOnAmount;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Cancel booking
    public void cancelBooking() {
        this.status = "CANCELLED";
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingID
                + "\nMovie: " + schedule.getMovie().getTitle()
                + "\nSchedule: " + schedule.getDate() + " " + schedule.getTime()
                + "\nSeats: " + selectedSeats
                + "\nTicket Total: ₱" + totalAmount
                + "\nDiscount: ₱" + discountAmount
                + "\nAdd-ons: " + addOns
                + "\nAdd-on Amount: ₱" + addOnAmount
                + "\nFinal Amount: ₱" + finalAmount
                + "\nStatus: " + status;
    }
}