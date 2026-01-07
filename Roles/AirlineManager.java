package Roles;
import Models.Airline;
import Models.Aircraft;
import Models.Flight;
import Models.FlightCreationRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AirlineManager {
    private Airline airline;
    private List<Aircraft> fleet;
    private List<Flight> scheduledFlights;
    
    public AirlineManager(int id, String name) {
        this.airline = new Airline(id, name);
        this.fleet = new ArrayList<>();
        this.scheduledFlights = new ArrayList<>();
    }

    // Manage airline fleet - add aircraft to fleet
    public void addAircraftToFleet(Aircraft aircraft) {
        fleet.add(aircraft);
        airline.addAircraft(aircraft);
        System.out.println("Aircraft " + aircraft.getModel() + " added to " + airline.getName() + "'s fleet");
    }

    // Create flight request (to be sent to System Engine)
    public FlightCreationRequest createFlightRequest(String sourceAirport, 
                                                      String destinationAirport,
                                                      Aircraft aircraft, 
                                                      LocalDate flightDate, 
                                                      LocalTime preferredDepartureTime) {
        // Validate aircraft availability
        if (!fleet.contains(aircraft)) {
            System.out.println("Error: Aircraft not in airline's fleet!");
            return null;
        }

        FlightCreationRequest request = new FlightCreationRequest(
            String.valueOf(airline.getId()),
            sourceAirport,
            destinationAirport,
            String.valueOf(aircraft.getId()),
            flightDate,
            preferredDepartureTime
        );

        System.out.println("Flight request created: " + sourceAirport + " → " + destinationAirport);
        System.out.println("Preferred departure: " + preferredDepartureTime);
        System.out.println("Request sent to System Engine for automatic resource allocation...");
        
        return request;
    }

    // View assigned schedule (read-only - assigned by System Engine)
    public void viewAssignedSchedule() {
        System.out.println("\n=== ASSIGNED FLIGHT SCHEDULE ===");
        System.out.println("Airline: " + airline.getName());
        if (scheduledFlights.isEmpty()) {
            System.out.println("No flights scheduled yet.");
        } else {
            for (Flight flight : scheduledFlights) {
                System.out.println("Flight " + flight.getFlightId() + ": " + 
                                   flight.getStartLocation() + " → " + flight.getDestination());
                System.out.println("  Runway: [Assigned by System]");
                System.out.println("  Gate: [Assigned by System]");
                System.out.println("  Time Slot: [Assigned by System]");
            }
        }
    }

    // View fleet information
    public void viewFleet() {
        System.out.println("\n=== FLEET INFORMATION ===");
        System.out.println("Airline: " + airline.getName());
        System.out.println("Total Aircraft: " + fleet.size());
        for (Aircraft aircraft : fleet) {
            System.out.println("  - " + aircraft.getModel() + 
                             " (Capacity: " + aircraft.getSittingCapacity() + 
                             ", ID: " + aircraft.getId() + ")");
        }
    }

    // Get airline information
    public Airline getAirline() {
        return airline;
    }

    // View operational reports
    public void viewOperationalReports() {
        System.out.println("\n=== OPERATIONAL REPORT ===");
        System.out.println("Airline: " + airline.getName());
        System.out.println("Fleet Size: " + fleet.size());
        System.out.println("Scheduled Flights: " + scheduledFlights.size());
        System.out.println("Revenue Statistics: [To be implemented]");
    }

    // Add scheduled flight (called by System Engine after allocation)
    public void addScheduledFlight(Flight flight) {
        scheduledFlights.add(flight);
    }
}
