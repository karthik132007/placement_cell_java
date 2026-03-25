import DB_connections.*;
import java.sql.ResultSet;

public class TestInterview {
    public static void main(String[] args) {
        try {
            // First check existing interviews
            System.out.println("Existing Interviews:");
            ResultSet rs = InterviewDB.getAllInterviews();
            while (rs != null && rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Student: " + rs.getString("sname")
                        + " (" + rs.getString("s_id") + "), Application ID: " + rs.getInt("application_id"));
            }

            // Check existing applications
            System.out.println("\nExisting Applications:");
            ResultSet apps = ApplicationDB.getAllApplications("Shortlisted");
            int firstAppId = -1;
            while (apps != null && apps.next()) {
                System.out.println("A_id: " + apps.getInt("A_id") + ", Student: " + apps.getString("sname"));
                if (firstAppId == -1)
                    firstAppId = apps.getInt("A_id");
            }

            if (firstAppId != -1) {
                System.out.println("\nScheduling Interview for A_id: " + firstAppId);
                boolean success = InterviewDB.scheduleInterview(firstAppId, "2026-03-25 10:00:00", "Technical",
                        "Test notes");
                System.out.println("Schedule successful: " + success);

                System.out.println("\nInterviews after scheduling:");
                ResultSet rs2 = InterviewDB.getAllInterviews();
                while (rs2 != null && rs2.next()) {
                    System.out.println("ID: " + rs2.getInt("id") + ", Student: " + rs2.getString("sname")
                            + " (" + rs2.getString("s_id") + "), Application ID: " + rs2.getInt("application_id")
                            + ", Status: " + rs2.getString("status"));
                }
            } else {
                System.out.println("\nNo shortlisted applications found to schedule.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
