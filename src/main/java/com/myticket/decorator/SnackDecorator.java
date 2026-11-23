package com.myticket.decorator;

public class SnackDecorator extends BookingCostDecorator {

    private static final double SNACK_PRICE = 100.00;

    public SnackDecorator(BookingCost bookingCost) {
        super(bookingCost);
    }

    @Override
    public double getCost() {
        return bookingCost.getCost() + SNACK_PRICE;
    }

    @Override
    public String getDescription() {
        return bookingCost.getDescription() + " + Snack";
    }
}