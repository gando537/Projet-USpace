package uspace.domain.cruise.itinerary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;
import uspace.domain.cruise.itinerary.planet.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.planet.PlanetAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItineraryTest {

    private static final CruiseDateTime CRUISE_DEPARTURE = dt("2084-04-01T00:00");
    private static final CruiseDateTime CRUISE_END       = dt("2084-05-01T00:00");

    private Itinerary itinerary;

    @BeforeEach
    public void createItinerary() {
        itinerary = new Itinerary(new ArrayList<>());
    }

    @Test
    public void givenSamePlanetName_whenAdd_thenThrowPlanetAlreadyExistsException() {
        itinerary.getPlanets().add(new Planet(new PlanetName("Mars"),
                dt("2084-04-10T00:00"), dt("2084-04-15T00:00")));

        Planet duplicate = new Planet(new PlanetName("Mars"),
                dt("2084-04-20T00:00"), dt("2084-04-25T00:00"));

        Executable add = () -> itinerary.addPlanet(duplicate, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(PlanetAlreadyExistsException.class, add);
    }

    @Test
    public void givenStayLessThanThreeDays_whenAdd_thenThrowInvalidPlanetDateException() {
        Planet tooShort = new Planet(new PlanetName("Jupiter"),
                dt("2084-04-10T10:00"), dt("2084-04-12T09:59"));

        Executable add = () -> itinerary.addPlanet(tooShort, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(InvalidPlanetDateException.class, add);
    }

    @Test
    public void givenArrivalEqualsCruiseDeparture_whenAdd_thenThrowInvalidPlanetDateException() {
        Planet edge = new Planet(new PlanetName("Saturne"),
                dt("2084-04-01T00:00"), dt("2084-04-05T00:00")); // arrivée == départ croisière

        Executable add = () -> itinerary.addPlanet(edge, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(InvalidPlanetDateException.class, add);
    }

    @Test
    public void givenDepartureEqualsCruiseEnd_whenAdd_thenThrowInvalidPlanetDateException() {
        Planet edge = new Planet(new PlanetName("Neptune"),
                dt("2084-04-20T00:00"), dt("2084-05-01T00:00")); // départ == fin croisière

        Executable add = () -> itinerary.addPlanet(edge, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(InvalidPlanetDateException.class, add);
    }

    @Test
    public void givenPreviousPlanet_whenNewDepartureNotAfterPreviousArrival_thenThrowInvalidPlanetDateException() {
        // planète précédente existante : arrivée 10 -> départ 15 (les règles ne demandent que l'arrivée précédente)
        itinerary.getPlanets().add(new Planet(new PlanetName("Terre"),
                dt("2084-04-10T00:00"), dt("2084-04-15T00:00")));

        // nouvelle : départ 10 => pas "après" arrivée précédente (10)
        Planet invalid = new Planet(new PlanetName("Uranus"),
                dt("2084-04-08T00:00"), dt("2084-04-10T00:00"));

        Executable add = () -> itinerary.addPlanet(invalid, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(InvalidPlanetDateException.class, add);
    }

    @Test
    public void givenNextPlanet_whenNewArrivalNotBeforeNextDeparture_thenThrowInvalidPlanetDateException() {
        // planète suivante : 20 -> 22
        itinerary.getPlanets().add(new Planet(new PlanetName("Mercure"),
                dt("2084-04-20T00:00"), dt("2084-04-22T00:00")));

        // nouvelle : arrivée 22 => pas "avant" départ suivante (22)
        Planet invalid = new Planet(new PlanetName("Venus"),
                dt("2084-04-22T00:00"), dt("2084-04-26T00:00"));

        Executable add = () -> itinerary.addPlanet(invalid, CRUISE_DEPARTURE, CRUISE_END);

        assertThrows(InvalidPlanetDateException.class, add);
    }

    @Test
    public void givenTwoPlanets_whenAddBetweenRespectingAllRules_thenInsertAndKeepOrder() {
        itinerary.getPlanets().add(new Planet(new PlanetName("Terre"),
                dt("2084-04-05T00:00"), dt("2084-04-08T00:00")));
        itinerary.getPlanets().add(new Planet(new PlanetName("Jupiter"),
                dt("2084-04-15T00:00"), dt("2084-04-20T00:00")));

        Planet ok = new Planet(new PlanetName("Mars"),
                dt("2084-04-10T00:00"), dt("2084-04-13T00:00")); // 3 jours exacts

        assertDoesNotThrow(() -> itinerary.addPlanet(ok, CRUISE_DEPARTURE, CRUISE_END));

        List<Planet> ordered = itinerary.getPlanets();
        assertEquals(3, ordered.size());
        assertEquals("Terre",   ordered.get(0).getName().toString());
        assertEquals("Mars",    ordered.get(1).getName().toString());
        assertEquals("Jupiter", ordered.get(2).getName().toString());
    }

    private static CruiseDateTime dt(String iso) {
        return new CruiseDateTime(LocalDateTime.parse(iso));
    }
}
