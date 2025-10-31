package uspace.api.cruise;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.cruise.travelers.TravelerActionsResource;
import uspace.api.cruise.zeroG.ZeroGBookingDto;
import uspace.api.cruise.zeroG.ZeroGBookingDtoValidator;
import uspace.application.cruise.CruiseService;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelerActionsResourceZeroGTest {

    private static final String CRUISE_ID   = "C1";
    private static final String BOOKING_ID  = "B1";
    private static final String TRAVELER_ID = "T1";
    private static final String ANY_ISO     = "2084-04-08T12:30:00";

    @Mock
    private CruiseService cruiseServiceMock;

    @Mock
    private ZeroGBookingDtoValidator zeroGValidatorMock;

    private TravelerActionsResource resource;

    @BeforeEach
    void setup() throws Exception {
        resource = new TravelerActionsResource(cruiseServiceMock, zeroGValidatorMock);
        // Simule l’injection @PathParam de JAX-RS
        setField(resource, "cruiseId", CRUISE_ID);
        setField(resource, "bookingId", BOOKING_ID);
        setField(resource, "travelerId", TRAVELER_ID);
    }

    @Test
    void givenValidDto_whenPostZeroG_thenReturn200AndCallService() {
        ZeroGBookingDto dto = new ZeroGBookingDto();
        dto.experienceBookingDateTime = ANY_ISO;

        doNothing().when(zeroGValidatorMock).validate(dto);

        Response resp = resource.bookZeroGravity(dto);

        assertEquals(200, resp.getStatus());
        verify(cruiseServiceMock).bookZeroGravityExperience(CRUISE_ID, BOOKING_ID, TRAVELER_ID, ANY_ISO);
        verifyNoMoreInteractions(cruiseServiceMock);
    }

    @Test
    void givenMissingDate_whenPostZeroG_thenThrowMissingParameterAndDontCallService() {
        ZeroGBookingDto dto = new ZeroGBookingDto(); // date absente

        doThrow(new MissingParameterException("experienceBookingDateTime"))
                .when(zeroGValidatorMock).validate(dto);

        Executable call = () -> resource.bookZeroGravity(dto);

        assertThrows(MissingParameterException.class, call);
        verifyNoInteractions(cruiseServiceMock);
    }

    @Test
    void givenBadDateFormat_whenPostZeroG_thenThrowInvalidDateFormatAndDontCallService() {
        ZeroGBookingDto dto = new ZeroGBookingDto();
        dto.experienceBookingDateTime = "2084/04/08 12:30"; // pas ISO

        doThrow(new InvalidDateFormatException())
                .when(zeroGValidatorMock).validate(dto);

        Executable call = () -> resource.bookZeroGravity(dto);

        assertThrows(InvalidDateFormatException.class, call);
        verifyNoInteractions(cruiseServiceMock);
    }

    // utilitaire pour setter les champs privés annotés @PathParam
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
