

package uspace.application.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.dtos.newPlanet.NewPlanetDto;
import uspace.application.cruise.planetvalidation.InvalidPlanetException;
import uspace.application.cruise.planetvalidation.PlanetValidator;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.itinerary.planet.Planet;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CruiseServiceTest {

    private static final String ANY_CRUISE_ID = "C1";
    private static final String ANY_NAME = "Mars";
    private static final String ANY_ARRIVAL = "2084-04-10T00:00";
    private static final String ANY_DEPARTURE = "2084-04-13T00:00";

    @Mock
    private CruiseRepository cruiseRepositoryMock;

    @Mock
    private PlanetValidator planetValidatorMock;

    private CruiseService service;

    @BeforeEach
    public void createService() {
        service = new CruiseService(cruiseRepositoryMock, /*assembler*/ null, planetValidatorMock);
    }

    @Test
    public void givenExternalValidatorRejects_whenAddPlanet_thenThrowInvalidPlanetAndDoNotSave() {
        when(planetValidatorMock.isValid(ANY_NAME)).thenReturn(false);

        NewPlanetDto dto = dto(ANY_NAME, ANY_ARRIVAL, ANY_DEPARTURE);
        Executable add = () -> service.addPlanetToItinerary(ANY_CRUISE_ID, dto);

        assertThrows(InvalidPlanetException.class, add);
        verify(planetValidatorMock).isValid(ANY_NAME);
        verify(cruiseRepositoryMock, never()).findById(any());
        verify(cruiseRepositoryMock, never()).save(any());
    }

    @Test
    public void givenCruiseNotFound_whenAddPlanet_thenThrowCruiseNotFoundException() {
        when(planetValidatorMock.isValid(ANY_NAME)).thenReturn(true);
        when(cruiseRepositoryMock.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(null);

        NewPlanetDto dto = dto(ANY_NAME, ANY_ARRIVAL, ANY_DEPARTURE);
        Executable add = () -> service.addPlanetToItinerary(ANY_CRUISE_ID, dto);

        assertThrows(CruiseNotFoundException.class, add);
        verify(planetValidatorMock).isValid(ANY_NAME);
        verify(cruiseRepositoryMock).findById(new CruiseId(ANY_CRUISE_ID));
        verify(cruiseRepositoryMock, never()).save(any());
    }

    @Test
    public void givenValidInput_whenAddPlanet_thenDelegateToDomainAndSave() {
        when(planetValidatorMock.isValid(ANY_NAME)).thenReturn(true);
        TestCruise cruise = new TestCruise(ANY_CRUISE_ID);
        when(cruiseRepositoryMock.findById(new CruiseId(ANY_CRUISE_ID))).thenReturn(cruise);

        NewPlanetDto dto = dto(ANY_NAME, ANY_ARRIVAL, ANY_DEPARTURE);

        assertDoesNotThrow(() -> service.addPlanetToItinerary(ANY_CRUISE_ID, dto));

        assertTrue(cruise.addCalled, "Cruise.addPlanetToItinerary doit être appelé");
        assertNotNull(cruise.lastPlanet, "La planète doit être transmise au domaine");
        assertEquals(ANY_NAME, cruise.lastPlanet.getName().toString());
        verify(cruiseRepositoryMock).save(cruise);
    }

    // ---------------- helpers ----------------

    private NewPlanetDto dto(String name, String arr, String dep) {
        NewPlanetDto dto = new NewPlanetDto();
        dto.name = name;
        dto.arrivalDateTime = arr;
        dto.departureDateTime = dep;
        return dto;
    }

    /** Cruise spécialisé pour capturer l'appel domaine sans dépendances réelles. */
    static class TestCruise extends Cruise {
        boolean addCalled = false;
        Planet lastPlanet;

        TestCruise(String id) {
            super(
                    new CruiseId(id),
                    new CruiseDateTime(LocalDateTime.parse("2084-04-01T00:00")),
                    new CruiseDateTime(LocalDateTime.parse("2084-05-01T00:00")),
                    /* cabinAvailabilities */ null,
                    /* bookings */ null,
                    /* zeroGravityExperience */ null,
                    /* itinerary */ new Itinerary(new ArrayList<>())
            );
        }

        @Override
        public void addPlanetToItinerary(Planet planet) {
            addCalled = true;
            lastPlanet = planet;
            // Pas de logique : on isole l'application du domaine ici
        }
    }
}
