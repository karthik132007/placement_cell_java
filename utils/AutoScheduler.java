package utils;

import DB_connections.ApplicationDB;
import DB_connections.DriveDB;
import DB_connections.InterviewDB;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoScheduler {

    /**
     * Automatically schedules interviews for shortlisted applications of a given drive.
     * It limits to the last 3 days of the drive's dates, between 08:00 AM and 09:00 PM,
     * assigning a 20-minute slot for each student.
     * 
     * @param driveId The ID of the Drive for which to schedule interviews.
     * @return Number of successful schedules.
     */
    public static int scheduleDriveInterviews(int driveId) {
        int scheduledCount = 0;

        try {
            // 1. Fetch Drive Dates
            ResultSet driveRs = DriveDB.getDriveById(driveId);
            if (driveRs == null || !driveRs.next()) {
                System.out.println("Drive not found: " + driveId);
                return 0;
            }

            // Assuming YYYY-MM-DD
            LocalDate startDate = LocalDate.parse(driveRs.getString("start_date"));
            LocalDate endDate = LocalDate.parse(driveRs.getString("end_date"));

            // 2. Fetch Shortlisted Applications for the specific drive
            ResultSet appsRs = ApplicationDB.getAllApplications("Shortlisted");
            List<Integer> appIdsToSchedule = new ArrayList<>();
            while (appsRs != null && appsRs.next()) {
                if (appsRs.getInt("driveId") == driveId) {
                    // Check if interview already scheduled for this app
                    // Simplistically, we could check if it exists in InterviewDB.
                    // For now, we assume if it's 'Shortlisted' without an interview it's eligible.
                    appIdsToSchedule.add(appsRs.getInt("A_id"));
                }
            }

            if (appIdsToSchedule.isEmpty()) {
                System.out.println("No shortlisted applications to schedule for drive: " + driveId);
                return 0;
            }

            // 3. Compute up to the last 3 days
            List<LocalDate> interviewDays = new ArrayList<>();
            LocalDate curr = endDate;
            while (interviewDays.size() < 3 && !curr.isBefore(startDate)) {
                interviewDays.add(curr);
                curr = curr.minusDays(1);
            }
            Collections.sort(interviewDays);

            // 4. Generate Timeslots and Assign
            int currentDayIdx = 0;
            LocalTime currentTime = LocalTime.of(8, 0); // 8:00 AM
            LocalTime endTime = LocalTime.of(21, 0);    // 9:00 PM

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");

            for (int appId : appIdsToSchedule) {
                if (currentDayIdx >= interviewDays.size()) {
                    System.out.println("Warning: Not enough slots/days to schedule all students! Reached capacity.");
                    break;
                }

                LocalDate day = interviewDays.get(currentDayIdx);
                LocalDateTime interviewDateTime = LocalDateTime.of(day, currentTime);

                String interviewDateStr = interviewDateTime.format(formatter);
                
                // Keep notes as Auto-scheduled
                boolean success = InterviewDB.scheduleInterview(appId, interviewDateStr, "Technical", "Auto-scheduled");
                if (success) {
                    scheduledCount++;
                    System.out.println("Scheduled appId: " + appId + " at " + interviewDateStr);
                } else {
                    System.out.println("Failed to schedule appId: " + appId);
                }

                // Advance by 20 mins
                currentTime = currentTime.plusMinutes(20);
                if (!currentTime.isBefore(endTime)) {
                    // Move to the next day
                    currentTime = LocalTime.of(8, 0);
                    currentDayIdx++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error scheduling interviews: " + e.getMessage());
            e.printStackTrace();
        }

        return scheduledCount;
    }
}
