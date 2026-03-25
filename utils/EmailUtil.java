package utils;

// Note: JavaMail API not included. This is a placeholder for email functionality.
// To enable real emails, add JavaMail JAR to classpath and uncomment the imports.

public class EmailUtil {

    // Placeholder email sending - prints to console instead
    public static boolean sendEmail(String to, String subject, String body) {
        System.out.println("=== EMAIL SIMULATION ===");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("========================");
        return true; // Always return true for simulation
    }

    public static void sendNotificationEmail(String studentEmail, String message) {
        sendEmail(studentEmail, "Placement Cell Notification", message);
    }

    public static void sendApplicationUpdateEmail(String studentEmail, String driveName, String status) {
        String subject = "Application Status Update";
        String body = "Dear Student,\n\nYour application for " + driveName + " has been " + status.toLowerCase() + ".\n\nBest regards,\nPlacement Cell";
        sendEmail(studentEmail, subject, body);
    }
}