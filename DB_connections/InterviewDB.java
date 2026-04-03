package DB_connections;

import java.sql.*;

public class InterviewDB {

    // Schedule interview
    public static boolean scheduleInterview(int applicationId, String interviewDate, String interviewType, String notes) {
        try (Connection c = connecton.getConnection()) {
            // First get student details for notifications
            PreparedStatement ps1 = c.prepareStatement(
                    "SELECT a.s_id, s.name, s.email, co.name AS cname " +
                    "FROM applications a " +
                    "JOIN student s ON a.s_id = s.rollnum " +
                    "JOIN drive d ON a.driveId = d.D_id " +
                    "JOIN company co ON d.companyId = co.id " +
                    "WHERE a.A_id = ?");
            ps1.setInt(1, applicationId);
            ResultSet rs = ps1.executeQuery();

            String studentRoll = null, studentName = null, studentEmail = null, companyName = null;
            if (rs.next()) {
                studentRoll = rs.getString("s_id");
                studentName = rs.getString("name");
                studentEmail = rs.getString("email");
                companyName = rs.getString("cname");
            }

            // Schedule the interview
            PreparedStatement ps2 = c.prepareStatement(
                    "INSERT INTO interviews(application_id, interview_date, interview_type, notes) VALUES(?,?,?,?)");
            ps2.setInt(1, applicationId);
            ps2.setString(2, interviewDate);
            ps2.setString(3, interviewType);
            ps2.setString(4, notes);
            boolean success = ps2.executeUpdate() > 0;

            if (success && studentRoll != null) {
                // Send notification to student
                NotificationDB.sendNotification("student", studentRoll,
                        "Interview scheduled for " + companyName + " on " + interviewDate + " (" + interviewType + ")");

                // Send email to student
                utils.EmailUtil.sendEmail(studentEmail, "Interview Scheduled",
                        "Dear " + studentName + ",\n\nYour interview for " + companyName + " has been scheduled.\n\n" +
                        "Date & Time: " + interviewDate + "\n" +
                        "Type: " + interviewType + "\n" +
                        "Notes: " + (notes != null ? notes : "N/A") + "\n\n" +
                        "Best regards,\nPlacement Cell");
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get interviews for admin
    public static ResultSet getAllInterviews() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery(
                    "SELECT i.*, a.s_id, s.name AS sname, co.name AS cname, d.D_id " +
                    "FROM interviews i " +
                    "JOIN applications a ON i.application_id = a.A_id " +
                    "JOIN student s ON a.s_id = s.rollnum " +
                    "JOIN drive d ON a.driveId = d.D_id " +
                    "JOIN company co ON d.companyId = co.id " +
                    "ORDER BY i.interview_date DESC");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get interviews for a student
    public static ResultSet getInterviewsByStudent(String rollnum) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT i.*, co.name AS cname, d.D_id FROM interviews i " +
                    "JOIN applications a ON i.application_id = a.A_id " +
                    "JOIN drive d ON a.driveId = d.D_id " +
                    "JOIN company co ON d.companyId = co.id " +
                    "WHERE a.s_id = ? ORDER BY i.interview_date DESC");
            ps.setString(1, rollnum);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update interview status
    public static String updateInterviewStatus(int interviewId, String status, String notes) {
        try (Connection c = connecton.getConnection()) {
            // First get interview details for placement creation or next round
            PreparedStatement ps1 = c.prepareStatement(
                    "SELECT a.s_id, a.driveId, s.name, s.email, co.name AS cname, d.lpa, d.rounds_list, i.interview_type, i.interview_date " +
                    "FROM interviews i " +
                    "JOIN applications a ON i.application_id = a.A_id " +
                    "JOIN student s ON a.s_id = s.rollnum " +
                    "JOIN drive d ON a.driveId = d.D_id " +
                    "JOIN company co ON d.companyId = co.id " +
                    "WHERE i.id = ?");
            ps1.setInt(1, interviewId);
            ResultSet rs = ps1.executeQuery();

            String studentRoll = null, studentName = null, studentEmail = null, companyName = null;
            String roundsListStr = null, currentType = null;
            int driveId = 0, companyId = 0;
            double salary = 0;
            if (rs.next()) {
                studentRoll = rs.getString("s_id");
                studentName = rs.getString("name");
                studentEmail = rs.getString("email");
                companyName = rs.getString("cname");
                driveId = rs.getInt("driveId");
                salary = rs.getDouble("lpa");
                roundsListStr = rs.getString("rounds_list");
                currentType = rs.getString("interview_type");

                PreparedStatement psCompany = c.prepareStatement("SELECT companyId FROM drive WHERE D_id = ?");
                psCompany.setInt(1, driveId);
                ResultSet rsCompany = psCompany.executeQuery();
                if (rsCompany.next()) companyId = rsCompany.getInt("companyId");
            }

            boolean isFinalRound = false;
            String nextRoundName = null;
            if ("Qualified".equals(status)) {
                if (roundsListStr != null && !roundsListStr.isEmpty() && currentType != null) {
                    String[] rounds = roundsListStr.split(",");
                    for (int i = 0; i < rounds.length; i++) {
                        if (rounds[i].trim().equalsIgnoreCase(currentType.trim())) {
                            if (i == rounds.length - 1) {
                                isFinalRound = true;
                                status = "Accepted"; // Auto upgrade to Accepted
                            } else {
                                nextRoundName = rounds[i+1].trim();
                            }
                            break;
                        }
                    }
                    if (!isFinalRound && nextRoundName == null) nextRoundName = "Next Round";
                } else {
                    isFinalRound = true;
                    status = "Accepted"; // Fallback
                }
            } else if ("Accepted".equals(status)) {
                isFinalRound = true;
            }

            PreparedStatement ps2 = c.prepareStatement("UPDATE interviews SET status=?, notes=? WHERE id=?");
            ps2.setString(1, status);
            ps2.setString(2, notes);
            ps2.setInt(3, interviewId);
            boolean success = ps2.executeUpdate() > 0;

            if (success && studentRoll != null) {
                if (isFinalRound) {
                    PreparedStatement ps3 = c.prepareStatement(
                            "INSERT INTO placements(student_roll, company_id, drive_id, salary, placement_date) VALUES(?,?,?, ?, CURDATE())");
                    ps3.setString(1, studentRoll);
                    ps3.setInt(2, companyId);
                    ps3.setInt(3, driveId);
                    ps3.setDouble(4, salary * 100000);
                    ps3.executeUpdate();

                    PreparedStatement ps4 = c.prepareStatement(
                            "UPDATE applications SET status='Accepted' WHERE A_id = (SELECT application_id FROM interviews WHERE id = ?)");
                    ps4.setInt(1, interviewId);
                    ps4.executeUpdate();

                    NotificationDB.sendNotification("student", studentRoll,
                            "Congratulations! You have been placed at " + companyName + " with a package of " + salary + " LPA.");
                    utils.EmailUtil.sendEmail(studentEmail, "Placement Confirmed",
                            "Dear " + studentName + ",\n\nCongratulations! You have been successfully placed at " + companyName + ".\n\n" +
                            "Package: " + salary + " LPA\n" +
                            "Placement Date: " + java.time.LocalDate.now() + "\n\n" +
                            "Best regards,\nPlacement Cell");
                    return "FINAL";
                } else if ("Qualified".equals(status) && nextRoundName != null) {
                    PreparedStatement psNext = c.prepareStatement(
                            "INSERT INTO interviews(application_id, interview_date, interview_type, status, notes) " +
                            "SELECT application_id, interview_date, ?, 'Pending Schedule', '' FROM interviews WHERE id=?");
                    psNext.setString(1, nextRoundName);
                    psNext.setInt(2, interviewId);
                    psNext.executeUpdate();

                    NotificationDB.sendNotification("student", studentRoll,
                            "Congratulations on qualifying! Your next round is: " + nextRoundName + ". It will be scheduled shortly.");
                    utils.EmailUtil.sendEmail(studentEmail, "Qualified for Next Round",
                            "Dear " + studentName + ",\n\nCongratulations! You have qualified the current round.\n\n" +
                            "Next Round: " + nextRoundName + "\nIt will be scheduled shortly.\n\nBest regards,\nPlacement Cell");
                    return "NEXT_ROUND";
                }
            }
            return success ? "SUCCESS" : "ERROR";
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    // Reschedule interview
    public static boolean rescheduleInterview(int interviewId, String newDate, String newType, String newNotes) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE interviews SET interview_date=?, interview_type=?, notes=? WHERE id=?");
            ps.setString(1, newDate);
            ps.setString(2, newType);
            ps.setString(3, newNotes);
            ps.setInt(4, interviewId);
            boolean success = ps.executeUpdate() > 0;
            
            if (success) {
                // Fetch student info to send notification
                PreparedStatement psInfo = c.prepareStatement(
                        "SELECT s.email, s.name, co.name AS cname, s.rollnum " +
                        "FROM interviews i " +
                        "JOIN applications a ON i.application_id = a.A_id " +
                        "JOIN student s ON a.s_id = s.rollnum " +
                        "JOIN drive d ON a.driveId = d.D_id " +
                        "JOIN company co ON d.companyId = co.id " +
                        "WHERE i.id = ?");
                psInfo.setInt(1, interviewId);
                ResultSet rs = psInfo.executeQuery();
                if (rs.next()) {
                    String sEmail = rs.getString("email");
                    String sName = rs.getString("name");
                    String cName = rs.getString("cname");
                    String roll = rs.getString("rollnum");
                    
                    NotificationDB.sendNotification("student", roll,
                            "Your interview for " + cName + " has been rescheduled to " + newDate + " (" + newType + ")");
                    
                    utils.EmailUtil.sendEmail(sEmail, "Interview Rescheduled",
                            "Dear " + sName + ",\n\nYour interview for " + cName + " has been rescheduled.\n\n" +
                            "New Date & Time: " + newDate + "\n" +
                            "Type: " + newType + "\n" +
                            "Notes: " + (newNotes != null ? newNotes : "N/A") + "\n\n" +
                            "Best regards,\nPlacement Cell");
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete interview
    public static boolean deleteInterview(int id) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM interviews WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}