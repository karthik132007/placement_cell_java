import java.time.LocalDate;
// import java.util.*;
public class Drive {
    LocalDate Startdate;
    LocalDate Enddate;
    int availableSeats;
    double LPA;
    Company company;
    double MinGPA;
    public Drive(LocalDate startdate, LocalDate enddate, int availableSeats, double LPA, Company company, double MinGPA) {
        Startdate = startdate;
        Enddate = enddate;
        this.availableSeats = availableSeats;
        this.LPA = LPA;
        this.company = company;
        this.MinGPA = MinGPA;
    }

}
