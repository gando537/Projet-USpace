package uspace.domain.cruise.itinerary.planet;

import uspace.domain.cruise.dateTime.CruiseDateTime;

public class Planet {
    private final PlanetName name;
    private final CruiseDateTime arrivalDateTime;
    private final CruiseDateTime departureDateTime;

    public Planet(PlanetName name, CruiseDateTime arrivalDateTime, CruiseDateTime departureDateTime) {
        this.name = name;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }

    public PlanetName getName() {
        return name;
    }

    public CruiseDateTime getArrivalDate() {
        return arrivalDateTime;
    }

    public CruiseDateTime getDepartureDate() {
        return departureDateTime;
    }
}
