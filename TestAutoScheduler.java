import utils.AutoScheduler;
import DB_connections.ApplicationDB;
import java.sql.ResultSet;

public class TestAutoScheduler {
    public static void main(String[] args) {
        System.out.println("Starting Test AutoScheduler...");
        try {
            // Find a drive that has shortlisted candidates
            ResultSet apps = ApplicationDB.getAllApplications("Shortlisted");
            int driveIdToTest = -1;
            int count = 0;
            while(apps != null && apps.next()) {
                driveIdToTest = apps.getInt("driveId");
                count++;
            }
            
            if (driveIdToTest == -1) {
                System.out.println("No shortlisted applications found for any drive to test with.");
                // For the sake of the test, let's just call it with drive ID 1
                System.out.println("Executing AutoScheduler with Drive ID=1...");
                AutoScheduler.scheduleDriveInterviews(1);
            } else {
                System.out.println("Found Drive ID " + driveIdToTest + " with " + count + " shortlisted applications.");
                int scheduled = AutoScheduler.scheduleDriveInterviews(driveIdToTest);
                System.out.println("Successfully scheduled " + scheduled + " interviews for Drive ID " + driveIdToTest);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
