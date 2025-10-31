package uspace.domain.cruise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Bookings;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CruiseTest {
    @Mock
    private Itinerary itineraryMock;
    @Mock
    private CabinAvailabilities cabinAvailabilitiesMock;
    @Mock
    private Bookings bookingsMock;
    @Mock
    private ZeroGravityExperience zeroGravityExperienceMock;
    @Mock
    private Planet planetMock;

    ///ATTENTION! Ce test sert à vous montrer la configuration avec Junit et Mockito.
    // Il ne respecte peut-être pas les bonnes pratiques vues en classe.
    @Test
    void testGetPlanetsInItinerary() {
        when(itineraryMock.getPlanets()).thenReturn(List.of(planetMock));
        Cruise cruise = new Cruise(new CruiseId("AnId"),
                                   new CruiseDateTime(LocalDateTime.now()),
                                   new CruiseDateTime(LocalDateTime.now()),
                                   cabinAvailabilitiesMock,
                                   bookingsMock,
                                   zeroGravityExperienceMock,
                                   itineraryMock);
        assertEquals(planetMock, cruise.getPlanetsInItinerary().get(0));
    }
}