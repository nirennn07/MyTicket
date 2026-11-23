package com.myticket.model;

public class Ticket {

    private String ticketID;
    private Booking booking;
    private Seat seat;
    private double ticketPrice;
    private double discountAmount;
    private double finalPrice;
    private TicketType ticketType;

    public Ticket(
            String ticketID,
            Booking booking,
            Seat seat,
            double ticketPrice,
            double discountAmount,
            double finalPrice,
            TicketType ticketType) {

        setTicketID(ticketID);
        setBooking(booking);
        setSeat(seat);
        setTicketPrice(ticketPrice);
        setDiscountAmount(discountAmount);
        setFinalPrice(finalPrice);
        setTicketType(ticketType);
    }

    // Backward-compatible constructor
    public Ticket(
            String ticketID,
            Booking booking,
            Seat seat,
            double ticketPrice,
            double discountAmount,
            double finalPrice,
            boolean seniorCitizen) {

        this(
                ticketID,
                booking,
                seat,
                ticketPrice,
                discountAmount,
                finalPrice,
                seniorCitizen ? TicketType.SENIOR : TicketType.REGULAR
        );
    }

    // Getters

    public String getTicketID() {
        return ticketID;
    }

    public Booking getBooking() {
        return booking;
    }

    public Seat getSeat() {
        return seat;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public boolean isSeniorCitizen() {
        return ticketType == TicketType.SENIOR;
    }

    public String getMovieTitle() {
        return booking.getSchedule().getMovie().getTitle();
    }

    public java.time.LocalDate getScreeningDate() {
        return booking.getSchedule().getDate();
    }

    public java.time.LocalTime getScreeningTime() {
        return booking.getSchedule().getTime();
    }

    // Setters

    public void setTicketID(String ticketID) {
        if (ticketID == null || ticketID.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be empty.");
        }

        this.ticketID = ticketID;
    }

    public void setBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }

        this.booking = booking;
    }

    public void setSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat cannot be null.");
        }

        this.seat = seat;
    }

    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative.");
        }

        this.ticketPrice = ticketPrice;
    }

    public void setDiscountAmount(double discountAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative.");
        }

        this.discountAmount = discountAmount;
    }

    public void setFinalPrice(double finalPrice) {
        if (finalPrice < 0) {
            throw new IllegalArgumentException("Final price cannot be negative.");
        }

        this.finalPrice = finalPrice;
    }

    public void setTicketType(TicketType ticketType) {
        if (ticketType == null) {
            throw new IllegalArgumentException("Ticket type cannot be null.");
        }

        this.ticketType = ticketType;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketID +
                "\nMovie: " + getMovieTitle() +
                "\nSeat: " + seat.getSeatNumber() +
                "\nTicket Type: " + ticketType +
                "\nTicket Price: ₱" + ticketPrice +
                "\nDiscount: ₱" + discountAmount +
                "\nFinal Price: ₱" + finalPrice;
    }
}