import java.time.LocalDate;
// import java.util.*;
public class Drive {
    LocalDate Startdate;
    LocalDate Enddate;
    int requirement;
    double LPA;
    Company company;
    double cutoffGPA;
    public Drive(LocalDate startdate, LocalDate enddate, int requirement, double LPA, Company company) {
        Startdate = startdate;
        Enddate = enddate;
        this.requirement = requirement;
        this.LPA = LPA;
        this.company = company;
    }

}
