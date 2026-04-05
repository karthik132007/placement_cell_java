package DB_connections;

import java.sql.*;

public class NotificationDB {

    // Send notification
    public static boolean sendNotification(String recipientType, String recipientId, String message) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO notifications(recipient_type, recipient_id, message) VALUES(?,?,?)");
            ps.setString(1, recipientType);
            ps.setString(2, recipientId);
            ps.setString(3, message);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get notifications for a recipient
    public static ResultSet getNotifications(String recipientType, String recipientId) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT * FROM notifications WHERE recipient_type = ? AND recipient_id = ? ORDER BY sent_date DESC");
            ps.setString(1, recipientType);
            ps.setString(2, recipientId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all notifications (for admin)
    public static ResultSet getAllNotifications() {
        try {
            Connection c = connecton.getConnection();
            return c.createStatement().executeQuery(
                    "SELECT * FROM notifications ORDER BY sent_date DESC");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get unread count
    public static int getUnreadCount(String recipientType, String recipientId) {
        try {
            Connection c = connecton.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT COUNT(*) FROM notifications WHERE recipient_type = ? AND recipient_id = ? AND is_read = FALSE");
            ps.setString(1, recipientType);
            ps.setString(2, recipientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Mark as read
    public static boolean markAsRead(int notificationId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE notifications SET is_read = TRUE WHERE id = ?");
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mark as unread
    public static boolean markAsUnread(int notificationId) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE notifications SET is_read = FALSE WHERE id = ?");
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete notification
    public static boolean deleteNotification(int id) {
        try (Connection c = connecton.getConnection()) {
            PreparedStatement ps = c.prepareStatement("DELETE FROM notifications WHERE id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}