package guishits;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OpenForm {
    private JButton btnHome;
    private JButton btnMovies;
    private JButton btnReservations;
    private JButton btnAdmin;
    private JPanel PnlMain;

    // package-private so MoviesPanel can reuse them
    static final Color NAVY = new Color(23, 43, 70);
    static final Color LIGHT_BLUE = new Color(220, 234, 255);
    static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color NAV_NORMAL = new Color(55, 75, 105);
    private static final Color NAV_ACTIVE = new Color(90, 120, 170);

    // Segoe UI Emoji fixes the "empty box" icons you saw on Windows
    static Font emojiFont(int size) {
        return new Font("Segoe UI Emoji", Font.PLAIN, size);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "MyTicketing - Movie Ticket Reservation System"
            );

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            OpenForm form = new OpenForm();

            // RIGHT SIDE: pages switched with CardLayout
            CardLayout cardLayout = new CardLayout();
            JPanel pages = new JPanel(cardLayout);
            pages.add(createHomeContent(), "HOME");
            pages.add(new MoviesPanel(), "MOVIES");
            pages.add(createPlaceholder("Reservations"), "RESERVATIONS");
            pages.add(createPlaceholder("Profile"), "PROFILE");
            pages.add(createPlaceholder("Admin Panel"), "ADMIN");

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(createSidebar(cardLayout, pages), BorderLayout.WEST);
            mainPanel.add(pages, BorderLayout.CENTER);

            form.PnlMain = mainPanel;

            frame.setContentPane(form.PnlMain);
            frame.setVisible(true);
        });
    }

    // SIDEBAR
    private static JPanel createSidebar(CardLayout cardLayout, JPanel pages) {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 15, 20, 15));

        JLabel logo = new JLabel("🎟");
        logo.setFont(emojiFont(55));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("MyTicketing");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(45));

        // {button text, card name}
        String[][] items = {
                {"Home", "HOME"},
                {"Movies", "MOVIES"},
                {"Reservations", "RESERVATIONS"},
                {"Profile", "PROFILE"},
                {"Admin Panel", "ADMIN"}
        };

        List<JButton> navButtons = new ArrayList<>();

        for (String[] item : items) {

            JButton button = new JButton(item[0]);

            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setForeground(Color.WHITE);
            button.setBackground(NAV_NORMAL);

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setMaximumSize(new Dimension(250, 55));
            button.setPreferredSize(new Dimension(200, 55));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            button.addActionListener(e -> {
                cardLayout.show(pages, item[1]);
                highlight(navButtons, button);
            });

            navButtons.add(button);
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(12));
        }

        highlight(navButtons, navButtons.get(0));   // Home is active at start

        sidebar.add(Box.createVerticalGlue());

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Arial", Font.BOLD, 18));
        exit.setMaximumSize(new Dimension(250, 55));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(e -> System.exit(0));

        sidebar.add(exit);

        return sidebar;
    }

    private static void highlight(List<JButton> buttons, JButton active) {
        for (JButton b : buttons) {
            b.setBackground(b == active ? NAV_ACTIVE : NAV_NORMAL);
        }
    }

    // TEMPORARY PAGE for sections not built yet
    private static JPanel createPlaceholder(String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND);

        JLabel label = new JLabel(name + " - coming soon");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(NAVY);

        panel.add(label);
        return panel;
    }

    // HOME CONTENT
    private static JPanel createHomeContent() {

        JPanel content = new JPanel();
        content.setBackground(BACKGROUND);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(35, 30, 30, 30));

        // WELCOME TITLE
        JLabel welcome = new JLabel("Welcome to MyTicketing!");
        welcome.setFont(new Font("Arial", Font.BOLD, 38));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Your Movie Ticket Reservation System");
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
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(new EmptyBorder(25, 25, 25, 25));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bannerTitle = new JLabel("🎬  Book Your Movie Tickets Now!");
        bannerTitle.setFont(emojiFont(27));
        bannerTitle.setForeground(NAVY);
        bannerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bannerText = new JLabel(
                "Browse movies, choose a schedule, select your seats, and reserve your tickets."
        );
        bannerText.setFont(new Font("Arial", Font.PLAIN, 15));
        bannerText.setForeground(NAVY);
        bannerText.setAlignmentX(Component.LEFT_ALIGNMENT);

        banner.add(bannerTitle);
        banner.add(Box.createVerticalStrut(10));
        banner.add(bannerText);

        content.add(banner);
        content.add(Box.createVerticalStrut(30));

        // NOW SHOWING
        JLabel nowShowing = new JLabel("Now Showing");
        nowShowing.setFont(new Font("Arial", Font.BOLD, 29));
        nowShowing.setForeground(NAVY);
        nowShowing.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(nowShowing);
        content.add(Box.createVerticalStrut(15));

        // MOVIE CARDS (first 4 sample movies)
        JPanel movies = new JPanel(new GridLayout(1, 4, 15, 0));
        movies.setBackground(BACKGROUND);
        movies.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Movie m : Movie.sampleMovies().subList(0, 4)) {
            movies.add(MoviesPanel.createMovieCard(m));
        }

        content.add(movies);

        return content;
    }
}