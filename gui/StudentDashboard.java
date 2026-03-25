package gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import DB_connections.*;

public class StudentDashboard extends JFrame {

    private String rollNum;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private String activeSection = "Profile";
    private String studentName;

    // Nav config
    private static final String[] NAV_ICONS = {"👤", "🎯", "📝", "🔔", "📅"};
    private static final String[] NAV_LABELS = {"Profile", "Available Drives", "My Applications", "Notifications", "Interviews"};

    public StudentDashboard(String rollNum, String name) {
        UITheme.applyGlobalTheme();

        this.rollNum = rollNum;
        this.studentName = name;
        setTitle("Placement Cell — " + name);
        setSize(1050, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_PRIMARY);

        setLayout(new BorderLayout());

        // ──── Sidebar ────
        sidebarPanel = UITheme.createSidebarPanel();

        // Brand header with avatar
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(28, 22, 20, 22));

        // Avatar initial circle
        String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(initial) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight());
                // Gradient circle
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT, size, size, UITheme.ACCENT_PURPLE));
                g2.fillOval(0, 0, size, size);
                // Letter
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                int x = (size - fm.stringWidth(initial)) / 2;
                int y = (size + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, x, y);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(48, 48));
        avatar.setMinimumSize(new Dimension(48, 48));
        avatar.setMaximumSize(new Dimension(48, 48));
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(avatar);
        brandPanel.add(Box.createVerticalStrut(10));

        JLabel welcomeLabel = new JLabel("Welcome back");
        welcomeLabel.setFont(UITheme.FONT_TINY);
        welcomeLabel.setForeground(UITheme.TEXT_TERTIARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(welcomeLabel);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(UITheme.FONT_HEADING);
        nameLabel.setForeground(UITheme.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(nameLabel);

        JLabel rollLabel = new JLabel(rollNum);
        rollLabel.setFont(UITheme.FONT_TINY);
        rollLabel.setForeground(UITheme.TEXT_TERTIARY);
        rollLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(rollLabel);

        sidebarPanel.add(brandPanel);
        sidebarPanel.add(Box.createVerticalStrut(8));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.DIVIDER);
        sep.setBackground(UITheme.DIVIDER);
        sep.setMaximumSize(new Dimension(UITheme.SIDEBAR_WIDTH - 20, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(sep);
        sidebarPanel.add(Box.createVerticalStrut(12));

        // Nav section label
        JLabel navLabel = new JLabel("  MENU");
        navLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        navLabel.setForeground(UITheme.TEXT_TERTIARY);
        navLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 8, 0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLabel.setMaximumSize(new Dimension(UITheme.SIDEBAR_WIDTH, 20));
        sidebarPanel.add(navLabel);

        rebuildSidebar();

        sidebarPanel.add(Box.createVerticalGlue());

        // Logout
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.DIVIDER),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        logoutPanel.setMaximumSize(new Dimension(UITheme.SIDEBAR_WIDTH, 60));
        JButton logoutBtn = UITheme.createDangerButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(170, 36));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        logoutPanel.add(logoutBtn);
        sidebarPanel.add(logoutPanel);

        add(sidebarPanel, BorderLayout.WEST);

        // ──── Content area ────
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.BG_PRIMARY);
        add(contentPanel, BorderLayout.CENTER);

        showProfile();
        setVisible(true);
    }

    private void rebuildSidebar() {
        // Remove old nav buttons
        Component[] comps = sidebarPanel.getComponents();
        java.util.List<Component> toRemove = new java.util.ArrayList<>();
        boolean foundLabel = false;
        for (Component c : comps) {
            if (c instanceof JLabel && "  MENU".equals(((JLabel) c).getText())) {
                foundLabel = true;
                continue;
            }
            if (foundLabel && c instanceof JButton) {
                toRemove.add(c);
            }
        }
        for (Component c : toRemove) sidebarPanel.remove(c);

        int insertIdx = -1;
        for (int i = 0; i < sidebarPanel.getComponentCount(); i++) {
            Component c = sidebarPanel.getComponent(i);
            if (c instanceof JLabel && "  MENU".equals(((JLabel) c).getText())) {
                insertIdx = i + 1;
                break;
            }
        }

        int offset = 0;
        for (int i = 0; i < NAV_LABELS.length; i++) {
            final String section = NAV_LABELS[i];
            JButton navBtn = UITheme.createNavButton(NAV_ICONS[i], section, section.equals(activeSection));
            navBtn.addActionListener(e -> {
                activeSection = section;
                refreshContent();
                rebuildSidebar();
                sidebarPanel.revalidate();
                sidebarPanel.repaint();
            });
            if (insertIdx >= 0) {
                sidebarPanel.add(navBtn, insertIdx + offset);
                offset++;
            }
        }
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private void refreshContent() {
        switch (activeSection) {
            case "Profile":          showProfile(); break;
            case "Available Drives": showDrives(); break;
            case "My Applications":  showApplications(); break;
            case "Notifications":    showNotifications(); break;
            case "Interviews":       showInterviews(); break;
        }
    }

    // ──────────────────── Profile Section ────────────────────
    private void showProfile() {
        contentPanel.removeAll();

        JPanel header = UITheme.createHeaderPanel("My Profile");
        contentPanel.add(header, BorderLayout.NORTH);

        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setOpaque(false);

        // Profile card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Shadow layers
                for (int i = 3; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 8 * i));
                    g2.fill(new RoundRectangle2D.Float(i * 2, i * 2, w - 2, h - 2, 20, 20));
                }
                // Card background
                g2.setColor(UITheme.BG_SECONDARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 20, 20));
                // Subtle border
                g2.setColor(new Color(255, 255, 255, 8));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 20, 20));
                // Top accent line
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT, w, 0, UITheme.ACCENT_PURPLE));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, 3, 3, 3));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        card.setPreferredSize(new Dimension(560, 650));

        // Top section with avatar + name
        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBorder(BorderFactory.createEmptyBorder(12, 40, 16, 40));

        String initial = studentName.isEmpty() ? "?" : studentName.substring(0, 1).toUpperCase();
        JLabel bigAvatar = new JLabel(initial) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int size = 64;
                g2.setPaint(new GradientPaint(0, 0, UITheme.ACCENT, size, size, UITheme.ACCENT_PURPLE));
                g2.fillOval(0, 0, size, size);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initial, (size - fm.stringWidth(initial)) / 2,
                        (size + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        bigAvatar.setPreferredSize(new Dimension(64, 64));
        bigAvatar.setMinimumSize(new Dimension(64, 64));
        bigAvatar.setMaximumSize(new Dimension(64, 64));
        bigAvatar.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(bigAvatar);
        topSection.add(Box.createVerticalStrut(10));

        JLabel bigName = new JLabel(studentName);
        bigName.setFont(UITheme.FONT_HEADING);
        bigName.setForeground(UITheme.TEXT_PRIMARY);
        bigName.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(bigName);

        JLabel rollDisplay = new JLabel(rollNum);
        rollDisplay.setFont(UITheme.FONT_SMALL);
        rollDisplay.setForeground(UITheme.TEXT_TERTIARY);
        rollDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(rollDisplay);

        card.add(topSection, BorderLayout.NORTH);

        // Form fields
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 16);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField fName = UITheme.createStyledTextField("Full name");
        JTextField fAge = UITheme.createStyledTextField("Age");
        JTextField fMajor = UITheme.createStyledTextField("Major");
        JTextField fGPA = UITheme.createStyledTextField("GPA");
        JTextField fEmail = UITheme.createStyledTextField("Email");
        JPasswordField fPass = UITheme.createStyledPasswordField("Password");
        JTextField fResume = UITheme.createStyledTextField("Resume path");        JButton chooseResumeBtn = UITheme.createSecondaryButton("Choose File");
        JLabel resumeLabel = new JLabel("No file selected");
        resumeLabel.setForeground(UITheme.TEXT_SECONDARY);
        resumeLabel.setFont(UITheme.FONT_SMALL);
        // Load current data
        try {
            ResultSet rs = StudentDB.getStudentByRoll(rollNum);
            if (rs != null && rs.next()) {
                fName.setText(rs.getString("name"));
                fAge.setText(String.valueOf(rs.getInt("age")));
                fMajor.setText(rs.getString("major"));
                fGPA.setText(String.valueOf(rs.getDouble("gpa")));
                fPass.setText(rs.getString("password"));
                fResume.setText(rs.getString("resume_path") != null ? rs.getString("resume_path") : "");
                resumeLabel.setText(rs.getString("resume_path") != null ? 
                    rs.getString("resume_path").substring(rs.getString("resume_path").lastIndexOf("\\") + 1) : "No file selected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        chooseResumeBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF and DOC files", "pdf", "doc", "docx"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                try {
                    // Copy file to resumes directory
                    java.nio.file.Path source = selectedFile.toPath();
                    String fileName = rollNum + "_" + selectedFile.getName();
                    java.nio.file.Path target = java.nio.file.Paths.get("resumes", fileName);
                    java.nio.file.Files.copy(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    fResume.setText("resumes/" + fileName);
                    resumeLabel.setText(selectedFile.getName());
                } catch (java.io.IOException ex) {
                    msg("Error uploading file: " + ex.getMessage());
                }
            }
        });

        String[] labels = {"NAME", "AGE", "MAJOR", "GPA", "EMAIL", "PASSWORD", "RESUME"};
        Object[] fields = {fName, fAge, fMajor, fGPA, fEmail, fPass, fResume};
        JButton[] buttons = {null, null, null, null, null, null, chooseResumeBtn};
        JLabel[] extraLabels = {null, null, null, null, null, null, resumeLabel};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("SansSerif", Font.BOLD, 10));
            label.setForeground(UITheme.TEXT_SECONDARY);
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            if (fields[i] instanceof JTextField || fields[i] instanceof JPasswordField) {
                ((JComponent) fields[i]).setPreferredSize(new Dimension(250, 38));
                form.add((JComponent) fields[i], gbc);
            }

            if (buttons[i] != null) {
                gbc.gridx = 2;
                gbc.fill = GridBagConstraints.NONE;
                gbc.weightx = 0;
                gbc.insets = new Insets(0, 10, 6, 0);
                form.add(buttons[i], gbc);
            }

            if (extraLabels[i] != null) {
                gbc.gridx = 1;
                gbc.gridy = i + 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                gbc.insets = new Insets(-6, 0, 6, 0);
                form.add(extraLabels[i], gbc);
            }
        }

        // Save button
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(18, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        JButton saveBtn = UITheme.createPrimaryButton("Save Changes");
        saveBtn.addActionListener(e -> {
            try {
                StudentDB.updateStudent(rollNum, fName.getText().trim(),
                        Integer.parseInt(fAge.getText().trim()), fMajor.getText().trim(),
                        Double.parseDouble(fGPA.getText().trim()), fEmail.getText().trim(),
                        new String(fPass.getPassword()).trim(), fResume.getText().trim());
                msg("Profile updated!");
            } catch (NumberFormatException ex) {
                msg("Age must be integer, GPA must be a number.");
            }
        });
        form.add(saveBtn, gbc);

        card.add(form, BorderLayout.CENTER);
        outerWrapper.add(card);
        
        JScrollPane scrollPane = new JScrollPane(outerWrapper);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.BG_PRIMARY);
        scrollPane.setBackground(UITheme.BG_PRIMARY);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Available Drives Section ────────────────────
    private void showDrives() {
        contentPanel.removeAll();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Drive ID", "Company", "Start", "End", "Seats", "LPA", "Min GPA"}, 0);
        JTable table = UITheme.createStyledTable(model);

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        try {
            ResultSet rs = DriveDB.getAllDrives();
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("D_id"), rs.getString("cname"),
                        rs.getString("start_date"), rs.getString("end_date"),
                        rs.getInt("availableSeats"), rs.getDouble("lpa"),
                        rs.getDouble("mingpa")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Stats
        double maxLpa = allData.stream().mapToDouble(r -> ((Number) r[5]).doubleValue()).max().orElse(0);
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(16, 0, 4, 0));
        stats.add(UITheme.createStatCard("🎯", "Open Drives", String.valueOf(allData.size()), UITheme.STAT_BLUE));
        stats.add(UITheme.createStatCard("💰", "Max LPA", allData.isEmpty() ? "—" : String.format("%.1f", maxLpa), UITheme.STAT_GREEN));
        stats.add(UITheme.createStatCard("💺", "Total Seats",
                String.valueOf(allData.stream().mapToInt(r -> ((Number) r[4]).intValue()).sum()),
                UITheme.STAT_ORANGE));

        // Search
        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search drives...", searchRef);
        searchRef[0].getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String q = searchRef[0].getText().trim().toLowerCase();
                model.setRowCount(0);
                for (Object[] row : allData) {
                    if (q.isEmpty()) { model.addRow(row); continue; }
                    for (Object cell : row) {
                        if (cell != null && cell.toString().toLowerCase().contains(q)) {
                            model.addRow(row);
                            break;
                        }
                    }
                }
            }
        });

        JPanel header = UITheme.createHeaderPanel("Available Drives");
        header.add(searchPanel, BorderLayout.EAST);

        contentPanel.add(header, BorderLayout.NORTH);

        JButton applyBtn = UITheme.createPrimaryButton("Apply to Drive");
        applyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a drive first."); return; }
            int driveId = (int) model.getValueAt(row, 0);
            double minGpa = (double) model.getValueAt(row, 6);

            try {
                ResultSet rs = StudentDB.getStudentByRoll(rollNum);
                if (rs != null && rs.next() && rs.getDouble("gpa") < minGpa) {
                    msg("Your GPA is below the minimum required (" + minGpa + ").");
                    return;
                }
            } catch (SQLException ex) {
                msg("Error: " + ex.getMessage());
                return;
            }

            if (ApplicationDB.hasApplied(rollNum, driveId)) {
                msg("You already applied to this drive.");
                return;
            }
            if (ApplicationDB.addApplication(driveId, rollNum)) {
                msg("Applied successfully!");
            } else {
                msg("Failed to apply.");
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
        centerPanel.add(stats, BorderLayout.NORTH);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        tableWrapper.add(UITheme.createStyledScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(tableWrapper, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(UITheme.createActionBar(applyBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── My Applications Section ────────────────────
    private void showApplications() {
        contentPanel.removeAll();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"App ID", "Drive", "Company", "Date", "Status"}, 0);
        JTable table = UITheme.createStyledTable(model);

        // Status badge renderer
        table.getColumnModel().getColumn(4).setCellRenderer(UITheme.createStatusBadgeRenderer());

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        try {
            ResultSet rs = ApplicationDB.getApplicationsByStudent(rollNum);
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("A_id"), rs.getInt("driveId"),
                        rs.getString("cname"), rs.getString("applicationDate"),
                        rs.getString("status")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Stats
        long applied = allData.stream().filter(r -> "Applied".equals(r[4])).count();
        long accepted = allData.stream().filter(r -> "Accepted".equals(r[4])).count();
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(16, 0, 4, 0));
        stats.add(UITheme.createStatCard("📝", "Total Apps", String.valueOf(allData.size()), UITheme.STAT_BLUE));
        stats.add(UITheme.createStatCard("⏳", "Pending", String.valueOf(applied), UITheme.STAT_ORANGE));
        stats.add(UITheme.createStatCard("✅", "Accepted", String.valueOf(accepted), UITheme.STAT_GREEN));

        JPanel header = UITheme.createHeaderPanel("My Applications");
        contentPanel.add(header, BorderLayout.NORTH);

        JButton withdrawBtn = UITheme.createDangerButton("Withdraw");
        withdrawBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select an application first."); return; }
            if (!"Applied".equals(model.getValueAt(row, 4))) {
                msg("Can only withdraw 'Applied' applications.");
                return;
            }
            ApplicationDB.deleteApplication((int) model.getValueAt(row, 0));
            msg("Application withdrawn.");
            showApplications();
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
        centerPanel.add(stats, BorderLayout.NORTH);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        tableWrapper.add(UITheme.createStyledScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(tableWrapper, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(UITheme.createActionBar(withdrawBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Notifications Section ────────────────────
    private void showNotifications() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Message", "Date", "Read"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = UITheme.createStyledTable(model);

        try {
            ResultSet rs = NotificationDB.getNotifications("student", rollNum);
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("message"),
                        rs.getString("sent_date"), rs.getBoolean("is_read") ? "Yes" : "No"};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JPanel header = UITheme.createHeaderPanel("Notifications");
        JButton markReadBtn = UITheme.createSecondaryButton("Mark as Read");
        JButton markUnreadBtn = UITheme.createSecondaryButton("Mark as Unread");

        // Double-click to view full message
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    String message = (String) model.getValueAt(row, 1);
                    
                    JTextArea textArea = new JTextArea(message);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setFont(UITheme.FONT_BODY);
                    textArea.setBackground(UITheme.BG_SECONDARY);
                    textArea.setForeground(UITheme.TEXT_PRIMARY);
                    textArea.setEditable(false);
                    textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 300));
                    scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.DIVIDER));
                    
                    Object[] fields = {"Details:", scrollPane};
                    UITheme.showStyledDialog(StudentDashboard.this, "Notification Details", fields);
                    
                    // Automatically mark as read
                    int notifId = (int) model.getValueAt(row, 0);
                    if ("No".equals(model.getValueAt(row, 3))) {
                        NotificationDB.markAsRead(notifId);
                        model.setValueAt("Yes", row, 3);
                    }
                }
            }
        });

        markReadBtn.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            if (rows.length == 0) { msg("Select a notification first."); return; }
            for (int row : rows) {
                int notifId = (int) model.getValueAt(row, 0);
                if ("No".equals(model.getValueAt(row, 3))) {
                    if (NotificationDB.markAsRead(notifId)) {
                        model.setValueAt("Yes", row, 3);
                    }
                }
            }
        });

        markUnreadBtn.addActionListener(e -> {
            int[] rows = table.getSelectedRows();
            if (rows.length == 0) { msg("Select a notification first."); return; }
            for (int row : rows) {
                int notifId = (int) model.getValueAt(row, 0);
                if ("Yes".equals(model.getValueAt(row, 3))) {
                    if (NotificationDB.markAsUnread(notifId)) {
                        model.setValueAt("No", row, 3);
                    }
                }
            }
        });

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(UITheme.createStyledScrollPane(table), BorderLayout.CENTER);
        contentPanel.add(UITheme.createActionBar(markReadBtn, markUnreadBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Interviews Section ────────────────────
    private void showInterviews() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Company", "Date & Time", "Type", "Status", "Notes"}, 0);
        JTable table = UITheme.createStyledTable(model);

        try {
            ResultSet rs = InterviewDB.getInterviewsByStudent(rollNum);
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("cname"),
                        rs.getString("interview_date"), rs.getString("interview_type"),
                        rs.getString("status"), rs.getString("notes")};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JPanel header = UITheme.createHeaderPanel("My Interviews");
        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(UITheme.createStyledScrollPane(table), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void msg(String m) {
        UITheme.showMessage(this, m);
    }
}
