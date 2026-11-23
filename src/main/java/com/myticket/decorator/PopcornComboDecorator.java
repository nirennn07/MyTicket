package com.myticket.decorator;

public class PopcornComboDecorator extends BookingCostDecorator {

    private static final double POPCORN_COMBO_PRICE = 150.00;

    public PopcornComboDecorator(BookingCost bookingCost) {
        super(bookingCost);
    }

    @Override
    public double getCost() {
        return bookingCost.getCost() + POPCORN_COMBO_PRICE;
    }

    @Override
    public String getDescription() {
        return bookingCost.getDescription() + " + Popcorn Combo";
    }
}