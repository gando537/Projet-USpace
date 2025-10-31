package uspace.application.cruise.planetvalidation;

public class InvalidPlanetException extends RuntimeException {
    public InvalidPlanetException() { super("Invalid planet"); }
}
