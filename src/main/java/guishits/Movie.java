package guishits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {
    private final String title;
    private final String genre;
    private final int duration;      // minutes
    private final String rating;     // e.g. PG, R-13
    private final String synopsis;
    private final String[] showtimes;
    private final double price;      // per ticket
    private final String emoji;      // placeholder poster

    // seats left for each showtime
    private final Map<String, Integer> seatsLeft = new HashMap<>();
    private static final int SEATS_PER_SHOWING = 20;   // set to 3 while testing

    public Movie(String title, String genre, int duration, String rating,
                 String synopsis, String[] showtimes, double price, String emoji) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.synopsis = synopsis;
        this.showtimes = showtimes;
        this.price = price;
        this.emoji = emoji;

        for (String t : showtimes) {
            seatsLeft.put(t, SEATS_PER_SHOWING);
        }
    }

    public String getTitle()       { return title; }
    public String getGenre()       { return genre; }
    public int getDuration()       { return duration; }
    public String getRating()      { return rating; }
    public String getSynopsis()    { return synopsis; }
    public String[] getShowtimes() { return showtimes; }
    public double getPrice()       { return price; }
    public String getEmoji()       { return emoji; }

    public String getShortInfo() {
        return genre + " | " + duration + " min";
    }

    // How many seats are left for a given showtime
    public int getSeatsLeft(String showtime) {
        return seatsLeft.getOrDefault(showtime, 0);
    }

    // Throws an error when there are not enough tickets left
    public void reserve(String showtime, int tickets) {
        int left = getSeatsLeft(showtime);

        if (left == 0) {
            throw new IllegalStateException(
                    "Sorry, the " + showtime + " showing of " + title + " is sold out.");
        }
        if (tickets > left) {
            throw new IllegalStateException(
                    "Only " + left + " ticket(s) left for the " + showtime + " showing.");
        }
        seatsLeft.put(showtime, left - tickets);
    }

    // Built once, so every page shares the same Movie objects (and seat counts)
    private static final List<Movie> SAMPLE = buildSampleMovies();

    // Temporary sample data - replace with database/file loading later
    public static List<Movie> sampleMovies() {
        return SAMPLE;
    }

    private static List<Movie> buildSampleMovies() {
        String[] times = {"10:00 AM", "1:30 PM", "4:45 PM", "7:30 PM"};
        List<Movie> list = new ArrayList<>();

        list.add(new Movie("Avengers: Endgame", "Action", 181, "PG-13",
                "After the devastating events of Infinity War, the remaining Avengers assemble once more to undo Thanos' actions and restore balance to the universe.",
                times, 350, "🦸"));
        list.add(new Movie("Inside Out 2", "Animation", 96, "PG",
                "Riley enters her teenage years and new emotions arrive at Headquarters, shaking up everything Joy and the gang thought they knew.",
                times, 300, "😊"));
        list.add(new Movie("Spider-Man: No Way Home", "Action", 148, "PG-13",
                "Peter Parker asks Doctor Strange for help hiding his identity, but the spell tears open the multiverse.",
                times, 350, "🕷"));
        list.add(new Movie("Interstellar", "Sci-Fi", 169, "PG-13",
                "A team of explorers travels through a wormhole in space in an attempt to ensure humanity's survival.",
                times, 320, "🚀"));
        list.add(new Movie("The Dark Knight", "Action", 152, "PG-13",
                "Batman faces the Joker, a criminal mastermind who plunges Gotham City into chaos.",
                times, 320, "🦇"));
        list.add(new Movie("Coco", "Animation", 105, "PG",
                "Aspiring musician Miguel enters the Land of the Dead to find his great-great-grandfather, a legendary singer.",
                times, 300, "💀"));
        list.add(new Movie("Dune: Part Two", "Sci-Fi", 166, "PG-13",
                "Paul Atreides unites with the Fremen to seek revenge against the conspirators who destroyed his family.",
                times, 350, "🏜"));
        list.add(new Movie("Parasite", "Drama", 132, "R-16",
                "Greed and class discrimination threaten the newly formed relationship between the wealthy Park family and the destitute Kim clan.",
                times, 300, "🏠"));

        return list;
    }
}