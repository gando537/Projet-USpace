package uspace.domain.cruise.itinerary.planet;

public class InvalidPlanetDateException extends RuntimeException {
    public InvalidPlanetDateException() { super("Arrival date or departure date is invalid"); }
}