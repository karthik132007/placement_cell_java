package Models;
public class Runway {
    int runway_number;
    int Length;

    public int getRunway_number() {
        return runway_number;
    }

    public int getLength() {
        return Length;
    }

    public Runway(int num, int Length) {
        this.runway_number = num;
        this.Length = Length;
    }
}
