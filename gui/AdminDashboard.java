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
    private static final String[] NAV_ICONS = {"👥", "🏢", "📋", "📄"};
    private static final String[] NAV_LABELS = {"Students", "Companies", "Drives", "Applications"};

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

            JTextField fS = UITheme.createStyledTextField("YYYY-MM-DD");
            JTextField fE = UITheme.createStyledTextField("YYYY-MM-DD");
            JTextField fSeats = UITheme.createStyledTextField("e.g. 50");
            JTextField fLPA = UITheme.createStyledTextField("e.g. 12.5");
            JTextField fGPA = UITheme.createStyledTextField("e.g. 7.0");
            Object[] fields = {"Company:", cb, "Start:", fS, "End:", fE,
                    "Seats:", fSeats, "LPA:", fLPA, "Min GPA:", fGPA};
            if (UITheme.showStyledDialog(this, "Create Drive", fields)) {
                try {
                    int compId = Integer.parseInt(((String) cb.getSelectedItem()).split(" - ")[0]);
                    DriveDB.addDrive(compId, fS.getText(), fE.getText(),
                            Integer.parseInt(fSeats.getText()),
                            Double.parseDouble(fLPA.getText()),
                            Double.parseDouble(fGPA.getText()));
                    showDrives();
                } catch (Exception ex) {
                    msg("Invalid input.");
                }
            }
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
        contentPanel.add(UITheme.createActionBar(addBtn, deleteBtn), BorderLayout.SOUTH);
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

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionBar.setOpaque(false);
        actionBar.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        actionBar.add(filterBox);
        actionBar.add(filterBtn);
        actionBar.add(statusBtn);

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

    private void msg(String m) {
        UITheme.showMessage(this, m);
    }
}
