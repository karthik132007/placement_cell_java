package Models;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightCreationRequest {

    private String airlineId;
    private String sourceAirport;
    private String destinationAirport;
    private String aircraftId;
    private LocalDate flightDate;
    private LocalTime preferredDepartureTime;

    public FlightCreationRequest(String airlineId, String sourceAirport, String destinationAirport,
                                  String aircraftId, LocalDate flightDate, LocalTime preferredDepartureTime) {
        this.airlineId = airlineId;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.aircraftId = aircraftId;
        this.flightDate = flightDate;
        this.preferredDepartureTime = preferredDepartureTime;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public String getAircraftId() {
        return aircraftId;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public LocalTime getPreferredDepartureTime() {
        return preferredDepartureTime;
    }
}
