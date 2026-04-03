package gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
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

        JButton viewResumeBtn = UITheme.createSecondaryButton("View Resume");
        JButton deleteBtn = UITheme.createDangerButton("Delete");
        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");

        viewResumeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { msg("Select a student first."); return; }
            String roll = (String) model.getValueAt(row, 0);
            try {
                ResultSet rs = DB_connections.StudentDB.getStudentByRoll(roll);
                if (rs != null && rs.next()) {
                    String resumePath = rs.getString("resume_path");
                    if (resumePath != null && !resumePath.trim().isEmpty()) {
                        java.io.File file = new java.io.File(resumePath);
                        if (file.exists()) {
                            java.awt.Desktop.getDesktop().open(file);
                        } else {
                            msg("Resume file not found on server.");
                        }
                    } else {
                        msg("Student has not uploaded a resume.");
                    }
                }
            } catch (Exception ex) {
                msg("Error opening resume: " + ex.getMessage());
            }
        });

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
        contentPanel.add(UITheme.createActionBar(viewResumeBtn, deleteBtn, refreshBtn), BorderLayout.SOUTH);
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
            
            JCheckBox cbApt = new JCheckBox("Aptitude"); cbApt.setOpaque(false); cbApt.setForeground(UITheme.TEXT_PRIMARY);
            JCheckBox cbTr1 = new JCheckBox("TR1"); cbTr1.setOpaque(false); cbTr1.setForeground(UITheme.TEXT_PRIMARY); cbTr1.setSelected(true);
            JCheckBox cbTr2 = new JCheckBox("TR2"); cbTr2.setOpaque(false); cbTr2.setForeground(UITheme.TEXT_PRIMARY);
            JCheckBox cbHr  = new JCheckBox("HR");  cbHr.setOpaque(false); cbHr.setForeground(UITheme.TEXT_PRIMARY); cbHr.setSelected(true);
            JPanel cbPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
            cbPanel.setOpaque(false);
            cbPanel.add(cbApt); cbPanel.add(cbTr1); cbPanel.add(cbTr2); cbPanel.add(cbHr);

            Object[] fields = {"Company:", cb, "Start:", fS, "End:", fE,
                    "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA, "Rounds:", cbPanel};
            if (UITheme.showStyledDialog(this, "Create Drive", fields)) {
                try {
                    int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                    String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fS.getValue());
                    String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fE.getValue());
                    
                    java.util.List<String> rList = new java.util.ArrayList<>();
                    if (cbApt.isSelected()) rList.add("Aptitude");
                    if (cbTr1.isSelected()) rList.add("TR1");
                    if (cbTr2.isSelected()) rList.add("TR2");
                    if (cbHr.isSelected()) rList.add("HR");
                    String roundsStr = rList.isEmpty() ? "HR" : String.join(",", rList);

                    DriveDB.addDrive(compId, startDate, endDate,
                            Integer.parseInt(fSeats.getText()),
                            Double.parseDouble(fLPA.getText()),
                            Double.parseDouble(fGPA.getText()),
                            roundsStr);
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
                    String currentRounds = rs.getString("rounds_list");
                    if (currentRounds == null) currentRounds = "HR";
                    
                    JCheckBox cbApt = new JCheckBox("Aptitude"); cbApt.setOpaque(false); cbApt.setForeground(UITheme.TEXT_PRIMARY); cbApt.setSelected(currentRounds.contains("Aptitude"));
                    JCheckBox cbTr1 = new JCheckBox("TR1"); cbTr1.setOpaque(false); cbTr1.setForeground(UITheme.TEXT_PRIMARY); cbTr1.setSelected(currentRounds.contains("TR1"));
                    JCheckBox cbTr2 = new JCheckBox("TR2"); cbTr2.setOpaque(false); cbTr2.setForeground(UITheme.TEXT_PRIMARY); cbTr2.setSelected(currentRounds.contains("TR2"));
                    JCheckBox cbHr  = new JCheckBox("HR");  cbHr.setOpaque(false); cbHr.setForeground(UITheme.TEXT_PRIMARY); cbHr.setSelected(currentRounds.contains("HR"));
                    JPanel cbPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
                    cbPanel.setOpaque(false);
                    cbPanel.add(cbApt); cbPanel.add(cbTr1); cbPanel.add(cbTr2); cbPanel.add(cbHr);

                    Object[] fields = {"Company:", cb, "Start:", fS, "End:", fE,
                            "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA, "Rounds:", cbPanel};
                    if (UITheme.showStyledDialog(this, "Edit Drive", fields)) {
                        int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                        String startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fS.getValue());
                        String endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format((Date) fE.getValue());
                        
                        java.util.List<String> rList = new java.util.ArrayList<>();
                        if (cbApt.isSelected()) rList.add("Aptitude");
                        if (cbTr1.isSelected()) rList.add("TR1");
                        if (cbTr2.isSelected()) rList.add("TR2");
                        if (cbHr.isSelected()) rList.add("HR");
                        String roundsStr = rList.isEmpty() ? "HR" : String.join(",", rList);

                        DriveDB.updateDrive(driveId, compId, startDate, endDate,
                                Integer.parseInt(fSeats.getText()),
                                Double.parseDouble(fLPA.getText()),
                                Double.parseDouble(fGPA.getText()),
                                roundsStr);
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
        loadAppData(model, null, "All Companies", allData);

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
                
        java.util.List<String> companyNamesList = new java.util.ArrayList<>();
        companyNamesList.add("All Companies");
        try {
            ResultSet rs = DB_connections.CompanyDB.getAllCompanies();
            while (rs != null && rs.next()) {
                companyNamesList.add(rs.getString("name"));
            }
        } catch (SQLException ex) {}
        JComboBox<String> companyFilterBox = UITheme.createStyledComboBox(companyNamesList.toArray(new String[0]));

        JButton filterBtn = UITheme.createSecondaryButton("Filter");
        JButton statusBtn = UITheme.createPrimaryButton("Update Status");
        JButton scheduleInterviewBtn = UITheme.createPrimaryButton("Schedule Interview");

        filterBtn.addActionListener(e -> {
            String f = (String) filterBox.getSelectedItem();
            String cF = (String) companyFilterBox.getSelectedItem();
            model.setRowCount(0);
            allData.clear();
            loadAppData(model, "All".equals(f) ? null : f, cF, allData);
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
        actionBar.add(companyFilterBox);
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

    private void loadAppData(DefaultTableModel model, String statusFilter, String companyFilter, java.util.List<Object[]> allData) {
        try {
            ResultSet rs = ApplicationDB.getAllApplications(statusFilter);
            while (rs != null && rs.next()) {
                String cname = rs.getString("cname");
                if (companyFilter == null || companyFilter.equals("All Companies") || companyFilter.equals(cname)) {
                    Object[] row = {rs.getInt("A_id"), rs.getInt("driveId"),
                            cname, rs.getString("s_id"), rs.getString("sname"),
                            rs.getString("applicationDate"), rs.getString("status")};
                    allData.add(row);
                    model.addRow(row);
                }
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
            java.util.List<String[]> students = new java.util.ArrayList<>();
            try {
                ResultSet rs = DB_connections.StudentDB.getAllStudents();
                while (rs != null && rs.next()) {
                    students.add(new String[]{rs.getString("rollnum"), rs.getString("name")});
                }
            } catch (SQLException ex) { ex.printStackTrace(); }

            DefaultTableModel stModel = new DefaultTableModel(new Object[]{"Select", "Student", "RollNumber"}, 0) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 0 ? Boolean.class : String.class;
                }
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0;
                }
            };
            for (String[] s : students) {
                stModel.addRow(new Object[]{false, s[1] + " (" + s[0] + ")", s[0]});
            }

            JTable stTable = UITheme.createStyledTable(stModel);
            stTable.getColumnModel().getColumn(2).setMinWidth(0);
            stTable.getColumnModel().getColumn(2).setMaxWidth(0);
            stTable.getColumnModel().getColumn(2).setWidth(0);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stModel);
            stTable.setRowSorter(sorter);

            JTextField searchBox = UITheme.createStyledTextField("Search students...");
            searchBox.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent ev) { filter(); }
                public void removeUpdate(javax.swing.event.DocumentEvent ev) { filter(); }
                public void changedUpdate(javax.swing.event.DocumentEvent ev) { filter(); }
                private void filter() {
                    String query = searchBox.getText().trim();
                    if (query.isEmpty()) { sorter.setRowFilter(null); }
                    else { sorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + query, 1)); }
                }
            });

            JCheckBox selectAllCb = new JCheckBox("Select All Visible");
            selectAllCb.setOpaque(false);
            selectAllCb.setForeground(UITheme.TEXT_PRIMARY);
            selectAllCb.addActionListener(ev -> {
                boolean sel = selectAllCb.isSelected();
                for (int i = 0; i < stTable.getRowCount(); i++) {
                    int modelRow = stTable.convertRowIndexToModel(i);
                    stModel.setValueAt(sel, modelRow, 0);
                }
            });

            JPanel topPanel = new JPanel(new BorderLayout(10, 10));
            topPanel.setOpaque(false);
            topPanel.add(searchBox, BorderLayout.CENTER);
            topPanel.add(selectAllCb, BorderLayout.EAST);

            JScrollPane scrollPane = UITheme.createStyledScrollPane(stTable);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JPanel wrapper = new JPanel(new BorderLayout(0, 10));
            wrapper.setOpaque(false);
            wrapper.add(topPanel, BorderLayout.NORTH);
            wrapper.add(scrollPane, BorderLayout.CENTER);

            JTextField fMessage = UITheme.createStyledTextField("Message");
            Object[] fields = {"Recipients:", wrapper, "Message:", fMessage};
            
            if (UITheme.showStyledDialog(this, "Send Notification", fields)) {
                String msgText = fMessage.getText().trim();
                if (msgText.isEmpty()) { msg("Message cannot be empty."); return; }
                
                int sentCount = 0;
                for (int i = 0; i < stModel.getRowCount(); i++) {
                    if ((Boolean) stModel.getValueAt(i, 0)) {
                        String rollNum = (String) stModel.getValueAt(i, 2);
                        NotificationDB.sendNotification("student", rollNum, msgText);
                        sentCount++;
                    }
                }
                msg("Successfully sent notification to " + sentCount + " students.");
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
        DefaultTableModel modelActive = new DefaultTableModel(
                new String[]{"ID", "Student", "Company", "Date/Time", "Type", "Status"}, 0);
        JTable tableActive = UITheme.createStyledTable(modelActive);

        DefaultTableModel modelAccepted = new DefaultTableModel(
                new String[]{"ID", "Student", "Company", "Date/Time", "Type", "Status"}, 0);
        JTable tableAccepted = UITheme.createStyledTable(modelAccepted);

        java.util.List<Object[]> allDataActive = new java.util.ArrayList<>();
        java.util.List<Object[]> allDataAccepted = new java.util.ArrayList<>();
        loadInterviewData(modelActive, "!Accepted", "All Companies", allDataActive);
        loadInterviewData(modelAccepted, "Accepted", "All Companies", allDataAccepted);

        // Stats
        long scheduled = allDataActive.stream().filter(r -> "Scheduled".equals(r[5])).count();
        long accepted = allDataAccepted.size();
        JPanel stats = createStatsRow(
            UITheme.createStatCard("📅", "Active", String.valueOf(allDataActive.size()), UITheme.STAT_BLUE),
            UITheme.createStatCard("⏳", "Scheduled", String.valueOf(scheduled), UITheme.STAT_ORANGE),
            UITheme.createStatCard("✅", "Accepted", String.valueOf(accepted), UITheme.STAT_GREEN)
        );

        JTextField[] searchRefActive = new JTextField[1];
        JPanel searchPanelActive = UITheme.createSearchPanel("Search active...", searchRefActive);
        attachSearch(searchRefActive[0], tableActive, modelActive, allDataActive);

        JTextField[] searchRefAccepted = new JTextField[1];
        JPanel searchPanelAccepted = UITheme.createSearchPanel("Search accepted...", searchRefAccepted);
        attachSearch(searchRefAccepted[0], tableAccepted, modelAccepted, allDataAccepted);

        JPanel header = UITheme.createHeaderPanel("Interviews");

        JComboBox<String> filterBox = UITheme.createStyledComboBox(
                new String[]{"All Active", "Scheduled", "Completed", "Cancelled"});
        filterBox.setSelectedIndex(0);
        
        java.util.List<String> intCompanyNamesList = new java.util.ArrayList<>();
        intCompanyNamesList.add("All Companies");
        try {
            ResultSet rs = DB_connections.CompanyDB.getAllCompanies();
            while (rs != null && rs.next()) {
                intCompanyNamesList.add(rs.getString("name"));
            }
        } catch (SQLException ex) {}
        JComboBox<String> companyFilterBox = UITheme.createStyledComboBox(intCompanyNamesList.toArray(new String[0]));
        
        JButton filterBtn = UITheme.createSecondaryButton("Filter");
        JButton rescheduleBtn = UITheme.createPrimaryButton("Reschedule");
        JButton updateBtn = UITheme.createSecondaryButton("Update Status");
        JButton deleteBtn = UITheme.createDangerButton("Delete");

        filterBtn.addActionListener(e -> {
            String f = (String) filterBox.getSelectedItem();
            String cF = (String) companyFilterBox.getSelectedItem();
            modelActive.setRowCount(0);
            allDataActive.clear();
            loadInterviewData(modelActive, "All Active".equals(f) ? "!Accepted" : f, cF, allDataActive);

            modelAccepted.setRowCount(0);
            allDataAccepted.clear();
            loadInterviewData(modelAccepted, "Accepted", cF, allDataAccepted);
        });

        rescheduleBtn.addActionListener(e -> {
            JTable targetTable = tableActive.getSelectedRow() >= 0 ? tableActive : (tableAccepted.getSelectedRow() >= 0 ? tableAccepted : null);
            if (targetTable == null) { msg("Select an interview first."); return; }
            int row = targetTable.getSelectedRow();
            DefaultTableModel tModel = targetTable == tableActive ? modelActive : modelAccepted;

            int interviewId = (int) tModel.getValueAt(row, 0);
            String currentStudent = (String) tModel.getValueAt(row, 1);
            String currentType = (String) tModel.getValueAt(row, 4);
            // Current Date & Time format: "2026-03-25 08:00:00"
            String currentDateTimeStr = (String) tModel.getValueAt(row, 3);
            
            java.util.Date parsedDate = new java.util.Date();
            try {
                if (currentDateTimeStr != null && !currentDateTimeStr.isEmpty()) {
                     parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDateTimeStr);
                }
            } catch(Exception ex) {}

            com.toedter.calendar.JDateChooser dateSpinner = UITheme.createStyledDateChooser(parsedDate);
            JSpinner timeSpinner = UITheme.createStyledTimeSpinner(parsedDate);
            JTextField fType = UITheme.createStyledTextField(currentType);
            JTextField fNotes = UITheme.createStyledTextField("Rescheduled notes");

            Object[] fields = {"Student:", new javax.swing.JLabel(currentStudent), "New Date:", dateSpinner, "New Time:", timeSpinner, "Type:", fType, "Notes:", fNotes};
            if (UITheme.showStyledDialog(this, "Reschedule Interview", fields)) {
                try {
                    java.util.Date dateValue = dateSpinner.getDate();
                    java.util.Date timeValue = (java.util.Date) timeSpinner.getValue();
                    java.time.LocalDate localDate = dateValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    java.time.LocalTime localTime = timeValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
                    String newInterviewDate = java.time.LocalDateTime.of(localDate, localTime)
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    boolean success = InterviewDB.rescheduleInterview(interviewId, newInterviewDate, fType.getText(), fNotes.getText());
                    if (success) {
                        msg("Interview rescheduled successfully! Student has been notified.");
                        showInterviews();
                    } else {
                        msg("Failed to reschedule. Please try again.");
                    }
                } catch (Exception ex) {
                    msg("Invalid date/time value. Please select date and time from pickers.");
                }
            }
        });

        updateBtn.addActionListener(e -> {
            JTable targetTable = tableActive.getSelectedRow() >= 0 ? tableActive : (tableAccepted.getSelectedRow() >= 0 ? tableAccepted : null);
            if (targetTable == null) { msg("Select an interview first."); return; }
            int row = targetTable.getSelectedRow();
            DefaultTableModel tModel = targetTable == tableActive ? modelActive : modelAccepted;
            int interviewId = (int) tModel.getValueAt(row, 0);

            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Scheduled", "Qualified", "Rejected", "Accepted"});
            statusBox.setSelectedItem(tModel.getValueAt(row, 5));
            JTextField fNotes = UITheme.createStyledTextField("Update notes");

            Object[] fields = {"Status:", statusBox, "Notes:", fNotes};
            if (UITheme.showStyledDialog(this, "Update Interview", fields)) {
                String newStatus = (String) statusBox.getSelectedItem();
                String res = InterviewDB.updateInterviewStatus(interviewId, newStatus, fNotes.getText());
                if ("FINAL".equals(res)) {
                    msg("Final round qualified! Placement record created & student notified.");
                } else if ("NEXT_ROUND".equals(res)) {
                    msg("Student qualified! Next round is queued as 'Pending Schedule'.");
                } else if ("SUCCESS".equals(res)) {
                    msg("Interview updated successfully.");
                } else {
                    msg("Failed to update interview.");
                }
                showInterviews();
            }
        });

        deleteBtn.addActionListener(e -> {
            JTable targetTable = tableActive.getSelectedRow() >= 0 ? tableActive : (tableAccepted.getSelectedRow() >= 0 ? tableAccepted : null);
            if (targetTable == null) { msg("Select an interview first."); return; }
            int row = targetTable.getSelectedRow();
            DefaultTableModel tModel = targetTable == tableActive ? modelActive : modelAccepted;
            InterviewDB.deleteInterview((int) tModel.getValueAt(row, 0));
            showInterviews();
        });

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionBar.setOpaque(false);
        actionBar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        actionBar.add(filterBox);
        actionBar.add(companyFilterBox);
        actionBar.add(filterBtn);
        actionBar.add(rescheduleBtn);
        actionBar.add(updateBtn);
        actionBar.add(deleteBtn);

        contentPanel.removeAll();
        contentPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));
        centerPanel.add(stats, BorderLayout.NORTH);

        JPanel tabLeft = new JPanel(new BorderLayout(0, 5));
        tabLeft.setOpaque(false);
        tabLeft.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel pLeftTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10)); 
        pLeftTop.setOpaque(false);
        pLeftTop.add(searchPanelActive);
        tabLeft.add(pLeftTop, BorderLayout.NORTH);
        tabLeft.add(UITheme.createStyledScrollPane(tableActive), BorderLayout.CENTER);

        JPanel tabRight = new JPanel(new BorderLayout(0, 5));
        tabRight.setOpaque(false);
        tabRight.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel pRightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10)); 
        pRightTop.setOpaque(false);
        pRightTop.add(searchPanelAccepted);
        tabRight.add(pRightTop, BorderLayout.NORTH);
        tabRight.add(UITheme.createStyledScrollPane(tableAccepted), BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        tabbedPane.addTab("Current Pipeline", tabLeft);
        tabbedPane.addTab("Final Selection (Accepted)", tabRight);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);
        
        centerPanel.add(contentWrapper, BorderLayout.CENTER);

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(actionBar, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadInterviewData(DefaultTableModel model, String statusFilter, String companyFilter, java.util.List<Object[]> allData) {
        try {
            ResultSet rs = InterviewDB.getAllInterviews();
            while (rs != null && rs.next()) {
                String cname = rs.getString("cname");
                String status = rs.getString("status");
                if (status != null) status = status.trim();
                
                boolean statusMatch = false;
                if (statusFilter == null) {
                    statusMatch = true;
                } else if (statusFilter.startsWith("!")) {
                    statusMatch = status == null || !status.equals(statusFilter.substring(1));
                } else {
                    statusMatch = status != null && statusFilter.equals(status);
                }
                boolean companyMatch = (companyFilter == null || companyFilter.equals("All Companies") || companyFilter.equals(cname));
                
                if (statusMatch && companyMatch) {
                    Object[] row = {rs.getInt("id"), rs.getString("sname") + " (" + rs.getString("s_id") + ")",
                            cname, rs.getString("interview_date"),
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
