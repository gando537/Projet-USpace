package uspace.application.cruise.dtos;

public class ItineraryPlanetDto {

    public String name;
    public String arrivalDateTime;
    public String departureDateTime;

    public ItineraryPlanetDto(String name, String arrivalDateTime, String departureDateTime) {
        this.name = name;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }
}
