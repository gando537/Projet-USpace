package uspace.application.cruise;

import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.dtos.ItineraryPlanetDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.itinerary.planet.Planet;

import java.util.List;

public class CruiseAssembler {
    public CruiseDto toDto(Cruise cruise) {
        List<ItineraryPlanetDto> itinerary = createItineraryPlanetDtos(cruise.getPlanetsInItinerary());

        return new CruiseDto(
            cruise.getId().toString(),
            cruise.getDepartureDateTime().toString(),
            cruise.getEndDateTime().toString(),
            itinerary
        );
    }

    private List<ItineraryPlanetDto> createItineraryPlanetDtos(List<Planet> planetsInItinerary) {
        return planetsInItinerary.stream()
            .map(planet -> new ItineraryPlanetDto(
                planet.getName().toString(),
                planet.getArrivalDate().toString(),
                planet.getDepartureDate().toString()
            ))
            .toList();
    }

}
