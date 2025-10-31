package uspace.application.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.*;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.exceptions.InvalidDateFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CruiseServiceBookZeroGTest {

    private static final String CID = "C1";
    private static final String BID = "B1";
    private static final String TID = "T1";
    private static final String ANY_ISO = "2084-04-08T12:30:00";

    @Mock private CruiseRepository cruiseRepositoryMock;
    @Mock private CruiseAssembler cruiseAssemblerMock;

    private CruiseService service;

    @BeforeEach
    public void createService() {
        service = new CruiseService(cruiseRepositoryMock, cruiseAssemblerMock, /*planetValidator*/ null);
    }

    @Test
    public void givenCruiseNotFound_whenBookZeroG_thenThrowCruiseNotFound() {
        when(cruiseRepositoryMock.findById(new CruiseId(CID))).thenReturn(null);

        Executable call = () -> service.bookZeroGravityExperience(CID, BID, TID, ANY_ISO);

        assertThrows(CruiseNotFoundException.class, call);
        verify(cruiseRepositoryMock).findById(new CruiseId(CID));
        verify(cruiseRepositoryMock, never()).save(any());
    }

    @Test
    public void givenBadDateFormat_whenBookZeroG_thenThrowInvalidDateFormat() {
        when(cruiseRepositoryMock.findById(new CruiseId(CID))).thenReturn(mock(Cruise.class));

        Executable call = () -> service.bookZeroGravityExperience(CID, BID, TID, "BAD_FORMAT");

        assertThrows(InvalidDateFormatException.class, call);
        verify(cruiseRepositoryMock, never()).save(any());
    }

    @Test
    public void givenValidInput_whenBookZeroG_thenDelegateToCruiseAndSave() {
        Cruise cruiseMock = mock(Cruise.class);
        when(cruiseRepositoryMock.findById(new CruiseId(CID))).thenReturn(cruiseMock);

        assertDoesNotThrow(() -> service.bookZeroGravityExperience(CID, BID, TID, ANY_ISO));

        verify(cruiseMock).bookZeroGravityExperience(eq(new BookingId(BID)), eq(new TravelerId(TID)), any(CruiseDateTime.class));
        verify(cruiseRepositoryMock).save(cruiseMock);
    }
}
