package DB_connections;

import java.sql.*;

public class ApplicationDB {
    public static boolean addApplication(int driveId, String studentId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO applications(driveId, s_id, applicationDate, status) VALUES(?,?,CURDATE(),'Applied')");
            ps.setInt(1, driveId);
            ps.setString(2, studentId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read all (with student name & company name)
    public static ResultSet getAllApplications(String statusFilter) {
        try {
            Connection c = connecton.getConnection();
            String sql = "SELECT a.*, s.name AS sname, co.name AS cname "
                    + "FROM applications a "
                    + "JOIN student s ON a.s_id=s.rollnum "
                    + "JOIN drive d ON a.driveId=d.D_id "
                    + "JOIN company co ON d.companyId=co.id";
            if (statusFilter != null)
                sql += " WHERE a.status=?";
            sql += " ORDER BY a.A_id DESC";
            PreparedStatement ps = c.prepareStatement(sql);
            if (statusFilter != null)
                ps.setString(1, statusFilter);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read applications for a specific student
    public static ResultSet getApplicationsByStudent(String rollnum) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT a.A_id, a.driveId, co.name AS cname, a.applicationDate, a.status "
                            + "FROM applications a "
                            + "JOIN drive d ON a.driveId=d.D_id "
                            + "JOIN company co ON d.companyId=co.id "
                            + "WHERE a.s_id=? ORDER BY a.A_id DESC");
            ps.setString(1, rollnum);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update status
    public static boolean updateStatus(int appId, String newStatus) {
        try (Connection c = connecton.getConnection()) {
            // First get current application details for notifications
            PreparedStatement ps1 = c.prepareStatement(
                    "SELECT a.s_id, s.name, s.email, co.name AS cname, d.D_id " +
                    "FROM applications a " +
                    "JOIN student s ON a.s_id = s.rollnum " +
                    "JOIN drive d ON a.driveId = d.D_id " +
                    "JOIN company co ON d.companyId = co.id " +
                    "WHERE a.A_id = ?");
            ps1.setInt(1, appId);
            ResultSet rs = ps1.executeQuery();

            String studentRoll = null, studentName = null, studentEmail = null, companyName = null;
            int driveId = 0;
            if (rs.next()) {
                studentRoll = rs.getString("s_id");
                studentName = rs.getString("name");
                studentEmail = rs.getString("email");
                companyName = rs.getString("cname");
                driveId = rs.getInt("D_id");
            }

            // Update the status
            PreparedStatement ps2 = c.prepareStatement(
                    "UPDATE applications SET status=? WHERE A_id=?");
            ps2.setString(1, newStatus);
            ps2.setInt(2, appId);
            boolean success = ps2.executeUpdate() > 0;

            if (success && studentRoll != null) {
                // Send notifications based on status change
                if ("Shortlisted".equals(newStatus)) {
                    // Send notification to student
                    NotificationDB.sendNotification("student", studentRoll,
                            "Congratulations! You have been shortlisted for " + companyName + " placement drive.");

                    // Send email to student
                    utils.EmailUtil.sendApplicationUpdateEmail(studentEmail, companyName + " position", "shortlisted");
                }
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete (withdraw)
    public static boolean deleteApplication(int appId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM applications WHERE A_id=?");
            ps.setInt(1, appId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if student already applied to a drive
    public static boolean hasApplied(String rollnum, int driveId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM applications WHERE s_id=? AND driveId=?");
            ps.setString(1, rollnum);
            ps.setInt(2, driveId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
