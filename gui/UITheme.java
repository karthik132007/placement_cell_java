package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import com.toedter.calendar.JDateChooser;

/**
 * Premium dark theme for the Placement Cell Management System.
 * Features: gradient accents, glassmorphism, animations, stat cards,
 * search bars, icon nav, toast notifications, and custom scrollbars.
 */
public class UITheme {

    // ═══════════════════════ COLOR PALETTE ═══════════════════════
    public static final Color BG_PRIMARY     = new Color(0x0F, 0x0F, 0x11);       // near-black
    public static final Color BG_SECONDARY   = new Color(0x1A, 0x1A, 0x1E);       // card background
    public static final Color BG_TERTIARY    = new Color(0x26, 0x26, 0x2C);       // input fields
    public static final Color BG_GLASS       = new Color(0x1E, 0x1E, 0x24, 0xCC); // glassmorphism
    public static final Color ACCENT         = new Color(0x0A, 0x84, 0xFF);       // primary blue
    public static final Color ACCENT_PURPLE  = new Color(0x5E, 0x5C, 0xE6);       // purple
    public static final Color ACCENT_HOVER   = new Color(0x40, 0x9C, 0xFF);
    public static final Color TEXT_PRIMARY   = new Color(0xF5, 0xF5, 0xF7);
    public static final Color TEXT_SECONDARY = new Color(0x8E, 0x8E, 0x93);
    public static final Color TEXT_TERTIARY  = new Color(0x5A, 0x5A, 0x5E);
    public static final Color BORDER         = new Color(0x3A, 0x3A, 0x3E);
    public static final Color BORDER_GLOW    = new Color(0x0A, 0x84, 0xFF, 0x33);
    public static final Color DIVIDER        = new Color(0x2A, 0x2A, 0x2E);
    public static final Color ROW_ALT        = new Color(0x16, 0x16, 0x1A);
    public static final Color ROW_HOVER      = new Color(0x22, 0x22, 0x28);
    public static final Color SELECTION      = new Color(0x0A, 0x84, 0xFF, 0x33);
    public static final Color DANGER         = new Color(0xFF, 0x45, 0x3A);
    public static final Color DANGER_HOVER   = new Color(0xFF, 0x6B, 0x61);
    public static final Color SUCCESS        = new Color(0x30, 0xD1, 0x58);
    public static final Color WARNING        = new Color(0xFF, 0xD6, 0x0A);
    public static final Color SIDEBAR_BG     = new Color(0x0A, 0x0A, 0x0C);

    // Status badge colors
    public static final Color STATUS_APPLIED     = new Color(0x0A, 0x84, 0xFF);
    public static final Color STATUS_SHORTLISTED = new Color(0xFF, 0x9F, 0x0A);
    public static final Color STATUS_ACCEPTED    = new Color(0x30, 0xD1, 0x58);
    public static final Color STATUS_REJECTED    = new Color(0xFF, 0x45, 0x3A);

    // Stat card accents
    public static final Color STAT_BLUE   = new Color(0x00, 0x7A, 0xFF);
    public static final Color STAT_PURPLE = new Color(0xBF, 0x5A, 0xF2);
    public static final Color STAT_GREEN  = new Color(0x30, 0xD1, 0x58);
    public static final Color STAT_ORANGE = new Color(0xFF, 0x9F, 0x0A);

    // ═══════════════════════ FONTS ═══════════════════════
    public static final Font FONT_TITLE       = new Font("SansSerif", Font.BOLD,  28);
    public static final Font FONT_HEADING     = new Font("SansSerif", Font.BOLD,  18);
    public static final Font FONT_SUBHEADING  = new Font("SansSerif", Font.BOLD,  15);
    public static final Font FONT_BODY        = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL       = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_TINY        = new Font("SansSerif", Font.PLAIN, 10);
    public static final Font FONT_BUTTON      = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_NAV         = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_NAV_ACTIVE  = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_TABLE_HEADER = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_STAT_VALUE  = new Font("SansSerif", Font.BOLD,  32);
    public static final Font FONT_STAT_LABEL  = new Font("SansSerif", Font.PLAIN, 12);

    // ═══════════════════════ DIMENSIONS ═══════════════════════
    public static final int CORNER_RADIUS = 14;
    public static final int SIDEBAR_WIDTH = 220;

