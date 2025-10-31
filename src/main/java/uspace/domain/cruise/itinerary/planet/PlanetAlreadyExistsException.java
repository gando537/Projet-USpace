package uspace.domain.cruise.itinerary.planet;

public class PlanetAlreadyExistsException extends RuntimeException {
    public PlanetAlreadyExistsException() { super("Planet already exists in the itinerary"); }
}