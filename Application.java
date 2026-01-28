public class Application {
    Student student;
    Drive drive;
    String applicationDate;
    String status; //"Applied", "Shortlisted", "Rejected", "Accepted"

    public Application(Student student, Drive drive, String applicationDate, String status) {
        this.student = student;
        this.drive = drive;
        this.applicationDate = applicationDate;
        this.status = status;
    }
}
