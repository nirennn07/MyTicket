```mermaid
classDiagram
    class Account {
        -userID
        -name
        -email
        -password
        +login()
        +logout()
    }

    class Organizer {
        +addMovie()
        +editMovie()
        +removeMovie()
        +manageSchedule()
    }

    class Admin {
        +manageUsers()
        +manageReservations()
        +viewDashboard()
        +generateReports()
    }

    class User {
        +register()
        +bookTicket()
        +viewBooking()
    }

    class Movie {
        -movieID
        -title
        -genre
        -cast
        -description
    }

    class Schedule {
        -scheduleID
        -date
        -time
    }

    class Cinema {
        -cinemaID
        -cinemaName
        -capacity
    }

    class Seat {
        -seatID
        -seatNumber
        -status
    }

    class Booking {
        -bookingID
        -totalAmount
        -status
        +createBooking()
        +cancelBooking()
    }

    class Ticket {
        -ticketID
        -ticketType
        -price
    }

    class QRCode {
        -qrID
        -qrData
        +generateQR()
    }

    class Payment {
        -paymentID
        -amount
        -paymentMethod
        -paymentStatus
        +processPayment()
    }

    %% Inheritance relationships
    Account <|-- Organizer
    Account <|-- Admin
    Account <|-- User

    %% Management / Association relationships
    Admin -- Organizer : Manages
    Admin -- User : Manages
    Admin -- Booking : Monitors
    Organizer -- Movie : Manages
    Organizer -- Schedule : Creates

    %% Structural relationships
    Schedule -- Movie
    Schedule -- Cinema
    User -- Booking
    Booking -- Schedule
    Booking -- Ticket
    Booking -- Payment
    Ticket -- QRCode
    Ticket -- Seat