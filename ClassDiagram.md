```mermaid
classDiagram
    class Account {
        -userID
        -userName
        -password
        +login()
        +logout()
    }
    
    class Admin {
        +addMovie()
        +editMovie()
        +removeMovie()
        +manageSchedule()
        +manageReservations()
        +viewDashboard()
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
        -description
    }

    class Schedule {
        -date
        -time
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
        -amount
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