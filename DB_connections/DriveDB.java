package DB_connections;

import java.sql.*;

public class DriveDB {

    // Create
    public static boolean addDrive(int companyId, String startDate, String endDate, int seats, double lpa,
            double mingpa, String roundsList) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO drive(companyId, start_date, end_date, availableSeats, lpa, mingpa, rounds_list) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1, companyId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setInt(4, seats);
            ps.setDouble(5, lpa);
            ps.setDouble(6, mingpa);
            ps.setString(7, roundsList);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read all (with company name)
    public static ResultSet getAllDrives() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery(
                    "SELECT d.*, c.name AS cname FROM drive d JOIN company c ON d.companyId=c.id ORDER BY d.D_id");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read drives available for a student (checks GPA)
    public static ResultSet getDrivesForStudent(double studentGpa) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT d.*, c.name AS cname FROM drive d JOIN company c ON d.companyId=c.id "
                            + "WHERE d.mingpa <= ? ORDER BY d.D_id");
            ps.setDouble(1, studentGpa);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read one
    public static ResultSet getDriveById(int dId) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT d.*, c.name AS cname FROM drive d JOIN company c ON d.companyId=c.id WHERE d.D_id=?");
            ps.setInt(1, dId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update
    public static boolean updateDrive(int dId, int companyId, String startDate, String endDate, int seats, double lpa,
            double mingpa, String roundsList) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE drive SET companyId=?, start_date=?, end_date=?, availableSeats=?, lpa=?, mingpa=?, rounds_list=? WHERE D_id=?");
            ps.setInt(1, companyId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setInt(4, seats);
            ps.setDouble(5, lpa);
            ps.setDouble(6, mingpa);
            ps.setString(7, roundsList);
            ps.setInt(8, dId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public static boolean deleteDrive(int dId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM drive WHERE D_id=?");
            ps.setInt(1, dId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
