package com.myticket.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BlueTheme {

    public static final Color DEEP_NAVY = new Color(15, 32, 60);
    public static final Color NAVY = new Color(23, 48, 86);
    public static final Color ROYAL_BLUE = new Color(37, 99, 235);
    public static final Color ROYAL_BLUE_HOVER = new Color(29, 78, 216);
    public static final Color SKY_BLUE = new Color(219, 234, 254);
    public static final Color SOFT_ICE = new Color(239, 246, 255);
    public static final Color APP_BG = new Color(241, 245, 252);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER_BLUE = new Color(191, 219, 254);
    public static final Color TEXT_DARK = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(71, 85, 105);
    public static final Color NAV_IDLE = new Color(33, 62, 108);
    public static final Color NAV_HOVER = new Color(48, 86, 145);
    public static final Color DANGER_BG = new Color(185, 28, 28);
    public static final Color DANGER_HOVER = new Color(153, 27, 27);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    private BlueTheme() {
    }

    public static void stylePrimaryButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(ROYAL_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(9, 16, 9, 16));
        addHoverColor(button, ROYAL_BLUE, ROYAL_BLUE_HOVER);
    }

    public static void styleSecondaryButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(SOFT_ICE);
        button.setForeground(NAVY);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
                new LineBorder(BORDER_BLUE, 1, true),
                new EmptyBorder(8, 15, 8, 15)
        ));
        addHoverColor(button, SOFT_ICE, SKY_BLUE);
    }

    public static void styleDangerButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(DANGER_BG);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(9, 16, 9, 16));
        addHoverColor(button, DANGER_BG, DANGER_HOVER);
    }

    public static void styleNavButton(JButton button, boolean isExit) {
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        Color base = isExit ? DANGER_BG : NAV_IDLE;
        Color hover = isExit ? DANGER_HOVER : NAV_HOVER;
        button.setBackground(base);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
        addHoverColor(button, base, hover);
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_DARK);
        field.setBackground(CARD_BG);
        field.setCaretColor(ROYAL_BLUE);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_BLUE, 1, true),
                new EmptyBorder(6, 8, 6, 8)
        ));
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_BODY);
        area.setForeground(TEXT_DARK);
        area.setBackground(CARD_BG);
        area.setCaretColor(ROYAL_BLUE);
    }

    public static <T> void styleList(JList<T> list) {
        list.setFont(FONT_BODY);
        list.setBackground(CARD_BG);
        list.setForeground(TEXT_DARK);
        list.setSelectionBackground(SKY_BLUE);
        list.setSelectionForeground(DEEP_NAVY);
    }

    public static <T> void styleComboBox(JComboBox<T> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setBackground(CARD_BG);
        comboBox.setForeground(TEXT_DARK);
    }

    public static Border createBlueTitledBorder(String title) {
        return new CompoundBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(BORDER_BLUE, 1, true),
                        title,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        FONT_SECTION,
                        NAVY
                ),
                new EmptyBorder(8, 10, 10, 10)
        );
    }

    public static Border createCardBorder() {
        return new CompoundBorder(
                new LineBorder(BORDER_BLUE, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        );
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(BORDER_BLUE, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
    }

    private static void addHoverColor(JButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normal);
                }
            }
        });
    }
}