package Roles;
import Models.Airport;
import Models.Gate;
import Models.Runway;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AirportAuth {
    Airport airport;
    int safetyBuffer; // minutes between flights on same runway
    LocalTime operatingHoursStart;
    LocalTime operatingHoursEnd;

    public AirportAuth(String airportName, int capacity) {
        this.airport = new Airport();
        this.airport.setName(airportName);
        this.airport.setCapacity(capacity);
        this.airport.setGates(new ArrayList<>());
        this.airport.setRunways(new ArrayList<>());
        this.safetyBuffer = 30; // default 30 minutes
        this.operatingHoursStart = LocalTime.of(6, 0);
        this.operatingHoursEnd = LocalTime.of(23, 0);
    }

    // Policy: Configure runways
    public void addRunway(int runwayNumber, int length) {
        Runway runway = new Runway(runwayNumber, length);
        airport.getRunways().add(runway);
        System.out.println("Runway " + runwayNumber + " added with length " + length);
    }

    // Policy: Configure gates
    public void addGate(int gateNumber, int capacity, LocalTime openTime, LocalTime closeTime) {
        Gate gate = new Gate(gateNumber, capacity, openTime, closeTime);
        airport.getGates().add(gate);
        System.out.println("Gate " + gateNumber + " added with capacity " + capacity);
    }

    // Policy: Set safety buffer between flights
    public void setSafetyBuffer(int bufferMinutes) {
        this.safetyBuffer = bufferMinutes;
        System.out.println("Safety buffer set to " + bufferMinutes + " minutes");
    }

    // Policy: Set operating hours
    public void setOperatingHours(LocalTime start, LocalTime end) {
        this.operatingHoursStart = start;
        this.operatingHoursEnd = end;
        System.out.println("Operating hours: " + start + " to " + end);
    }

    // Monitoring: View airport configuration
    public void viewAirportConfig() {
        System.out.println("\n=== AIRPORT CONFIGURATION ===");
        System.out.println("Airport: " + airport.getName());
        System.out.println("Capacity: " + airport.getCapacity());
        System.out.println("Operating Hours: " + operatingHoursStart + " to " + operatingHoursEnd);
        System.out.println("Safety Buffer: " + safetyBuffer + " minutes");
        System.out.println("\nRunways (" + airport.getRunways().size() + "):");
        for (Runway r : airport.getRunways()) {
            System.out.println("  - Runway " + r.getRunway_number() + " (Length: " + r.getLength() + "m)");
        }
        System.out.println("\nGates (" + airport.getGates().size() + "):");
        for (Gate g : airport.getGates()) {
            System.out.println("  - Gate " + g.getGate_num() + " (Capacity: " + g.getCapacity() + ")");
        }
    }

    // Monitoring: Get available runways count
    public int getAvailableRunwaysCount() {
        return airport.getRunways().size();
    }

    // Monitoring: Get available gates count
    public int getAvailableGatesCount() {
        return airport.getGates().size();
    }

    // Utility: Get airport object
    public Airport getAirport() {
        return airport;
    }

    // Utility: Get safety buffer
    public int getSafetyBuffer() {
        return safetyBuffer;
    }

    // Utility: Get operating hours
    public LocalTime getOperatingHoursStart() {
        return operatingHoursStart;
    }

    public LocalTime getOperatingHoursEnd() {
        return operatingHoursEnd;
    }
}
