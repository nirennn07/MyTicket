package com.myticket.decorator;

public class BaseBookingCost implements BookingCost {

    private double ticketCost;

    public BaseBookingCost(double ticketCost) {

        if (ticketCost < 0) {
            throw new IllegalArgumentException(
                    "Ticket cost cannot be negative."
            );
        }

        this.ticketCost = ticketCost;
    }

    @Override
    public double getCost() {
        return ticketCost;
    }

    @Override
    public String getDescription() {
        return "Movie Tickets";
    }
}
