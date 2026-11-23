package com.myticket.decorator;

public class DrinkDecorator extends BookingCostDecorator {

    private static final double DRINK_PRICE = 80.00;

    public DrinkDecorator(BookingCost bookingCost) {
        super(bookingCost);
    }

    @Override
    public double getCost() {
        return bookingCost.getCost() + DRINK_PRICE;
    }

    @Override
    public String getDescription() {
        return bookingCost.getDescription() + " + Drink";
    }
}