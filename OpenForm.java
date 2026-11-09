
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OpenForm {
    private JButton btnHome;
    private JButton btnMovies;
    private JButton btnReservations;
    private JButton btnAdmin;
    private JPanel PnlMain;
    private static final Color NAVY =
            new Color(23, 43, 70);

    private static final Color LIGHT_BLUE =
            new Color(220, 234, 255);

    private static final Color BACKGROUND =
            new Color(245, 247, 251);

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "MyTicketing - Movie Ticket Reservation System"
            );

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            OpenForm form = new OpenForm();

            JPanel mainPanel = new JPanel(new BorderLayout());

            // LEFT SIDEBAR
            JPanel sidebar = createSidebar();

            // RIGHT CONTENT
            JPanel content = createHomeContent();

            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(content, BorderLayout.CENTER);

            // CONNECT MAIN PANEL TO PnlMain
            form.PnlMain = mainPanel;

            frame.setContentPane(form.PnlMain);
            frame.setVisible(true);
        });
    }

    // SIDEBAR
    private static JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(
                sidebar, BoxLayout.Y_AXIS
        ));

        sidebar.setBorder(new EmptyBorder(30, 15, 20, 15));

        JLabel logo = new JLabel("🎟");
        logo.setFont(new Font("Arial", Font.PLAIN, 55));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("MyTicketing");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(45));

        String[] buttons = {
                "Home", "Movies", "Reservations",
                "Profile", "Admin Panel"
        };

        for (String name : buttons) {

            JButton button = new JButton(name);

            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(55, 75, 105));

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setMaximumSize(new Dimension(250, 55));
            button.setPreferredSize(new Dimension(200, 55));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(12));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Arial", Font.BOLD, 18));
        exit.setMaximumSize(new Dimension(250, 55));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        exit.addActionListener(e -> System.exit(0));

        sidebar.add(exit);

        return sidebar;
    }

    // HOME CONTENT
    private static JPanel createHomeContent() {

        JPanel content = new JPanel();
        content.setBackground(BACKGROUND);

        content.setLayout(new BoxLayout(
                content, BoxLayout.Y_AXIS
        ));

        content.setBorder(new EmptyBorder(35, 30, 30, 30));

        // WELCOME TITLE
        JLabel welcome = new JLabel("Welcome to MyTicketing!");
        welcome.setFont(new Font("Arial", Font.BOLD, 38));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Your Movie Ticket Reservation System"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 23));
        subtitle.setForeground(new Color(75, 90, 110));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(welcome);
        content.add(Box.createVerticalStrut(5));
        content.add(subtitle);

        content.add(Box.createVerticalStrut(35));

        // BOOKING BANNER
        JPanel banner = new JPanel();
        banner.setBackground(LIGHT_BLUE);
        banner.setLayout(new BoxLayout(
                banner, BoxLayout.Y_AXIS
        ));

        banner.setBorder(new EmptyBorder(25, 25, 25, 25));
        banner.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 150
        ));

        JLabel bannerTitle = new JLabel(
                "🎬  Book Your Movie Tickets Now!"
        );
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerTitle.setFont(new Font("Arial", Font.BOLD, 27));
        bannerTitle.setForeground(NAVY);

        JLabel bannerText = new JLabel(
                "Browse movies, choose a schedule, select your seats, and reserve your tickets."
        );
        bannerText.setFont(new Font("Arial", Font.PLAIN, 17));
        bannerText.setForeground(NAVY);

        banner.add(bannerTitle);
        banner.add(Box.createVerticalStrut(10));
        banner.add(bannerText);
        bannerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerText.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(banner);
        content.add(Box.createVerticalStrut(30));

        // NOW SHOWING
        JLabel nowShowing = new JLabel("Now Showing");
        nowShowing.setFont(new Font("Arial", Font.BOLD, 29));
        nowShowing.setForeground(NAVY);

        content.add(nowShowing);
        content.add(Box.createVerticalStrut(15));

        // MOVIE CARDS
        JPanel movies = new JPanel(new GridLayout(1, 4, 15, 0));
        movies.setBackground(BACKGROUND);

        movies.add(createMovieCard("Avengers: Endgame",
                "Action | 181 min"));

        movies.add(createMovieCard("Inside Out 2",
                "Animation | 96 min"));

        movies.add(createMovieCard("Spider-Man: No Way Home",
                "Action | 148 min"));

        movies.add(createMovieCard("Interstellar",
                "Sci-Fi | 169 min"));

        content.add(movies);

        return content;
    }

    // MOVIE CARD
    private static JPanel createMovieCard(
            String movieName, String details) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);

        card.setLayout(new BoxLayout(
                card, BoxLayout.Y_AXIS
        ));

        card.setBorder(new EmptyBorder(10, 10, 15, 10));

        JLabel poster = new JLabel("🎬");
        poster.setFont(new Font("Arial", Font.PLAIN, 65));
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setOpaque(true);
        poster.setBackground(new Color(210, 220, 235));
        poster.setPreferredSize(new Dimension(200, 180));
        poster.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 180
        ));
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel(movieName);
        name.setFont(new Font("Arial", Font.BOLD, 15));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel(details);
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton view = new JButton("View Details");
        view.setAlignmentX(Component.CENTER_ALIGNMENT);
        view.setMaximumSize(new Dimension(200, 40));

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(8));
        card.add(info);
        card.add(Box.createVerticalStrut(15));
        card.add(view);

        return card;
    }
}
