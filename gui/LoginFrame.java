package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import DB_connections.StudentDB;

public class LoginFrame extends JFrame {

    private float bgPhase = 0f;
    private Timer bgTimer;

    public LoginFrame() {
        UITheme.applyGlobalTheme();

        setTitle("Placement Cell — Login");
        setSize(520, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(UITheme.BG_PRIMARY);

        // ──── Main wrapper with animated gradient background ────
        JPanel wrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Base dark gradient
                g2.setPaint(new GradientPaint(0, 0, UITheme.BG_PRIMARY, w, h, new Color(0x08, 0x08, 0x0A)));
                g2.fillRect(0, 0, w, h);

                // Animated accent orbs
                float phase = bgPhase;
                float orbX1 = w * 0.3f + (float)(Math.sin(phase) * w * 0.15);
                float orbY1 = h * 0.2f + (float)(Math.cos(phase * 0.7) * h * 0.1);
                float orbX2 = w * 0.7f + (float)(Math.sin(phase * 0.5 + 2) * w * 0.15);
                float orbY2 = h * 0.7f + (float)(Math.cos(phase * 0.8 + 1) * h * 0.1);

                // Blue orb
                RadialGradientPaint orb1 = new RadialGradientPaint(
                    new Point2D.Float(orbX1, orbY1), 180,
                    new float[]{0f, 1f},
                    new Color[]{new Color(0x0A, 0x84, 0xFF, 25), new Color(0x0A, 0x84, 0xFF, 0)});
                g2.setPaint(orb1);
                g2.fillOval((int)(orbX1 - 180), (int)(orbY1 - 180), 360, 360);

                // Purple orb
                RadialGradientPaint orb2 = new RadialGradientPaint(
                    new Point2D.Float(orbX2, orbY2), 200,
                    new float[]{0f, 1f},
                    new Color[]{new Color(0x5E, 0x5C, 0xE6, 20), new Color(0x5E, 0x5C, 0xE6, 0)});
                g2.setPaint(orb2);
                g2.fillOval((int)(orbX2 - 200), (int)(orbY2 - 200), 400, 400);

                g2.dispose();
            }
        };
        wrapper.setOpaque(false);

        // Animate background
        bgTimer = new Timer(50, e -> {
            bgPhase += 0.02f;
            wrapper.repaint();
        });
        bgTimer.start();

