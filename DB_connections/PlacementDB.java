package DB_connections;

import java.sql.*;

public class PlacementDB {

    // Add placement
    public static boolean addPlacement(String studentRoll, int companyId, int driveId, double salary, String placementDate) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO placements(student_roll, company_id, drive_id, salary, placement_date) VALUES(?,?,?,?,?)");
            ps.setString(1, studentRoll);
            ps.setInt(2, companyId);
            ps.setInt(3, driveId);
            ps.setDouble(4, salary);
            ps.setString(5, placementDate);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all placements
    public static ResultSet getAllPlacements() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery(
                    "SELECT p.*, s.name AS sname, co.name AS cname FROM placements p " +
                    "JOIN student s ON p.student_roll = s.rollnum " +
                    "JOIN company co ON p.company_id = co.id ORDER BY p.placement_date DESC");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get placements for a student
    public static ResultSet getPlacementsByStudent(String rollnum) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT p.*, co.name AS cname FROM placements p " +
                    "JOIN company co ON p.company_id = co.id WHERE p.student_roll = ? ORDER BY p.placement_date DESC");
            ps.setString(1, rollnum);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update placement
    public static boolean updatePlacement(int id, String studentRoll, int companyId, int driveId, double salary, String placementDate) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE placements SET student_roll=?, company_id=?, drive_id=?, salary=?, placement_date=? WHERE id=?");
            ps.setString(1, studentRoll);
            ps.setInt(2, companyId);
            ps.setInt(3, driveId);
            ps.setDouble(4, salary);
            ps.setString(5, placementDate);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete placement
    public static boolean deletePlacement(int id) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM placements WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}