    // ═══════════════════════ GLOBAL SETUP ═══════════════════════
    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        UIManager.put("Panel.background", BG_PRIMARY);
        UIManager.put("OptionPane.background", BG_SECONDARY);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.messageFont", FONT_BODY);
        UIManager.put("OptionPane.buttonFont", FONT_BUTTON);
        UIManager.put("TextField.background", BG_TERTIARY);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("TextField.font", FONT_BODY);
        UIManager.put("PasswordField.background", BG_TERTIARY);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", TEXT_PRIMARY);
        UIManager.put("PasswordField.font", FONT_BODY);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", FONT_BODY);
        UIManager.put("ComboBox.background", BG_TERTIARY);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", ACCENT);
        UIManager.put("ComboBox.selectionForeground", TEXT_PRIMARY);
        UIManager.put("ComboBox.font", FONT_BODY);
        UIManager.put("Button.font", FONT_BUTTON);
        UIManager.put("ScrollBar.thumb", BG_TERTIARY);
        UIManager.put("ScrollBar.track", BG_PRIMARY);
        UIManager.put("ScrollPane.background", BG_PRIMARY);
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Viewport.background", BG_PRIMARY);
        UIManager.put("Table.background", BG_SECONDARY);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", SELECTION);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BG_SECONDARY);
        UIManager.put("Table.font", FONT_BODY);
        UIManager.put("TableHeader.background", BG_SECONDARY);
        UIManager.put("TableHeader.foreground", TEXT_SECONDARY);
        UIManager.put("TableHeader.font", FONT_TABLE_HEADER);
        // ScrollBar UI
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("ScrollBar.thumbArc", 10);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
    }

    // ═══════════════════════ COLOR UTILITIES ═══════════════════════

    /** Interpolates between two colors. t in [0, 1]. */
    public static Color lerpColor(Color a, Color b, float t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t);
        int g = (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl= (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
        int al= (int)(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t);
        return new Color(r, g, bl, al);
    }

    /** Creates a horizontal gradient paint. */
    public static GradientPaint accentGradient(int x, int y, int w) {
        return new GradientPaint(x, y, ACCENT, x + w, y, ACCENT_PURPLE);
    }

    // ═══════════════════════ ANIMATED BUTTONS ═══════════════════════

    public static JButton createPrimaryButton(String text) {
        return createGradientButton(text, ACCENT, ACCENT_PURPLE, ACCENT_HOVER, Color.WHITE);
    }

    public static JButton createDangerButton(String text) {
        return createGradientButton(text, DANGER, new Color(0xCC, 0x22, 0x22), DANGER_HOVER, Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, BG_TERTIARY, BORDER, TEXT_PRIMARY);
    }

    private static JButton createGradientButton(String text, Color c1, Color c2, Color hoverBg, Color fg) {
        JButton btn = new JButton(text) {
            private float hoverAnim = 0f;
            private Timer timer;
            private boolean hovered = false;
            {
                setOpaque(false); setContentAreaFilled(false);
                setBorderPainted(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setFont(FONT_BUTTON); setForeground(fg);
                timer = new Timer(16, e -> {
                    float target = hovered ? 1f : 0f;
                    hoverAnim += (target - hoverAnim) * 0.2f;
                    if (Math.abs(hoverAnim - target) < 0.01f) {
                        hoverAnim = target;
                        timer.stop();
                    }
                    repaint();
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; timer.start(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Hover glow
                if (hoverAnim > 0.01f) {
                    g2.setColor(new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), (int)(40 * hoverAnim)));
                    g2.fill(new RoundRectangle2D.Float(-3, -3, w + 6, h + 6, 26, 26));
                }
                // Gradient fill
                Color startC = lerpColor(c1, hoverBg, hoverAnim * 0.3f);
                g2.setPaint(new GradientPaint(0, 0, startC, w, 0, c2));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 22, 22));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    private static JButton createStyledButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton btn = new JButton(text) {
            private float hoverAnim = 0f;
            private Timer timer;
            private boolean hovered = false;
            {
                setOpaque(false); setContentAreaFilled(false);
                setBorderPainted(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setFont(FONT_BUTTON); setForeground(fg);
                timer = new Timer(16, e -> {
                    float target = hovered ? 1f : 0f;
                    hoverAnim += (target - hoverAnim) * 0.2f;
                    if (Math.abs(hoverAnim - target) < 0.01f) {
                        hoverAnim = target;
                        timer.stop();
                    }
                    repaint();
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; timer.start(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = lerpColor(bg, hoverBg, hoverAnim);
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 22, 22));
                // Subtle border
                g2.setColor(new Color(255, 255, 255, (int)(15 + 20 * hoverAnim)));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 22, 22));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    // ═══════════════════════ ICON SIDEBAR NAV BUTTON ═══════════════════════

    public static JButton createNavButton(String icon, String text, boolean active) {
        JButton btn = new JButton(icon + "  " + text) {
            private float hoverAnim = 0f;
            private Timer timer;
            private boolean hovered = false;
            {
                setOpaque(false); setContentAreaFilled(false);
                setBorderPainted(false); setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setFont(active ? FONT_NAV_ACTIVE : FONT_NAV);
                setForeground(active ? TEXT_PRIMARY : TEXT_SECONDARY);
                setHorizontalAlignment(SwingConstants.LEFT);
                timer = new Timer(16, e -> {
                    float target = hovered ? 1f : 0f;
                    hoverAnim += (target - hoverAnim) * 0.2f;
                    if (Math.abs(hoverAnim - target) < 0.01f) {
                        hoverAnim = target;
                        timer.stop();
                    }
                    repaint();
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; timer.start(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                if (active) {
                    // Gradient highlight
                    g2.setPaint(new GradientPaint(4, 0, new Color(0x0A, 0x84, 0xFF, 0x28), w - 8, 0, new Color(0x5E, 0x5C, 0xE6, 0x10)));
                    g2.fill(new RoundRectangle2D.Float(4, 2, w - 8, h - 4, 10, 10));
                    // Active indicator bar
                    g2.setPaint(new GradientPaint(0, 6, ACCENT, 0, h - 6, ACCENT_PURPLE));
                    g2.fill(new RoundRectangle2D.Float(0, 6, 3, h - 12, 3, 3));
                } else if (hoverAnim > 0.01f) {
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, (int)(13 * hoverAnim)));
                    g2.fill(new RoundRectangle2D.Float(4, 2, w - 8, h - 4, 10, 10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 42));
        btn.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        return btn;
    }

    // ═══════════════════════ STYLED TABLE (hover rows) ═══════════════════════

    public static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            private int hoveredRow = -1;
            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int row = rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            repaint();
                        }
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hoveredRow = -1;
                        repaint();
                    }
                });
            }
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(SELECTION);
                } else if (row == hoveredRow) {
                    c.setBackground(ROW_HOVER);
                } else {
                    c.setBackground(row % 2 == 0 ? BG_SECONDARY : ROW_ALT);
                }
                c.setForeground(TEXT_PRIMARY);
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                }
                return c;
            }
        };

        table.setFont(FONT_BODY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(BG_SECONDARY);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(SELECTION);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_PRIMARY);
        header.setForeground(TEXT_SECONDARY);
        header.setFont(FONT_TABLE_HEADER);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 44));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setBackground(new Color(0x12, 0x12, 0x15));
                l.setForeground(TEXT_SECONDARY);
                l.setFont(FONT_TABLE_HEADER);
                l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                l.setHorizontalAlignment(SwingConstants.LEFT);
                return l;
            }
        });

        return table;
    }

    public static JScrollPane createStyledScrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(DIVIDER, 1, true));
        sp.getViewport().setBackground(BG_SECONDARY);
        sp.setBackground(BG_SECONDARY);
        return sp;
    }

    // ═══════════════════════ STYLED TEXT FIELD ═══════════════════════

    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            private float focusAnim = 0f;
            private Timer timer;
            private boolean focused = false;
            {
                timer = new Timer(16, e -> {
                    float target = focused ? 1f : 0f;
                    focusAnim += (target - focusAnim) * 0.2f;
                    if (Math.abs(focusAnim - target) < 0.01f) {
                        focusAnim = target;
                        timer.stop();
                    }
                    repaint();
                });
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true; timer.start(); }
                    public void focusLost(FocusEvent e)   { focused = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                // Focus glow border
                if (focusAnim > 0.01f) {
                    g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), (int)(60 * focusAnim)));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
                }
                g2.dispose();
                super.paintComponent(g);
                // Placeholder text
                if (getText().isEmpty() && !hasFocus() && placeholder != null && !placeholder.isEmpty()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g3.setColor(TEXT_TERTIARY);
                    g3.setFont(FONT_BODY);
                    FontMetrics fm = g3.getFontMetrics();
                    g3.drawString(placeholder, getInsets().left + 4, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g3.dispose();
                }
            }
        };
        field.setOpaque(false);
        field.setBackground(BG_TERTIARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        field.setPreferredSize(new Dimension(280, 40));
        return field;
    }

    // ═══════════════════════ STYLED DATE/TIME SPINNERS ═══════════════════════

    public static JSpinner createStyledDateSpinner(java.util.Date date) {
        SpinnerDateModel model = new SpinnerDateModel(date == null ? new java.util.Date() : date, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);

        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setBackground(BG_TERTIARY);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(ACCENT);
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        spinner.setOpaque(false);
        spinner.setBackground(BG_TERTIARY);
        spinner.setPreferredSize(new Dimension(280, 40));
        return spinner;
    }

    public static JSpinner createStyledTimeSpinner(java.util.Date date) {
        SpinnerDateModel model = new SpinnerDateModel(date == null ? new java.util.Date() : date, null, null, java.util.Calendar.SECOND);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        spinner.setEditor(editor);

        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setBackground(BG_TERTIARY);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(ACCENT);
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        spinner.setOpaque(false);
        spinner.setBackground(BG_TERTIARY);
        spinner.setPreferredSize(new Dimension(280, 40));
        return spinner;
    }

    public static JDateChooser createStyledDateChooser(java.util.Date date) {
        JDateChooser chooser = new JDateChooser(date == null ? new java.util.Date() : date);
        chooser.setDateFormatString("yyyy-MM-dd");
        chooser.setOpaque(false);
        chooser.setBackground(BG_TERTIARY);
        chooser.setForeground(TEXT_PRIMARY);
        chooser.setFont(FONT_BODY);
        
        JTextField textField = (JTextField) chooser.getDateEditor().getUiComponent();
        textField.setBackground(BG_TERTIARY);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(ACCENT);
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        chooser.setPreferredSize(new Dimension(280, 40));
        return chooser;
    }

    // ═══════════════════════ STYLED PASSWORD FIELD ═══════════════════════

    public static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            private float focusAnim = 0f;
            private Timer timer;
            private boolean focused = false;
            {
                timer = new Timer(16, e -> {
                    float target = focused ? 1f : 0f;
                    focusAnim += (target - focusAnim) * 0.2f;
                    if (Math.abs(focusAnim - target) < 0.01f) {
                        focusAnim = target;
                        timer.stop();
                    }
                    repaint();
                });
                addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) { focused = true; timer.start(); }
                    public void focusLost(FocusEvent e)   { focused = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                if (focusAnim > 0.01f) {
                    g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), (int)(60 * focusAnim)));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
                }
                g2.dispose();
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus() && placeholder != null) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g3.setColor(TEXT_TERTIARY);
                    g3.setFont(FONT_BODY);
                    FontMetrics fm = g3.getFontMetrics();
                    g3.drawString(placeholder, getInsets().left + 4, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g3.dispose();
                }
            }
        };
        field.setOpaque(false);
        field.setBackground(BG_TERTIARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        field.setPreferredSize(new Dimension(280, 40));
        return field;
    }

    // ═══════════════════════ SEARCH FIELD ═══════════════════════

    public static JTextField createSearchField(String placeholder) {
        JTextField field = createStyledTextField(placeholder);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 32, 8, 14)));
        field.setPreferredSize(new Dimension(260, 38));

        // Wrap in a panel with a search icon
        return field;
    }

    /** Creates a search panel with a search icon and text field. */
    public static JPanel createSearchPanel(String placeholder, JTextField[] fieldOut) {
        JTextField searchField = createStyledTextField(placeholder);
        searchField.setPreferredSize(new Dimension(260, 38));
        if (fieldOut != null && fieldOut.length > 0) fieldOut[0] = searchField;

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                // Transparent
            }
        };
        panel.setOpaque(false);

        JLabel searchIcon = new JLabel("🔍") {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(TEXT_TERTIARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("⌕", 8, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        searchIcon.setPreferredSize(new Dimension(28, 38));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(searchIcon, BorderLayout.WEST);
        wrapper.add(searchField, BorderLayout.CENTER);
        panel.add(wrapper);
        return panel;
    }

    // ═══════════════════════ STYLED COMBOBOX ═══════════════════════

    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(BG_TERTIARY);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_BODY);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBackground(isSelected ? ACCENT : BG_TERTIARY);
                l.setForeground(TEXT_PRIMARY);
                l.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                l.setFont(FONT_BODY);
                return l;
            }
        });
        return combo;
    }

    // ═══════════════════════ STAT CARD ═══════════════════════

    /**
     * Creates a dashboard stat card with icon, label, value, and accent color.
     */
    public static JPanel createStatCard(String icon, String label, String value, Color accent) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Card background
                g2.setColor(BG_SECONDARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                // Subtle gradient overlay at top
                g2.setPaint(new GradientPaint(0, 0, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20),
                        0, h / 2, new Color(0, 0, 0, 0)));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
                // Bottom accent line
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0, h - 3, w, 3, 0, 0));
                // Subtle border
                g2.setColor(new Color(255, 255, 255, 8));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 14, 20));
        card.setPreferredSize(new Dimension(200, 120));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(FONT_STAT_VALUE);
        valLabel.setForeground(TEXT_PRIMARY);
        valLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valLabel);
        card.add(Box.createVerticalStrut(2));

        JLabel descLabel = new JLabel(label);
        descLabel.setFont(FONT_STAT_LABEL);
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        return card;
    }

    // ═══════════════════════ STATUS BADGE RENDERER ═══════════════════════

    /** Returns a table cell renderer that draws pill-shaped status badges. */
    public static TableCellRenderer createStatusBadgeRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                String status = (value != null) ? value.toString() : "";
                Color badgeColor = getStatusColor(status);
                JLabel l = new JLabel(status) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        // Badge background
                        g2.setColor(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 30));
                        FontMetrics fm = g2.getFontMetrics(getFont());
                        int textW = fm.stringWidth(getText());
                        int badgeW = textW + 20;
                        int badgeH = 26;
                        int x = 8;
                        int y = (getHeight() - badgeH) / 2;
                        g2.fill(new RoundRectangle2D.Float(x, y, badgeW, badgeH, 14, 14));
                        // Badge text
                        g2.setColor(badgeColor);
                        g2.setFont(getFont());
                        g2.drawString(getText(), x + 10, y + fm.getAscent() + (badgeH - fm.getHeight()) / 2);
                        g2.dispose();
                    }
                };
                l.setFont(FONT_BUTTON);
                l.setOpaque(true);
                if (isSelected) {
                    l.setBackground(SELECTION);
                } else {
                    l.setBackground(row % 2 == 0 ? BG_SECONDARY : ROW_ALT);
                }
                l.setForeground(new Color(0,0,0,0)); // hide default text, we paint custom
                l.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                return l;
            }
        };
    }

    // ═══════════════════════ PANELS & LAYOUT HELPERS ═══════════════════════

    public static JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, DIVIDER));
        return sidebar;
    }

    /** Creates a gradient header bar. */
    public static JPanel createHeaderPanel(String title) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Subtle gradient
                g2.setPaint(new GradientPaint(0, 0, new Color(0x14, 0x14, 0x18), w, 0, new Color(0x10, 0x10, 0x14)));
                g2.fillRect(0, 0, w, h);
                // Bottom border with gradient accent
                g2.setPaint(new GradientPaint(0, h - 1, new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 40),
                        w, h - 1, new Color(ACCENT_PURPLE.getRed(), ACCENT_PURPLE.getGreen(), ACCENT_PURPLE.getBlue(), 20)));
                g2.fillRect(0, h - 1, w, 1);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(18, 28, 18, 28));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_HEADING);
        titleLabel.setForeground(TEXT_PRIMARY);
        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    /** Creates a glassmorphism card panel. */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Card background
                g2.setColor(BG_GLASS);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, CORNER_RADIUS, CORNER_RADIUS));
                // Subtle border glow
                g2.setColor(new Color(255, 255, 255, 10));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, CORNER_RADIUS, CORNER_RADIUS));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    /** Action bar with better spacing. */
    public static JPanel createActionBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bar.setOpaque(false);
        bar.setBackground(BG_PRIMARY);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        for (JButton b : buttons) bar.add(b);
        return bar;
    }

    // ═══════════════════════ STYLED FORM DIALOG ═══════════════════════

    public static boolean showStyledDialog(Component parent, String title, Object[] labelFieldPairs) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title,
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(false);
        dialog.getContentPane().setBackground(BG_SECONDARY);
        dialog.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BG_SECONDARY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 28, 8, 28));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_HEADING);
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(titleLabel);
        dialog.add(titlePanel, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_SECONDARY);
        form.setBorder(BorderFactory.createEmptyBorder(8, 28, 8, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 16);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        for (int i = 0; i < labelFieldPairs.length; i += 2) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            JLabel label = new JLabel(labelFieldPairs[i].toString());
            label.setFont(new Font("SansSerif", Font.BOLD, 11));
            label.setForeground(TEXT_SECONDARY);
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            Component field = (Component) labelFieldPairs[i + 1];
            form.add(field, gbc);
            row++;
        }
        dialog.add(form, BorderLayout.CENTER);

        // Buttons
        final boolean[] result = {false};
        JButton okBtn = createPrimaryButton("Confirm");
        JButton cancelBtn = createSecondaryButton("Cancel");
        okBtn.addActionListener(e -> { result[0] = true; dialog.dispose(); });
        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(BG_SECONDARY);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 28, 20, 28));
        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setMinimumSize(new Dimension(460, dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return result[0];
    }

    // ═══════════════════════ TOAST NOTIFICATION ═══════════════════════

    /**
     * Shows an animated toast notification that auto-dismisses.
     * Fades in from bottom, stays 2s, fades out.
     */
    public static void showToast(Component parent, String message) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        if (window == null && parent instanceof Window) window = (Window) parent;
        if (window == null) {
            // Fallback
            JOptionPane.showMessageDialog(parent, message);
            return;
        }

        final Window parentWindow = window;
        JWindow toast = new JWindow(parentWindow);
        toast.setBackground(new Color(0, 0, 0, 0));

        JPanel content = new JPanel() {
            private float alpha = 0f;
            {
                // Fade in
                Timer fadeIn = new Timer(16, null);
                fadeIn.addActionListener(e -> {
                    alpha = Math.min(1f, alpha + 0.08f);
                    repaint();
                    if (alpha >= 1f) fadeIn.stop();
                });
                fadeIn.start();

                // Auto dismiss after 2.5s
                Timer dismiss = new Timer(2500, e -> {
                    Timer fadeOut = new Timer(16, null);
                    fadeOut.addActionListener(ev -> {
                        alpha = Math.max(0f, alpha - 0.08f);
                        repaint();
                        if (alpha <= 0f) {
                            fadeOut.stop();
                            toast.dispose();
                        }
                    });
                    fadeOut.start();
                });
                dismiss.setRepeats(false);
                dismiss.start();
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                // Background
                g2.setColor(new Color(0x2A, 0x2A, 0x2E, 0xF0));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                // Border
                g2.setColor(new Color(255, 255, 255, 15));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 16, 16));
                // Accent line at left
                g2.setPaint(new GradientPaint(0, 4, ACCENT, 0, getHeight() - 4, ACCENT_PURPLE));
                g2.fill(new RoundRectangle2D.Float(0, 4, 3, getHeight() - 8, 3, 3));
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel msgLabel = new JLabel("  " + message);
        msgLabel.setFont(FONT_BODY);
        msgLabel.setForeground(TEXT_PRIMARY);
        content.add(msgLabel, BorderLayout.CENTER);

        toast.setContentPane(content);
        toast.pack();
        toast.setSize(Math.max(toast.getWidth(), 320), toast.getHeight());

        // Position at bottom-center of parent
        Point loc = parentWindow.getLocationOnScreen();
        int cx = loc.x + (parentWindow.getWidth() - toast.getWidth()) / 2;
        int cy = loc.y + parentWindow.getHeight() - toast.getHeight() - 30;
        toast.setLocation(cx, cy);
        toast.setVisible(true);
    }

    /** Legacy convenience — routes to toast. */
    public static void showMessage(Component parent, String message) {
        showToast(parent, message);
    }

    // ═══════════════════════ STATUS BADGE ═══════════════════════

    public static Color getStatusColor(String status) {
        if (status == null) return TEXT_SECONDARY;
        switch (status) {
            case "Applied":     return STATUS_APPLIED;
            case "Shortlisted": return STATUS_SHORTLISTED;
            case "Accepted":    return STATUS_ACCEPTED;
            case "Rejected":    return STATUS_REJECTED;
            default:            return TEXT_SECONDARY;
        }
    }
}