        // ──── Glassmorphism Card ────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Shadow
                for (int i = 3; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 10 * i));
                    g2.fill(new RoundRectangle2D.Float(i * 2, i * 2, w - 2, h - 2, 24, 24));
                }

                // Glass background
                g2.setColor(UITheme.BG_GLASS);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));

                // Subtle inner border glow
                g2.setColor(new Color(255, 255, 255, 8));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 24, 24));

                // Top accent gradient line
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT, w, 0, UITheme.ACCENT_PURPLE));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, 3, 3, 3));

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 520));

        // ──── Logo & Title ────
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(36, 0, 10, 0));

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(icon);

        titlePanel.add(Box.createVerticalStrut(10));

        // Gradient title
        JLabel title = new JLabel("Placement Cell") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                FontMetrics fm = g2.getFontMetrics(getFont());
                g2.setFont(getFont());
                int textW = fm.stringWidth(getText());
                int x = (getWidth() - textW) / 2;
                int y = fm.getAscent();
                g2.setPaint(new GradientPaint(x, 0, UITheme.TEXT_PRIMARY, x + textW, 0,
                        new Color(0x8E, 0x8E, 0xF7)));
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setPreferredSize(new Dimension(400, 36));
        title.setMaximumSize(new Dimension(400, 36));
        titlePanel.add(title);

        titlePanel.add(Box.createVerticalStrut(4));

        JLabel subtitle = new JLabel("Sign in to your account");
        subtitle.setFont(UITheme.FONT_SMALL);
        subtitle.setForeground(UITheme.TEXT_TERTIARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(subtitle);

        card.add(titlePanel);
        card.add(Box.createVerticalStrut(16));

        // ──── Form Fields ────
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Role selector
        JLabel roleLabel = createFieldLabel("ROLE");
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(6));
        JComboBox<String> roleBox = UITheme.createStyledComboBox(new String[]{"Admin", "Student"});
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(roleBox);
        formPanel.add(Box.createVerticalStrut(16));

        // Username
        JLabel userLabel = createFieldLabel("USERNAME / ROLL NO");
        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(6));
        JTextField userField = UITheme.createStyledTextField("Enter your username or roll number");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(16));

        // Password
        JLabel passLabel = createFieldLabel("PASSWORD");
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(6));
        JPasswordField passField = UITheme.createStyledPasswordField("Enter your password");
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(passField);

        card.add(formPanel);
        card.add(Box.createVerticalStrut(28));

        // ──── Buttons ────
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 36, 40));

        JButton loginBtn = UITheme.createPrimaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.add(loginBtn);

        btnPanel.add(Box.createVerticalStrut(12));

        JButton registerBtn = UITheme.createSecondaryButton("Create Account");
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.add(registerBtn);

        card.add(btnPanel);

        wrapper.add(card);
        add(wrapper);

        // ═══════════════════ ACTION LISTENERS (unchanged logic) ═══════════════════

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                UITheme.showMessage(this, "Please fill all fields.");
                return;
            }
            if ("Admin".equals(roleBox.getSelectedItem())) {
                if ("admin".equals(user) && "admin".equals(pass)) {
                    bgTimer.stop();
                    dispose();
                    new AdminDashboard();
                } else {
                    UITheme.showMessage(this, "Invalid admin credentials.");
                }
            } else {
                try {
                    ResultSet rs = StudentDB.validateStudent(user, pass);
                    if (rs != null && rs.next()) {
                        String name = rs.getString("name");
                        bgTimer.stop();
                        dispose();
                        new StudentDashboard(user, name);
                    } else {
                        UITheme.showMessage(this, "Invalid roll number or password.");
                    }
                } catch (SQLException ex) {
                    UITheme.showMessage(this, "Error: " + ex.getMessage());
                }
            }
        });

        registerBtn.addActionListener(e -> {
            JTextField fRoll = UITheme.createStyledTextField("e.g. CS2024001");
            JTextField fName = UITheme.createStyledTextField("Full name");
            JTextField fAge = UITheme.createStyledTextField("e.g. 21");
            JTextField fMajor = UITheme.createStyledTextField("e.g. Computer Science");
            JTextField fGPA = UITheme.createStyledTextField("e.g. 8.5");
            JTextField fEmail = UITheme.createStyledTextField("e.g. name@example.com");
            JPasswordField fPass = UITheme.createStyledPasswordField("Enter password");
            JPasswordField fConfirmPass = UITheme.createStyledPasswordField("Confirm password");
            Object[] fields = {"Roll No:", fRoll, "Name:", fName, "Age:", fAge,
                    "Major:", fMajor, "GPA:", fGPA, "Email:", fEmail, "Password:", fPass, "Confirm Password:", fConfirmPass};
            if (UITheme.showStyledDialog(this, "Student Registration", fields)) {
                String pass = new String(fPass.getPassword()).trim();
                String confirmPass = new String(fConfirmPass.getPassword()).trim();
                if (!pass.equals(confirmPass)) {
                    UITheme.showMessage(this, "Passwords do not match.");
                    return;
                }
                if (pass.isEmpty()) {
                    UITheme.showMessage(this, "Password cannot be empty.");
                    return;
                }
                try {
                    if (StudentDB.addStudent(fRoll.getText().trim(), fName.getText().trim(),
                            Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                            Double.parseDouble(fGPA.getText().trim()), fEmail.getText().trim(), pass)) {
                        UITheme.showMessage(this, "Registered! You can now login.");
                    } else {
                        UITheme.showMessage(this, "Registration failed.");
                    }
                } catch (NumberFormatException ex) {
                    UITheme.showMessage(this, "Age must be integer, GPA must be a number.");
                }
            }
        });

        setVisible(true);
    }

    /** Small uppercase field label. */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(UITheme.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
