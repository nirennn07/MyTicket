package guishits;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Locale;

public class MoviesPanel extends JPanel {

    private final List<Movie> allMovies = Movie.sampleMovies();
    private final JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
    private final JTextField searchField = new JTextField(18);
    private final JComboBox<String> genreBox = new JComboBox<>(
            new String[]{"All Genres", "Action", "Animation", "Sci-Fi", "Drama"});
    private final JLabel countLabel = new JLabel();

    public MoviesPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(OpenForm.BACKGROUND);
        setBorder(new EmptyBorder(35, 30, 30, 30));

        add(createHeader(), BorderLayout.NORTH);

        grid.setBackground(OpenForm.BACKGROUND);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(OpenForm.BACKGROUND);
        wrapper.add(grid, BorderLayout.NORTH);   // keeps cards from stretching vertically

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(OpenForm.BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    // HEADER: title + search + genre filter
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(OpenForm.BACKGROUND);

        JPanel left = new JPanel();
        left.setBackground(OpenForm.BACKGROUND);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Movies");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(OpenForm.NAVY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        countLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        countLabel.setForeground(new Color(75, 90, 110));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(countLabel);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filters.setBackground(OpenForm.BACKGROUND);

        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setFont(new Font("Arial", Font.BOLD, 15));
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        genreBox.setFont(new Font("Arial", Font.PLAIN, 15));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { refresh(); }
            public void removeUpdate(DocumentEvent e)  { refresh(); }
            public void changedUpdate(DocumentEvent e) { refresh(); }
        });
        genreBox.addActionListener(e -> refresh());

        filters.add(searchLbl);
        filters.add(searchField);
        filters.add(genreBox);

        header.add(left, BorderLayout.WEST);
        header.add(filters, BorderLayout.EAST);
        return header;
    }

    // Rebuild the card grid based on search text + genre
    private void refresh() {
        String query = searchField.getText().trim().toLowerCase(Locale.ROOT);
        String genre = (String) genreBox.getSelectedItem();

        grid.removeAll();
        int shown = 0;

        for (Movie m : allMovies) {
            boolean matchesGenre = "All Genres".equals(genre) || m.getGenre().equals(genre);
            boolean matchesText = m.getTitle().toLowerCase(Locale.ROOT).contains(query);

            if (matchesGenre && matchesText) {
                grid.add(createMovieCard(m));
                shown++;
            }
        }

        // pad so a single result doesn't stretch across the whole row
        while (shown > 0 && shown % 3 != 0) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            grid.add(filler);
            shown++;
        }

        int found = 0;
        for (Movie m : allMovies) {
            boolean g = "All Genres".equals(genre) || m.getGenre().equals(genre);
            if (g && m.getTitle().toLowerCase(Locale.ROOT).contains(query)) found++;
        }
        countLabel.setText(found + (found == 1 ? " movie available" : " movies available"));

        grid.revalidate();
        grid.repaint();
    }

    // MOVIE CARD (also used by the Home page)
    public static JPanel createMovieCard(Movie movie) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(10, 10, 15, 10));

        JLabel poster = new JLabel(movie.getEmoji());
        poster.setFont(OpenForm.emojiFont(65));
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setOpaque(true);
        poster.setBackground(new Color(210, 220, 235));
        poster.setPreferredSize(new Dimension(200, 180));
        poster.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel(movie.getTitle());
        name.setFont(new Font("Arial", Font.BOLD, 15));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel(movie.getShortInfo());
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel price = new JLabel("₱" + String.format("%.2f", movie.getPrice()) + " / ticket");
        price.setFont(new Font("Arial", Font.PLAIN, 13));
        price.setForeground(new Color(75, 90, 110));
        price.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton view = new JButton("View Details");
        view.setAlignmentX(Component.CENTER_ALIGNMENT);
        view.setMaximumSize(new Dimension(200, 40));
        view.addActionListener(e -> showDetails(card, movie));

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(6));
        card.add(info);
        card.add(Box.createVerticalStrut(4));
        card.add(price);
        card.add(Box.createVerticalStrut(12));
        card.add(view);

        return card;
    }

    // DETAILS / BOOKING DIALOG
    public static void showDetails(Component parent, Movie movie) {

        Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, movie.getTitle(),
                Dialog.ModalityType.APPLICATION_MODAL);

        JPanel root = new JPanel(new BorderLayout(20, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Poster
        JLabel poster = new JLabel(movie.getEmoji());
        poster.setFont(OpenForm.emojiFont(90));
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setOpaque(true);
        poster.setBackground(new Color(210, 220, 235));
        poster.setPreferredSize(new Dimension(220, 300));
        root.add(poster, BorderLayout.WEST);

        // Info column
        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(movie.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(OpenForm.NAVY);

        JLabel meta = new JLabel(movie.getGenre() + "  |  " + movie.getDuration()
                + " min  |  " + movie.getRating());
        meta.setFont(new Font("Arial", Font.PLAIN, 15));
        meta.setForeground(new Color(75, 90, 110));

        JTextArea synopsis = new JTextArea(movie.getSynopsis());
        synopsis.setFont(new Font("Arial", Font.PLAIN, 14));
        synopsis.setLineWrap(true);
        synopsis.setWrapStyleWord(true);
        synopsis.setEditable(false);
        synopsis.setOpaque(false);
        synopsis.setRows(4);
        synopsis.setColumns(26);

        JLabel timeLbl = new JLabel("Schedule:");
        timeLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JComboBox<String> timeBox = new JComboBox<>(movie.getShowtimes());
        timeBox.setMaximumSize(new Dimension(200, 30));

        JLabel qtyLbl = new JLabel("Tickets:");
        qtyLbl.setFont(new Font("Arial", Font.BOLD, 14));
        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        qty.setMaximumSize(new Dimension(80, 30));

        JLabel total = new JLabel();
        total.setFont(new Font("Arial", Font.BOLD, 18));
        total.setForeground(OpenForm.NAVY);
        Runnable updateTotal = () -> total.setText("Total: ₱" + String.format("%.2f",
                movie.getPrice() * (Integer) qty.getValue()));
        qty.addChangeListener(e -> updateTotal.run());
        updateTotal.run();

        JButton reserve = new JButton("Reserve Tickets");
        reserve.setFont(new Font("Arial", Font.BOLD, 15));
        reserve.setBackground(OpenForm.NAVY);
        reserve.setForeground(Color.WHITE);
        reserve.setFocusPainted(false);
        reserve.setMaximumSize(new Dimension(200, 40));
        reserve.addActionListener(e -> {
            String showtime = (String) timeBox.getSelectedItem();
            int tickets = (Integer) qty.getValue();

            try {
                // throws IllegalStateException if sold out / not enough seats
                movie.reserve(showtime, tickets);

                int remaining = movie.getSeatsLeft(showtime);
                String seatsMsg = (remaining == 0)
                        ? "That was the last of the tickets. This showing is now SOLD OUT."
                        : "Seats still available for this showing: " + remaining;

                // TODO: save the reservation (database/file) and open seat selection
                JOptionPane.showMessageDialog(dialog,
                        "Reserved " + tickets + " ticket(s) for\n"
                                + movie.getTitle() + " at " + showtime + "\n"
                                + total.getText() + "\n\n"
                                + seatsMsg,
                        "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

            } catch (IllegalStateException ex) {
                // dialog stays open so the user can change showtime or quantity
                JOptionPane.showMessageDialog(dialog,
                        ex.getMessage(),
                        "Not Enough Tickets", JOptionPane.ERROR_MESSAGE);
            }
        });

        for (JComponent c : new JComponent[]{title, meta, synopsis, timeLbl,
                timeBox, qtyLbl, qty, total, reserve}) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        info.add(title);
        info.add(Box.createVerticalStrut(6));
        info.add(meta);
        info.add(Box.createVerticalStrut(15));
        info.add(synopsis);
        info.add(Box.createVerticalStrut(15));
        info.add(timeLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(timeBox);
        info.add(Box.createVerticalStrut(10));
        info.add(qtyLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(qty);
        info.add(Box.createVerticalGlue());
        info.add(total);
        info.add(Box.createVerticalStrut(10));
        info.add(reserve);

        root.add(info, BorderLayout.CENTER);

        dialog.setContentPane(root);
        dialog.setSize(700, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }
}