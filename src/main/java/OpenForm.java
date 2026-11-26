import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OpenForm {
    private JButton btnHome;
    private JButton btnMovies;
    private JButton btnReservations;
    private JButton btnAdmin;
    private JPanel PnlMain;

    private static final Color DEEP_NAVY = new Color(15, 32, 60);
    private static final Color NAVY = new Color(23, 48, 86);
    private static final Color ROYAL_BLUE = new Color(37, 99, 235);
    private static final Color ROYAL_BLUE_HOVER = new Color(29, 78, 216);
    private static final Color LIGHT_BLUE = new Color(219, 234, 254);
    private static final Color SOFT_ICE = new Color(239, 246, 255);
    private static final Color BACKGROUND = new Color(241, 245, 252);
    private static final Color BORDER_BLUE = new Color(191, 219, 254);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_MUTED = new Color(71, 85, 105);
    private static final Color NAV_IDLE = new Color(33, 62, 108);
    private static final Color NAV_HOVER = new Color(48, 86, 145);
    private static final Color EXIT_RED = new Color(185, 28, 28);
    private static final Color EXIT_RED_HOVER = new Color(153, 27, 27);

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
            mainPanel.setBackground(BACKGROUND);

            // LEFT SIDEBAR
            JPanel sidebar = createSidebar(form);

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
    private static JPanel createSidebar(OpenForm form) {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(
                sidebar, BoxLayout.Y_AXIS
        ));

        sidebar.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 0, 1, DEEP_NAVY),
                        new EmptyBorder(30, 15, 20, 15)
                )
        );

        JLabel logo = new JLabel("🎟");
        logo.setFont(new Font("Segoe UI", Font.PLAIN, 55));
        logo.setForeground(LIGHT_BLUE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("MyTicketing");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
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

            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(NAV_IDLE);

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setMaximumSize(new Dimension(250, 55));
            button.setPreferredSize(new Dimension(200, 55));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            addHoverEffect(button, NAV_IDLE, NAV_HOVER);

            if ("Home".equals(name)) {
                form.btnHome = button;
            } else if ("Movies".equals(name)) {
                form.btnMovies = button;
            } else if ("Reservations".equals(name)) {
                form.btnReservations = button;
            } else if ("Admin Panel".equals(name)) {
                form.btnAdmin = button;
            }

            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(12));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exit.setForeground(Color.WHITE);
        exit.setBackground(EXIT_RED);
        exit.setFocusPainted(false);
        exit.setBorderPainted(false);
        exit.setOpaque(true);
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.setMaximumSize(new Dimension(250, 55));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        addHoverEffect(exit, EXIT_RED, EXIT_RED_HOVER);

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
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Your Movie Ticket Reservation System"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 21));
        subtitle.setForeground(TEXT_MUTED);
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

        banner.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_BLUE, 1, true),
                        new EmptyBorder(25, 25, 25, 25)
                )
        );
        banner.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 150
        ));

        JLabel bannerTitle = new JLabel(
                "🎬  Book Your Movie Tickets Now!"
        );
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        bannerTitle.setForeground(DEEP_NAVY);

        JLabel bannerText = new JLabel(
                "Browse movies, choose a schedule, select your seats, and reserve your tickets."
        );
        bannerText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
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
        nowShowing.setFont(new Font("Segoe UI", Font.BOLD, 27));
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

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_BLUE, 1, true),
                        new EmptyBorder(10, 10, 15, 10)
                )
        );

        JLabel poster = new JLabel("🎬");
        poster.setFont(new Font("Segoe UI", Font.PLAIN, 65));
        poster.setForeground(ROYAL_BLUE);
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setOpaque(true);
        poster.setBackground(SOFT_ICE);
        poster.setBorder(new LineBorder(BORDER_BLUE, 1, true));
        poster.setPreferredSize(new Dimension(200, 180));
        poster.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 180
        ));
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel(movieName);
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setForeground(TEXT_DARK);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel(details);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(TEXT_MUTED);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton view = new JButton("View Details");
        view.setFont(new Font("Segoe UI", Font.BOLD, 13));
        view.setBackground(ROYAL_BLUE);
        view.setForeground(Color.WHITE);
        view.setFocusPainted(false);
        view.setBorderPainted(false);
        view.setOpaque(true);
        view.setCursor(new Cursor(Cursor.HAND_CURSOR));
        view.setAlignmentX(Component.CENTER_ALIGNMENT);
        view.setMaximumSize(new Dimension(200, 40));
        addHoverEffect(view, ROYAL_BLUE, ROYAL_BLUE_HOVER);

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(8));
        card.add(info);
        card.add(Box.createVerticalStrut(15));
        card.add(view);

        return card;
    }

    private static void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }
}