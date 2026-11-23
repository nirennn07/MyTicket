package com.myticket.decorator;

public abstract class BookingCostDecorator implements BookingCost {

    protected BookingCost bookingCost;

    public BookingCostDecorator(BookingCost bookingCost) {

        if (bookingCost == null) {
            throw new IllegalArgumentException(
                    "Booking cost cannot be null."
            );
        }

        this.bookingCost = bookingCost;
    }

    @Override
    public double getCost() {
        return bookingCost.getCost();
    }

    @Override
    public String getDescription() {
        return bookingCost.getDescription();
    }
}