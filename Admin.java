import DB_connections.*;

public class Admin {

    

    public boolean addDrive(int companyId, String startDate, String endDate,
            int seats, double lpa, double minGpa, String roundsList) {
        return DriveDB.addDrive(companyId, startDate, endDate, seats, lpa, minGpa, roundsList);
    }

    public boolean updateDrive(int driveId, int companyId, String startDate, String endDate,
            int seats, double lpa, double minGpa, String roundsList) {
        return DriveDB.updateDrive(driveId, companyId, startDate, endDate, seats, lpa, minGpa, roundsList);
    }

    public boolean deleteDrive(int driveId) {
        return DriveDB.deleteDrive(driveId);
    }

  

    public boolean addCompany(String name, String location, String industry, String hrContact) {
        return CompanyDB.addCompany(name, location, industry, hrContact);
    }

    public boolean updateCompany(int id, String name, String location, String industry, String hrContact) {
        return CompanyDB.updateCompany(id, name, location, industry, hrContact);
    }

    public boolean deleteCompany(int id) {
        return CompanyDB.deleteCompany(id);
    }



    public boolean deleteStudent(String rollNum) {
        return StudentDB.deleteStudent(rollNum);
    }


    public boolean updateApplicationStatus(int appId, String newStatus) {
        return ApplicationDB.updateStatus(appId, newStatus);
    }

    public boolean deleteApplication(int appId) {
        return ApplicationDB.deleteApplication(appId);
    }
}
