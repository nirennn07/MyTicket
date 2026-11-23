package com.myticket.service;

import com.myticket.factory.TicketFactory;

import com.myticket.model.Booking;
import com.myticket.model.Movie;
import com.myticket.model.Schedule;
import com.myticket.model.Seat;
import com.myticket.model.Ticket;
import com.myticket.model.TicketType;

import com.myticket.decorator.BookingCost;
import com.myticket.decorator.BaseBookingCost;
import com.myticket.decorator.PopcornComboDecorator;
import com.myticket.decorator.DrinkDecorator;
import com.myticket.decorator.SnackDecorator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingService {

    private ArrayList<Movie> movies;
    private ArrayList<Schedule> schedules;
    private ArrayList<Booking> bookings;
    private ArrayList<Ticket> tickets;

    private int movieCounter;
    private int scheduleCounter;
    private int bookingCounter;
    private int ticketCounter;

    private static final int DEFAULT_ROWS = 3;
    private static final int SEATS_PER_ROW = 5;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BookingService() {

        movies = new ArrayList<>();
        schedules = new ArrayList<>();
        bookings = new ArrayList<>();
        tickets = new ArrayList<>();

        movieCounter = 6;

        scheduleCounter = 1;
        bookingCounter = 1;
        ticketCounter = 1;

        loadDemoData();

    }


    // =========================================================
    // DEMO DATA
    // =========================================================

    private void loadDemoData() {

        Movie movie1 = new Movie(
                "M001",
                "Interstellar",
                "Sci-Fi",
                "A journey beyond the stars.",
                169,
                "PG-13"
        );

        Movie movie2 = new Movie(
                "M002",
                "Avengers: Infinity War",
                "Action",
                "The Avengers face Thanos.",
                149,
                "PG-13"
        );

        Movie movie3 = new Movie(
                "M003",
                "Monsters, Inc.",
                "Animation",
                "Monsters generate power through children's screams.",
                92,
                "G"
        );

        Movie movie4 = new Movie(
                "M004",
                "The Dark Knight",
                "Action",
                "Batman faces the Joker.",
                152,
                "PG-13"
        );

        Movie movie5 = new Movie(
                "M005",
                "Spider-Man: No Way Home",
                "Action",
                "Spider-Man faces villains from different universes.",
                148,
                "PG-13"
        );

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        movies.add(movie4);
        movies.add(movie5);


        // Schedules

        createSchedule(
                "M001",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(10, 0),
                250
        );

        createSchedule(
                "M001",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(14, 30),
                250
        );

        createSchedule(
                "M001",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(19, 0),
                300
        );


        createSchedule(
                "M002",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(11, 0),
                250
        );

        createSchedule(
                "M002",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(15, 0),
                250
        );

        createSchedule(
                "M002",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(20, 0),
                300
        );


        createSchedule(
                "M003",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(9, 0),
                200
        );

        createSchedule(
                "M003",
                LocalDate.of(2026, 12, 1),
                LocalTime.of(13, 0),
                200
        );


        createSchedule(
                "M004",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(10, 30),
                250
        );

        createSchedule(
                "M004",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(16, 0),
                250
        );

        createSchedule(
                "M004",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(19, 30),
                300
        );


        createSchedule(
                "M005",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(11, 0),
                250
        );

        createSchedule(
                "M005",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(15, 30),
                250
        );

        createSchedule(
                "M005",
                LocalDate.of(2026, 12, 2),
                LocalTime.of(20, 0),
                300
        );

    }


    // =========================================================
    // MOVIE MANAGEMENT
    // =========================================================

    public void addMovie(Movie movie) {

        if (movie == null) {

            throw new IllegalArgumentException(
                    "Movie cannot be null."
            );

        }

        if (findMovie(movie.getMovieID()) != null) {

            throw new IllegalArgumentException(
                    "Movie already exists."
            );

        }

        movies.add(movie);

    }


    public Movie addMovie(
            String title,
            String genre,
            String description,
            int duration,
            String rating) {

        String movieID = String.format(
                "M%03d",
                movieCounter++
        );

        Movie movie = new Movie(
                movieID,
                title,
                genre,
                description,
                duration,
                rating
        );

        movies.add(movie);

        return movie;

    }


    public ArrayList<Movie> getMovies() {

        return new ArrayList<>(movies);

    }


    public Movie findMovie(String movieID) {

        for (Movie movie : movies) {

            if (movie.getMovieID().equals(movieID)) {

                return movie;

            }

        }

        return null;

    }


    public void removeMovie(String movieID) {

        Movie movie = findMovie(movieID);

        if (movie == null) {

            throw new IllegalArgumentException(
                    "Movie not found."
            );

        }

        movies.remove(movie);

        schedules.removeIf(schedule ->
                schedule.getMovie()
                        .getMovieID()
                        .equals(movieID)
        );

    }


    // =========================================================
    // SCHEDULE MANAGEMENT
    // =========================================================

    public Schedule createSchedule(
            String movieID,
            LocalDate date,
            LocalTime time,
            double ticketPrice) {

        Movie movie = findMovie(movieID);

        if (movie == null) {

            throw new IllegalArgumentException(
                    "Movie not found."
            );

        }

        String scheduleID = String.format(
                "S%04d",
                scheduleCounter++
        );

        Schedule schedule = new Schedule(
                scheduleID,
                movie,
                date,
                time,
                ticketPrice
        );

        generateSeats(schedule);

        schedules.add(schedule);

        return schedule;

    }


    public void addSchedule(Schedule schedule) {

        if (schedule == null) {

            throw new IllegalArgumentException(
                    "Schedule cannot be null."
            );

        }

        if (findSchedule(schedule.getScheduleID()) != null) {

            throw new IllegalArgumentException(
                    "Schedule already exists."
            );

        }

        schedules.add(schedule);

    }


    private void generateSeats(Schedule schedule) {

        for (int row = 0; row < DEFAULT_ROWS; row++) {

            char rowLetter = (char) ('A' + row);

            for (int seatNumber = 1;
                 seatNumber <= SEATS_PER_ROW;
                 seatNumber++) {

                String seatID =
                        rowLetter + String.valueOf(seatNumber);

                schedule.addSeat(new Seat(seatID));

            }

        }

    }


    public ArrayList<Schedule> getSchedules() {

        return new ArrayList<>(schedules);

    }


    public ArrayList<Schedule> getSchedulesForMovie(
            String movieID) {

        ArrayList<Schedule> movieSchedules =
                new ArrayList<>();

        for (Schedule schedule : schedules) {

            if (schedule.getMovie()
                    .getMovieID()
                    .equals(movieID)) {

                movieSchedules.add(schedule);

            }

        }

        return movieSchedules;

    }


    public Schedule findSchedule(String scheduleID) {

        for (Schedule schedule : schedules) {

            if (schedule.getScheduleID().equals(scheduleID)) {

                return schedule;

            }

        }

        return null;

    }


    public void removeSchedule(String scheduleID) {

        Schedule schedule = findSchedule(scheduleID);

        if (schedule == null) {

            throw new IllegalArgumentException(
                    "Schedule not found."
            );

        }

        schedules.remove(schedule);

    }


    // =========================================================
    // SEAT MANAGEMENT
    // =========================================================

    public boolean isSeatAvailable(
            Schedule schedule,
            String seatNumber) {

        if (schedule == null) {

            return false;

        }

        Seat seat = schedule.getSeat(seatNumber);

        return seat != null && seat.isAvailable();

    }


    // =========================================================
    // PRICING
    // =========================================================

    public double calculateDiscount(
            double ticketPrice,
            boolean seniorCitizen) {

        if (seniorCitizen) {

            return ticketPrice * 0.20;

        }

        return 0.0;

    }


    public double calculateFinalPrice(
            double ticketPrice,
            boolean seniorCitizen) {

        return ticketPrice - calculateDiscount(
                ticketPrice,
                seniorCitizen
        );

    }


    // =========================================================
    // BOOKING CREATION
    // =========================================================

    // Regular booking

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats) {

        return createBooking(
                schedule,
                selectedSeats,
                new ArrayList<>()
        );

    }


    // Booking with senior citizen seats

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats,
            ArrayList<Seat> seniorCitizenSeats) {

        Map<String, TicketType> ticketTypes =
                new HashMap<>();

        if (seniorCitizenSeats != null) {

            for (Seat seat : seniorCitizenSeats) {

                if (seat != null) {

                    ticketTypes.put(
                            seat.getSeatNumber(),
                            TicketType.SENIOR
                    );

                }

            }

        }

        return createBooking(
                schedule,
                selectedSeats,
                ticketTypes
        );

    }


    // Booking with different ticket types

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats,
            Map<String, TicketType> ticketTypes) {

        return createBooking(
                schedule,
                selectedSeats,
                ticketTypes,
                false,
                false,
                false
        );

    }


    // Booking with add-ons

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats,
            boolean popcornSelected,
            boolean drinkSelected,
            boolean snackSelected) {

        return createBooking(
                schedule,
                selectedSeats,
                new HashMap<>(),
                popcornSelected,
                drinkSelected,
                snackSelected
        );

    }


    // Booking with ticket types and add-ons
    // Original method preserved for compatibility

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats,
            Map<String, TicketType> ticketTypes,
            boolean popcornSelected,
            boolean drinkSelected,
            boolean snackSelected) {

        return createBooking(
                schedule,
                selectedSeats,
                ticketTypes,
                popcornSelected,
                drinkSelected,
                snackSelected,
                "Unknown"
        );

    }


    // NEW: Booking with username

    public Booking createBooking(
            Schedule schedule,
            ArrayList<Seat> selectedSeats,
            Map<String, TicketType> ticketTypes,
            boolean popcornSelected,
            boolean drinkSelected,
            boolean snackSelected,
            String username) {


        // =====================================================
        // VALIDATION
        // =====================================================

        if (schedule == null) {

            throw new IllegalArgumentException(
                    "Schedule cannot be null."
            );

        }

        if (selectedSeats == null
                || selectedSeats.isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one seat."
            );

        }

        if (username == null || username.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Username cannot be empty."
            );

        }

        if (ticketTypes == null) {

            ticketTypes = new HashMap<>();

        }


        // =====================================================
        // VALIDATE SELECTED SEATS
        // =====================================================

        ArrayList<Seat> validatedSeats =
                new ArrayList<>();

        for (Seat selectedSeat : selectedSeats) {

            if (selectedSeat == null) {

                throw new IllegalArgumentException(
                        "Selected seat cannot be null."
                );

            }

            Seat actualSeat = schedule.getSeat(
                    selectedSeat.getSeatNumber()
            );


            if (actualSeat == null) {

                throw new IllegalArgumentException(
                        "Seat does not belong to this schedule."
                );

            }


            if (!actualSeat.isAvailable()) {

                throw new IllegalStateException(
                        "Seat "
                                + actualSeat.getSeatNumber()
                                + " is already reserved."
                );

            }


            // Check duplicate seats

            for (Seat existingSeat : validatedSeats) {

                if (existingSeat.getSeatNumber()
                        .equals(actualSeat.getSeatNumber())) {

                    throw new IllegalArgumentException(
                            "Duplicate seat selected."
                    );

                }

            }

            validatedSeats.add(actualSeat);

        }


        // =====================================================
        // CALCULATE TICKET TOTALS
        // =====================================================

        double totalAmount = 0;
        double discountAmount = 0;
        double finalAmount = 0;


        for (Seat seat : validatedSeats) {

            TicketType ticketType = ticketTypes.get(
                    seat.getSeatNumber()
            );

            if (ticketType == null) {

                ticketType = TicketType.REGULAR;

            }


            double basePrice = schedule.getTicketPrice();


            switch (ticketType) {

                case REGULAR:

                    totalAmount += basePrice;

                    finalAmount += basePrice;

                    break;


                case VIP:

                    double vipPrice =
                            basePrice + (basePrice * 0.50);

                    totalAmount += vipPrice;

                    finalAmount += vipPrice;

                    break;


                case SENIOR:

                    double seniorDiscount =
                            basePrice * 0.20;

                    totalAmount += basePrice;

                    discountAmount += seniorDiscount;

                    finalAmount +=
                            basePrice - seniorDiscount;

                    break;

            }

        }


        // =====================================================
        // DECORATOR PATTERN - ADD-ONS
        // =====================================================

        BookingCost bookingCost =
                new BaseBookingCost(finalAmount);


        // Popcorn Combo

        if (popcornSelected) {

            bookingCost =
                    new PopcornComboDecorator(
                            bookingCost
                    );

        }


        // Drink

        if (drinkSelected) {

            bookingCost =
                    new DrinkDecorator(
                            bookingCost
                    );

        }


        // Snack

        if (snackSelected) {

            bookingCost =
                    new SnackDecorator(
                            bookingCost
                    );

        }


        // Calculate total add-on amount

        double addOnAmount =
                bookingCost.getCost() - finalAmount;


        // Final amount including add-ons

        finalAmount = bookingCost.getCost();


        // Create add-on description

        String addOns = "";

        if (popcornSelected) {

            addOns += "Popcorn Combo";

        }


        if (drinkSelected) {

            if (!addOns.isEmpty()) {

                addOns += ", ";

            }

            addOns += "Drink";

        }


        if (snackSelected) {

            if (!addOns.isEmpty()) {

                addOns += ", ";

            }

            addOns += "Snack";

        }


        // =====================================================
        // CREATE BOOKING
        // =====================================================

        String bookingID = String.format(
                "B%04d",
                bookingCounter++
        );


        // Reserve seats

        ArrayList<Seat> reservedSeats =
                new ArrayList<>();


        try {

            for (Seat seat : validatedSeats) {

                seat.reserveSeat();

                reservedSeats.add(seat);

            }


            // Create Booking with username and add-on information

            Booking booking = new Booking(
                    bookingID,
                    schedule,
                    validatedSeats,
                    totalAmount,
                    discountAmount,
                    addOnAmount,
                    addOns,
                    finalAmount,
                    username
            );


            // =================================================
            // CREATE TICKETS USING TICKET FACTORY
            // =================================================

            ArrayList<Ticket> bookingTickets =
                    new ArrayList<>();


            for (Seat seat : validatedSeats) {

                TicketType ticketType = ticketTypes.get(
                        seat.getSeatNumber()
                );

                if (ticketType == null) {

                    ticketType = TicketType.REGULAR;

                }


                String ticketID = String.format(
                        "T%04d",
                        ticketCounter++
                );


                Ticket ticket = TicketFactory.createTicket(
                        ticketID,
                        booking,
                        seat,
                        ticketType,
                        schedule.getTicketPrice()
                );


                bookingTickets.add(ticket);

            }


            // =================================================
            // STORE BOOKING AND TICKETS
            // =================================================

            bookings.add(booking);

            tickets.addAll(bookingTickets);


            return booking;


        } catch (RuntimeException e) {

            // Rollback reserved seats if booking fails

            for (Seat seat : reservedSeats) {

                seat.releaseSeat();

            }

            throw e;

        }

    }


    // =========================================================
    // BOOKING CANCELLATION
    // =========================================================

    public void cancelBooking(String bookingID) {

        Booking booking = findBooking(bookingID);

        if (booking == null) {

            throw new IllegalArgumentException(
                    "Booking not found."
            );

        }

        booking.cancelBooking();


        // Remove tickets associated with booking

        tickets.removeIf(ticket ->
                ticket.getBooking()
                        .getBookingID()
                        .equals(bookingID)
        );

    }


    // =========================================================
    // BOOKING RETRIEVAL
    // =========================================================

    public Booking findBooking(String bookingID) {

        for (Booking booking : bookings) {

            if (booking.getBookingID().equals(bookingID)) {

                return booking;

            }

        }

        return null;

    }


    public ArrayList<Booking> getBookings() {

        return new ArrayList<>(bookings);

    }


    // NEW: Retrieve bookings belonging to a specific user

    public ArrayList<Booking> getBookingsForUser(
            String username) {

        ArrayList<Booking> userBookings =
                new ArrayList<>();

        if (username == null) {

            return userBookings;

        }

        for (Booking booking : bookings) {

            if (booking.getUsername() != null
                    && booking.getUsername()
                    .equalsIgnoreCase(username)) {

                userBookings.add(booking);

            }

        }

        return userBookings;

    }


    // =========================================================
    // TICKET RETRIEVAL
    // =========================================================

    public ArrayList<Ticket> getTickets() {

        return new ArrayList<>(tickets);

    }


    // NEW: Retrieve tickets belonging to a specific user

    public ArrayList<Ticket> getTicketsForUser(
            String username) {

        ArrayList<Ticket> userTickets =
                new ArrayList<>();

        if (username == null) {

            return userTickets;

        }

        for (Ticket ticket : tickets) {

            Booking booking = ticket.getBooking();

            if (booking.getUsername() != null
                    && booking.getUsername()
                    .equalsIgnoreCase(username)) {

                userTickets.add(ticket);

            }

        }

        return userTickets;

    }


    public ArrayList<Ticket> getTicketsForBooking(
            String bookingID) {

        ArrayList<Ticket> bookingTickets =
                new ArrayList<>();

        for (Ticket ticket : tickets) {

            if (ticket.getBooking()
                    .getBookingID()
                    .equals(bookingID)) {

                bookingTickets.add(ticket);

            }

        }

        return bookingTickets;

    }

}