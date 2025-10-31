package uspace.application.cruise.dtos;

import java.util.List;

public class CruiseDto {
    public String id;
    public String departureDateTime;
    public String endDateTime;
    public List<ItineraryPlanetDto> planets;

    public CruiseDto(String id, String departureDateTime, String endDateTime, List<ItineraryPlanetDto> planets) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.endDateTime = endDateTime;
        this.planets = planets;
    }
}
