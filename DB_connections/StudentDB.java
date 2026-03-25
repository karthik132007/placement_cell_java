package DB_connections;

import java.sql.*;

public class StudentDB {

    // Create
    public static boolean addStudent(String rollnum, String name, int age, String major, double gpa, String email, String password) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO student(rollnum, name, age, major, gpa, email, password) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1, rollnum);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, major);
            ps.setDouble(5, gpa);
            ps.setString(6, email);
            ps.setString(7, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read all
    public static ResultSet getAllStudents() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery("SELECT * FROM student ORDER BY rollnum");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Read one by rollnum
    public static ResultSet getStudentByRoll(String rollnum) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM student WHERE rollnum = ?");
            ps.setString(1, rollnum);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Validate login (rollnum + password)
    public static ResultSet validateStudent(String rollnum, String password) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT name FROM student WHERE rollnum = ? AND password = ?");
            ps.setString(1, rollnum);
            ps.setString(2, password);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update
    public static boolean updateStudent(String rollnum, String name, int age, String major, double gpa, String email, String password, String resumePath) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE student SET name=?, age=?, major=?, gpa=?, email=?, password=?, resume_path=? WHERE rollnum=?");
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, major);
            ps.setDouble(4, gpa);
            ps.setString(5, email);
            ps.setString(6, password);
            ps.setString(7, resumePath);
            ps.setString(8, rollnum);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public static boolean deleteStudent(String rollnum) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM student WHERE rollnum=?");
            ps.setString(1, rollnum);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if rollnum exists
    public static boolean rollExists(String rollnum) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT 1 FROM student WHERE rollnum = ?");
            ps.setString(1, rollnum);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if email exists
    public static boolean emailExists(String email) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT 1 FROM student WHERE email = ?");
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
