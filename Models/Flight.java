package Models;
import java.time.LocalTime;
public class Flight {
    int FlightId;
    String StartLocation;
    String Destination;
    LocalTime Duration;
    Aircraft aircraft;
    
    public Flight(String StartLocation, String Destination, LocalTime Duration, Aircraft aircraft) {
        this.StartLocation = StartLocation;
        this.Destination = Destination;
        this.Duration = Duration;
        this.aircraft = aircraft;
    }

    public int getFlightId() {
        return FlightId;
    }

    public void setFlightId(int flightId) {
        FlightId = flightId;
    }

    public String getStartLocation() {
        return StartLocation;
    }

    public String getDestination() {
        return Destination;
    }

    public LocalTime getDuration() {
        return Duration;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}
