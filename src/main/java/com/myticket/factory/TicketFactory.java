package com.myticket.factory;

import com.myticket.model.Booking;
import com.myticket.model.Seat;
import com.myticket.model.Ticket;
import com.myticket.model.TicketType;

public class TicketFactory {

    private static final double SENIOR_DISCOUNT_RATE = 0.20;
    private static final double VIP_SURCHARGE_RATE = 0.50;

    public static Ticket createTicket(
            String ticketID,
            Booking booking,
            Seat seat,
            TicketType ticketType,
            double basePrice) {

        double ticketPrice = basePrice;
        double discountAmount = 0.0;
        double finalPrice = basePrice;
        boolean seniorCitizen = false;

        switch (ticketType) {

            case REGULAR:
                ticketPrice = basePrice;
                discountAmount = 0.0;
                finalPrice = basePrice;
                seniorCitizen = false;
                break;

            case VIP:
                ticketPrice = basePrice + (basePrice * VIP_SURCHARGE_RATE);
                discountAmount = 0.0;
                finalPrice = ticketPrice;
                seniorCitizen = false;
                break;

            case SENIOR:
                ticketPrice = basePrice;
                discountAmount = basePrice * SENIOR_DISCOUNT_RATE;
                finalPrice = basePrice - discountAmount;
                seniorCitizen = true;
                break;

            default:
                throw new IllegalArgumentException("Invalid ticket type.");
        }

        return new Ticket(
                ticketID,
                booking,
                seat,
                ticketPrice,
                discountAmount,
                finalPrice,
                seniorCitizen
        );
    }
}