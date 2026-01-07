package Models;

import java.time.LocalDate;
public class Aircraft {
    int Id;
    int SittingCapacity;
    String Model;
    LocalDate DateOfManufacturing; // to calc age of aircraft
    int No_PastProblems;

    public Aircraft(int id, int SittingCapacity, String Model, LocalDate DateOfManufacturing, int No_PastProblems) {
        this.Id = id;
        this.SittingCapacity = SittingCapacity;
        this.Model = Model;
        this.DateOfManufacturing = DateOfManufacturing;
        this.No_PastProblems = No_PastProblems;
    }

    public int getId() {
        return Id;
    }

    public int getSittingCapacity() {
        return SittingCapacity;
    }

    public String getModel() {
        return Model;
    }

    public LocalDate getDateOfManufacturing() {
        return DateOfManufacturing;
    }

    public int getNo_PastProblems() {
        return No_PastProblems;
    }
}
