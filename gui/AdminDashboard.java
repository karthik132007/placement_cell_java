package gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.sql.*;
import DB_connections.*;

public class AdminDashboard extends JFrame {

    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private String activeSection = "Students";

    // Nav icons
    private static final String[] NAV_ICONS = {"👥", "🏢", "📋", "📄", "🎓", "🔔", "📅"};
    private static final String[] NAV_LABELS = {"Students", "Companies", "Drives", "Applications", "Placements", "Notifications", "Interviews"};

    public AdminDashboard() {
        UITheme.applyGlobalTheme();

        setTitle("Admin Dashboard — Placement Cell");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_PRIMARY);

        setLayout(new BorderLayout());

        // ──── Sidebar ────
        sidebarPanel = UITheme.createSidebarPanel();

        // Brand header in sidebar
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(28, 22, 20, 22));

        JLabel brandIcon = new JLabel("⚙️");
        brandIcon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        brandIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandIcon);
        brandPanel.add(Box.createVerticalStrut(8));

        JLabel brandLabel = new JLabel("Admin Panel");
        brandLabel.setFont(UITheme.FONT_HEADING);
        brandLabel.setForeground(UITheme.TEXT_PRIMARY);
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandLabel);

        JLabel brandSub = new JLabel("Management Console");
        brandSub.setFont(UITheme.FONT_TINY);
        brandSub.setForeground(UITheme.TEXT_TERTIARY);
        brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandSub);

        sidebarPanel.add(brandPanel);
        sidebarPanel.add(Box.createVerticalStrut(8));

        // Divider line
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

        // Nav buttons
        rebuildSidebar();

        sidebarPanel.add(Box.createVerticalGlue());

        // Logout at bottom
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

        showStudents();
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
            case "Students":     showStudents(); break;
            case "Companies":    showCompanies(); break;
            case "Drives":       showDrives(); break;
            case "Applications": showApplications(); break;
            case "Placements":   showPlacements(); break;
            case "Notifications": showNotifications(); break;
            case "Interviews":   showInterviews(); break;
        }
    }

    // helper to swap content
    private void setContent(String title, JPanel statsRow, JScrollPane tableScroll, JPanel buttons) {
        contentPanel.removeAll();

        JPanel header = UITheme.createHeaderPanel(title);
        contentPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        if (statsRow != null) {
            centerPanel.add(statsRow, BorderLayout.NORTH);
        }

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        tableWrapper.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(tableWrapper, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(buttons, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /** Creates a stats row with cards. */
    private JPanel createStatsRow(JPanel... cards) {
        JPanel row = new JPanel(new GridLayout(1, cards.length, 16, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(16, 0, 4, 0));
        for (JPanel c : cards) row.add(c);
        return row;
    }

    /** Attach a real-time search filter to a table. */
    private void attachSearch(JTextField searchField, JTable table, DefaultTableModel model,
                              java.util.List<Object[]> allData) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String q = searchField.getText().trim().toLowerCase();
                model.setRowCount(0);
                for (Object[] row : allData) {
                    if (q.isEmpty()) {
                        model.addRow(row);
                    } else {
                        for (Object cell : row) {
                            if (cell != null && cell.toString().toLowerCase().contains(q)) {
                                model.addRow(row);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    // ──────────────────── Students Section ────────────────────
    private void showStudents() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Roll No", "Name", "Age", "Major", "GPA", "Email"}, 0);
        JTable table = UITheme.createStyledTable(model);

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        try {
            ResultSet rs = StudentDB.getAllStudents();
            while (rs != null && rs.next()) {
                Object[] row = {rs.getString("rollnum"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("major"),
                        rs.getDouble("gpa"), rs.getString("email")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Stats
        JPanel stats = createStatsRow(
            UITheme.createStatCard("👥", "Total Students", String.valueOf(allData.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("🎓", "Avg GPA",
                    allData.isEmpty() ? "—" : String.format("%.1f", allData.stream()
                        .mapToDouble(r -> ((Number) r[4]).doubleValue()).average().orElse(0)),
                    UITheme.STAT_PURPLE),
            UITheme.createStatCard("📚", "Departments",
                    String.valueOf(allData.stream().map(r -> r[3]).distinct().count()),
                    UITheme.STAT_GREEN)
        );

        // Search
        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search students...", searchRef);
        attachSearch(searchRef[0], table, model, allData);

        JPanel header = UITheme.createHeaderPanel("Students");
        header.add(searchPanel, BorderLayout.EAST);

        JButton deleteBtn = UITheme.createDangerButton("Delete");
        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a student first."); return; }
            String roll = (String) model.getValueAt(row, 0);
            StudentDB.deleteStudent(roll);
            showStudents();
        });
        refreshBtn.addActionListener(e -> showStudents());

        // Custom layout with search in header
        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

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
        contentPanel.add(UITheme.createActionBar(deleteBtn, refreshBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Companies Section ────────────────────
    private void showCompanies() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "Location", "Industry", "HR Contact"}, 0);
        JTable table = UITheme.createStyledTable(model);

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        try {
            ResultSet rs = CompanyDB.getAllCompanies();
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("name"),
                        rs.getString("location"), rs.getString("industry"),
                        rs.getString("hrcontact")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Stats
        JPanel stats = createStatsRow(
            UITheme.createStatCard("🏢", "Total Companies", String.valueOf(allData.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("🌍", "Locations",
                    String.valueOf(allData.stream().map(r -> r[2]).distinct().count()),
                    UITheme.STAT_PURPLE),
            UITheme.createStatCard("🏭", "Industries",
                    String.valueOf(allData.stream().map(r -> r[3]).distinct().count()),
                    UITheme.STAT_ORANGE)
        );

        // Search
        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search companies...", searchRef);
        attachSearch(searchRef[0], table, model, allData);

        JPanel header = UITheme.createHeaderPanel("Companies");
        header.add(searchPanel, BorderLayout.EAST);

        JButton addBtn = UITheme.createPrimaryButton("Add");
        JButton editBtn = UITheme.createSecondaryButton("Edit");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        addBtn.addActionListener(e -> {
            JTextField fN = UITheme.createStyledTextField("Company name");
            JTextField fL = UITheme.createStyledTextField("Location");
            JTextField fI = UITheme.createStyledTextField("Industry");
            JTextField fH = UITheme.createStyledTextField("HR contact");
            Object[] fields = {"Name:", fN, "Location:", fL, "Industry:", fI, "HR Contact:", fH};
            if (UITheme.showStyledDialog(this, "Add Company", fields)) {
                CompanyDB.addCompany(fN.getText(), fL.getText(), fI.getText(), fH.getText());
                showCompanies();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a company first."); return; }
            int id = (int) model.getValueAt(row, 0);
            JTextField fN = UITheme.createStyledTextField(""); fN.setText((String) model.getValueAt(row, 1));
            JTextField fL = UITheme.createStyledTextField(""); fL.setText((String) model.getValueAt(row, 2));
            JTextField fI = UITheme.createStyledTextField(""); fI.setText((String) model.getValueAt(row, 3));
            JTextField fH = UITheme.createStyledTextField(""); fH.setText((String) model.getValueAt(row, 4));
            Object[] fields = {"Name:", fN, "Location:", fL, "Industry:", fI, "HR Contact:", fH};
            if (UITheme.showStyledDialog(this, "Edit Company", fields)) {
                CompanyDB.updateCompany(id, fN.getText(), fL.getText(), fI.getText(), fH.getText());
                showCompanies();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a company first."); return; }
            CompanyDB.deleteCompany((int) model.getValueAt(row, 0));
            showCompanies();
        });

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

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
        contentPanel.add(UITheme.createActionBar(addBtn, editBtn, deleteBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Drives Section ────────────────────
    private void showDrives() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Company", "Start", "End", "Seats", "LPA", "Min GPA"}, 0);
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
        double avgLpa = allData.stream().mapToDouble(r -> ((Number) r[5]).doubleValue()).average().orElse(0);
        int totalSeats = allData.stream().mapToInt(r -> ((Number) r[4]).intValue()).sum();
        JPanel stats = createStatsRow(
            UITheme.createStatCard("📋", "Active Drives", String.valueOf(allData.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("💰", "Avg LPA", allData.isEmpty() ? "—" : String.format("%.1f", avgLpa), UITheme.STAT_GREEN),
            UITheme.createStatCard("💺", "Total Seats", String.valueOf(totalSeats), UITheme.STAT_ORANGE)
        );

        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search drives...", searchRef);
        attachSearch(searchRef[0], table, model, allData);

        JPanel header = UITheme.createHeaderPanel("Drives");
        header.add(searchPanel, BorderLayout.EAST);

        JButton addBtn = UITheme.createPrimaryButton("Create Drive");
        JButton editBtn = UITheme.createSecondaryButton("Edit Drive");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        addBtn.addActionListener(e -> {
            JComboBox<String> cb = UITheme.createStyledComboBox(new String[]{});
            try {
                ResultSet rs = CompanyDB.getCompanyIdNames();
                while (rs != null && rs.next())
                    cb.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            JSpinner fS = UITheme.createStyledDateSpinner(new java.util.Date());
            JSpinner fE = UITheme.createStyledDateSpinner(new java.util.Date());
            JTextField fSeats = UITheme.createStyledTextField("e.g. 50");
            JTextField fLPA = UITheme.createStyledTextField("e.g. 12.5");
            JTextField fGPA = UITheme.createStyledTextField("e.g. 7.0");
            Object[] fields = {"Company:", cb, "Start:", fS, "End:", fE,
                    "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA};
            if (UITheme.showStyledDialog(this, "Create Drive", fields)) {
                try {
                    int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                    String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fS.getValue());
                    String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fE.getValue());
                    DriveDB.addDrive(compId, startDate, endDate,
                            Integer.parseInt(fSeats.getText()),
                            Double.parseDouble(fLPA.getText()),
                            Double.parseDouble(fGPA.getText()));
                    showDrives();
                } catch (Exception ex) {
                    msg("Invalid input.");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a drive first."); return; }
            int driveId = (int) model.getValueAt(row, 0);
            try {
                ResultSet rs = DriveDB.getDriveById(driveId);
                if (rs != null && rs.next()) {
                    JComboBox<String> cb = UITheme.createStyledComboBox(new String[]{});
                    try {
                        ResultSet crs = CompanyDB.getCompanyIdNames();
                        while (crs != null && crs.next()) {
                            String item = crs.getInt("id") + " - " + crs.getString("name");
                            cb.addItem(item);
                            if (crs.getInt("id") == rs.getInt("companyId")) cb.setSelectedItem(item);
                        }
                    } catch (SQLException ex) { ex.printStackTrace(); }

                    JSpinner fS = UITheme.createStyledDateSpinner(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("start_date")));
                    JSpinner fE = UITheme.createStyledDateSpinner(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("end_date")));
                    JTextField fSeats = UITheme.createStyledTextField(String.valueOf(rs.getInt("availableSeats")));
                    JTextField fLPA = UITheme.createStyledTextField(String.valueOf(rs.getDouble("lpa")));
                    JTextField fGPA = UITheme.createStyledTextField(String.valueOf(rs.getDouble("mingpa")));
                    Object[] fields = {"Company:", cb, "Start:", fS, "End:", fE,
                            "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA};
                    if (UITheme.showStyledDialog(this, "Edit Drive", fields)) {
                        int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                        String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fS.getValue());
                        String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fE.getValue());
                        DriveDB.updateDrive(driveId, compId, startDate, endDate,
                                Integer.parseInt(fSeats.getText()),
                                Double.parseDouble(fLPA.getText()),
                                Double.parseDouble(fGPA.getText()));
                        showDrives();
                    }
                }
            } catch (SQLException | java.text.ParseException ex) { ex.printStackTrace(); }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a drive first."); return; }
            DriveDB.deleteDrive((int) model.getValueAt(row, 0));
            showDrives();
        });

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

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
        contentPanel.add(UITheme.createActionBar(addBtn, editBtn, deleteBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Applications Section ────────────────────
    private void showApplications() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"App ID", "Drive", "Company", "Student", "Name", "Date", "Status"}, 0);
        JTable table = UITheme.createStyledTable(model);

        // Status column badge renderer
        table.getColumnModel().getColumn(6).setCellRenderer(UITheme.createStatusBadgeRenderer());

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        loadAppData(model, null, allData);

        // Stats
        long applied = allData.stream().filter(r -> "Applied".equals(r[6])).count();
        long accepted = allData.stream().filter(r -> "Accepted".equals(r[6])).count();
        JPanel stats = createStatsRow(
            UITheme.createStatCard("📄", "Total Apps", String.valueOf(allData.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("⏳", "Pending", String.valueOf(applied), UITheme.STAT_ORANGE),
            UITheme.createStatCard("✅", "Accepted", String.valueOf(accepted), UITheme.STAT_GREEN)
        );

        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search applications...", searchRef);
        attachSearch(searchRef[0], table, model, allData);

        JPanel header = UITheme.createHeaderPanel("Applications");
        header.add(searchPanel, BorderLayout.EAST);

        JComboBox<String> filterBox = UITheme.createStyledComboBox(
                new String[]{"All", "Applied", "Shortlisted", "Accepted", "Rejected"});
        JButton filterBtn = UITheme.createSecondaryButton("Filter");
        JButton statusBtn = UITheme.createPrimaryButton("Update Status");
        JButton scheduleInterviewBtn = UITheme.createPrimaryButton("Schedule Interview");

        filterBtn.addActionListener(e -> {
            String f = (String) filterBox.getSelectedItem();
            model.setRowCount(0);
            allData.clear();
            loadAppData(model, "All".equals(f) ? null : f, allData);
        });

        statusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select an application first."); return; }
            int appId = (int) model.getValueAt(row, 0);
            JComboBox<String> combo = UITheme.createStyledComboBox(
                    new String[]{"Applied", "Shortlisted", "Accepted", "Rejected"});
            combo.setSelectedItem(model.getValueAt(row, 6));
            Object[] fields = {"New Status:", combo};
            if (UITheme.showStyledDialog(this, "Update Application Status", fields)) {
                ApplicationDB.updateStatus(appId, (String) combo.getSelectedItem());
                showApplications();
            }
        });

        scheduleInterviewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a shortlisted application first."); return; }

            String status = (String) model.getValueAt(row, 6);
            if (!"Shortlisted".equals(status)) {
                msg("Only shortlisted applications can be scheduled for interviews.");
                return;
            }

            int appId = (int) model.getValueAt(row, 0);
            String studentName = (String) model.getValueAt(row, 4);
            String companyName = (String) model.getValueAt(row, 2);

            com.toedter.calendar.JDateChooser dateSpinner = UITheme.createStyledDateChooser(new java.util.Date());
            JSpinner timeSpinner = UITheme.createStyledTimeSpinner(new java.util.Date());
            JTextField fType = UITheme.createStyledTextField("e.g. Technical Round 1");
            JTextField fNotes = UITheme.createStyledTextField("Additional notes (optional)");

            Object[] fields = {"Student:", new JLabel(studentName), "Company:", new JLabel(companyName),
                    "Date:", dateSpinner, "Time:", timeSpinner,
                    "Interview Type:", fType, "Notes:", fNotes};
            if (UITheme.showStyledDialog(this, "Schedule Interview", fields)) {
                try {
                    java.util.Date dateValue = dateSpinner.getDate();
                    java.util.Date timeValue = (java.util.Date) timeSpinner.getValue();
                    java.time.LocalDate localDate = dateValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    java.time.LocalTime localTime = timeValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
                    String interviewDate = java.time.LocalDateTime.of(localDate, localTime)
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    boolean success = InterviewDB.scheduleInterview(appId, interviewDate, fType.getText(), fNotes.getText());
                    if (success) {
                        msg("Interview scheduled successfully! Student has been notified.");
                        showApplications();
                    } else {
                        msg("Failed to schedule. Please check the date/time and try again.");
                    }
                } catch (Exception ex) {
                    msg("Invalid date/time value. Please select date and time from pickers.");
                }
            }
        });

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionBar.setOpaque(false);
        actionBar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        actionBar.add(filterBox);
        actionBar.add(filterBtn);
        actionBar.add(statusBtn);
        actionBar.add(scheduleInterviewBtn);

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

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
        contentPanel.add(actionBar, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadAppData(DefaultTableModel model, String statusFilter, java.util.List<Object[]> allData) {
        try {
            ResultSet rs = ApplicationDB.getAllApplications(statusFilter);
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("A_id"), rs.getInt("driveId"),
                        rs.getString("cname"), rs.getString("s_id"), rs.getString("sname"),
                        rs.getString("applicationDate"), rs.getString("status")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ──────────────────── Placements Section ────────────────────
    private void showPlacements() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Student", "Company", "Salary", "Placement Date"}, 0);
        JTable table = UITheme.createStyledTable(model);

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        try {
            ResultSet rs = PlacementDB.getAllPlacements();
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("sname") + " (" + rs.getString("student_roll") + ")",
                        rs.getString("cname"), rs.getDouble("salary"), rs.getString("placement_date")};
                allData.add(row);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Stats
        JPanel stats = createStatsRow(
            UITheme.createStatCard("🎓", "Total Placements", String.valueOf(allData.size()), UITheme.STAT_GREEN),
            UITheme.createStatCard("💰", "Avg Salary",
                    allData.isEmpty() ? "—" : String.format("₹%.1fL", allData.stream()
                        .mapToDouble(r -> (Double) r[3]).average().orElse(0) / 100000),
                    UITheme.STAT_BLUE),
            UITheme.createStatCard("🏢", "Companies",
                    String.valueOf(allData.stream().map(r -> r[2]).distinct().count()),
                    UITheme.STAT_PURPLE)
        );

        JPanel header = UITheme.createHeaderPanel("Placements");
        JButton addBtn = UITheme.createPrimaryButton("Add Placement");
        JButton editBtn = UITheme.createSecondaryButton("Edit");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        addBtn.addActionListener(e -> {
            // Get companies and students for dropdown
            java.util.List<String[]> companies = new java.util.ArrayList<>();
            java.util.List<String[]> students = new java.util.ArrayList<>();
            try {
                ResultSet rs = CompanyDB.getCompanyIdNames();
                while (rs != null && rs.next()) companies.add(new String[]{String.valueOf(rs.getInt(1)), rs.getString(2)});
                rs = StudentDB.getAllStudents();
                while (rs != null && rs.next()) students.add(new String[]{rs.getString("rollnum"), rs.getString("name")});
            } catch (SQLException ex) { ex.printStackTrace(); }

            JComboBox<String> studentBox = new JComboBox<>();
            JComboBox<String> companyBox = new JComboBox<>();
            JTextField fSalary = UITheme.createStyledTextField("e.g. 500000");
            JTextField fDate = UITheme.createStyledTextField("YYYY-MM-DD");

            for (String[] s : students) studentBox.addItem(s[1] + " (" + s[0] + ")");
            for (String[] c : companies) companyBox.addItem(c[1] + " (ID: " + c[0] + ")");

            Object[] fields = {"Student:", studentBox, "Company:", companyBox, "Salary:", fSalary, "Placement Date:", fDate};
            if (UITheme.showStyledDialog(this, "Add Placement", fields)) {
                String selectedStudent = students.get(studentBox.getSelectedIndex())[0];
                String selectedCompany = companies.get(companyBox.getSelectedIndex())[0];
                PlacementDB.addPlacement(selectedStudent, Integer.parseInt(selectedCompany), 0, Double.parseDouble(fSalary.getText()), fDate.getText());
                showPlacements();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a placement first."); return; }
            // Similar to add, but pre-fill
            msg("Edit functionality to be implemented.");
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a placement first."); return; }
            PlacementDB.deletePlacement((int) model.getValueAt(row, 0));
            showPlacements();
        });

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(stats, BorderLayout.CENTER);
        contentPanel.add(UITheme.createActionBar(addBtn, editBtn, deleteBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Notifications Section ────────────────────
    private void showNotifications() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Recipient", "Message", "Date", "Read"}, 0);
        JTable table = UITheme.createStyledTable(model);

        try {
            ResultSet rs = NotificationDB.getNotifications("admin", null);
            while (rs != null && rs.next()) {
                Object[] row = {rs.getInt("id"), rs.getString("recipient_type") + ": " + rs.getString("recipient_id"),
                        rs.getString("message"), rs.getString("sent_date"), rs.getBoolean("is_read") ? "Yes" : "No"};
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JPanel header = UITheme.createHeaderPanel("Notifications");
        JButton sendBtn = UITheme.createPrimaryButton("Send Notification");
        JButton markReadBtn = UITheme.createSecondaryButton("Mark as Read");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        sendBtn.addActionListener(e -> {
            JTextField fRecipient = UITheme.createStyledTextField("student rollnum or 'all'");
            JTextField fMessage = UITheme.createStyledTextField("Message");
            Object[] fields = {"Recipient:", fRecipient, "Message:", fMessage};
            if (UITheme.showStyledDialog(this, "Send Notification", fields)) {
                String recipient = fRecipient.getText().trim();
                if ("all".equals(recipient)) {
                    // Send to all students
                    try {
                        ResultSet rs = StudentDB.getAllStudents();
                        while (rs != null && rs.next()) {
                            NotificationDB.sendNotification("student", rs.getString("rollnum"), fMessage.getText());
                        }
                    } catch (SQLException ex) { ex.printStackTrace(); }
                } else {
                    NotificationDB.sendNotification("student", recipient, fMessage.getText());
                }
                showNotifications();
            }
        });

        markReadBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a notification first."); return; }
            NotificationDB.markAsRead((int) model.getValueAt(row, 0));
            showNotifications();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a notification first."); return; }
            NotificationDB.deleteNotification((int) model.getValueAt(row, 0));
            showNotifications();
        });

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);
        contentPanel.add(UITheme.createStyledScrollPane(table), BorderLayout.CENTER);
        contentPanel.add(UITheme.createActionBar(sendBtn, markReadBtn, deleteBtn), BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ──────────────────── Interviews Section ────────────────────
    private void showInterviews() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Student", "Company", "Date & Time", "Type", "Status"}, 0);
        JTable table = UITheme.createStyledTable(model);

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        loadInterviewData(model, null, allData);

        // Stats
        long scheduled = allData.stream().filter(r -> "Scheduled".equals(r[5])).count();
        long completed = allData.stream().filter(r -> "Completed".equals(r[5])).count();
        long accepted = allData.stream().filter(r -> "Accepted".equals(r[5])).count();
        JPanel stats = createStatsRow(
            UITheme.createStatCard("📅", "Total", String.valueOf(allData.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("⏳", "Scheduled", String.valueOf(scheduled), UITheme.STAT_ORANGE),
            UITheme.createStatCard("✅", "Accepted", String.valueOf(accepted), UITheme.STAT_GREEN)
        );

        JTextField[] searchRef = new JTextField[1];
        JPanel searchPanel = UITheme.createSearchPanel("Search interviews...", searchRef);
        attachSearch(searchRef[0], table, model, allData);

        JPanel header = UITheme.createHeaderPanel("Interviews");
        header.add(searchPanel, BorderLayout.EAST);

        JComboBox<String> filterBox = UITheme.createStyledComboBox(
                new String[]{"All", "Scheduled", "Completed", "Cancelled", "Accepted"});
        filterBox.setSelectedIndex(0); // Always start with "All"
        JButton filterBtn = UITheme.createSecondaryButton("Filter");
        JButton scheduleBtn = UITheme.createPrimaryButton("Schedule Interview");
        JButton updateBtn = UITheme.createSecondaryButton("Update Status");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        filterBtn.addActionListener(e -> {
            String f = (String) filterBox.getSelectedItem();
            model.setRowCount(0);
            allData.clear();
            loadInterviewData(model, "All".equals(f) ? null : f, allData);
        });

        scheduleBtn.addActionListener(e -> {
            // Get applications for scheduling
            java.util.List<String[]> applications = new java.util.ArrayList<>();
            try {
                ResultSet rs = ApplicationDB.getAllApplications("Shortlisted");
                while (rs != null && rs.next()) {
                    applications.add(new String[]{String.valueOf(rs.getInt("A_id")),
                            rs.getString("sname") + " - " + rs.getString("cname")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }

            if (applications.isEmpty()) {
                msg("No shortlisted applications available.");
                return;
            }

            JComboBox<String> appBox = new JComboBox<>();
            for (String[] app : applications) appBox.addItem(app[1]);

            com.toedter.calendar.JDateChooser dateSpinner = UITheme.createStyledDateChooser(new java.util.Date());
            JSpinner timeSpinner = UITheme.createStyledTimeSpinner(new java.util.Date());
            JTextField fType = UITheme.createStyledTextField("e.g. Technical Round 1");
            JTextField fNotes = UITheme.createStyledTextField("Additional notes");

            Object[] fields = {"Application:", appBox, "Date:", dateSpinner, "Time:", timeSpinner, "Type:", fType, "Notes:", fNotes};
            if (UITheme.showStyledDialog(this, "Schedule Interview", fields)) {
                String selectedApp = applications.get(appBox.getSelectedIndex())[0];
                try {
                    java.util.Date dateValue = dateSpinner.getDate();
                    java.util.Date timeValue = (java.util.Date) timeSpinner.getValue();
                    java.time.LocalDate localDate = dateValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    java.time.LocalTime localTime = timeValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
                    String interviewDate = java.time.LocalDateTime.of(localDate, localTime)
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    boolean success = InterviewDB.scheduleInterview(Integer.parseInt(selectedApp), interviewDate, fType.getText(), fNotes.getText());
                    if (success) {
                        msg("Interview scheduled successfully! Student has been notified.");
                        showInterviews();
                    } else {
                        msg("Failed to schedule. Please check the selected date/time and try again.");
                    }
                } catch (Exception ex) {
                    msg("Invalid date/time value. Please select date and time from pickers.");
                }
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select an interview first."); return; }
            int interviewId = (int) model.getValueAt(row, 0);

            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled", "Accepted"});
            statusBox.setSelectedItem(model.getValueAt(row, 5));
            JTextField fNotes = UITheme.createStyledTextField("Update notes");

            Object[] fields = {"Status:", statusBox, "Notes:", fNotes};
            if (UITheme.showStyledDialog(this, "Update Interview", fields)) {
                String newStatus = (String) statusBox.getSelectedItem();
                InterviewDB.updateInterviewStatus(interviewId, newStatus, fNotes.getText());
                if ("Accepted".equals(newStatus)) {
                    msg("Interview marked as accepted! Placement record created and student notified.");
                }
                showInterviews();
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select an interview first."); return; }
            InterviewDB.deleteInterview((int) model.getValueAt(row, 0));
            showInterviews();
        });

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionBar.setOpaque(false);
        actionBar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        actionBar.add(filterBox);
        actionBar.add(filterBtn);
        actionBar.add(scheduleBtn);
        actionBar.add(updateBtn);
        actionBar.add(deleteBtn);

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

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
        contentPanel.add(actionBar, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadInterviewData(DefaultTableModel model, String statusFilter, java.util.List<Object[]> allData) {
        try {
            ResultSet rs = InterviewDB.getAllInterviews();
            while (rs != null && rs.next()) {
                String status = rs.getString("status");
                if (status != null) status = status.trim(); // Handle whitespace
                if (statusFilter == null || (status != null && statusFilter.equals(status))) {
                    Object[] row = {rs.getInt("id"), rs.getString("sname") + " (" + rs.getString("s_id") + ")",
                            rs.getString("cname"), rs.getString("interview_date"),
                            rs.getString("interview_type"), status};
                    allData.add(row);
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void msg(String m) {
        UITheme.showMessage(this, m);
    }
}
