package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.Bookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CruiseBookZeroGTest {

    private static final CruiseId ANY_CRUISE_ID = new CruiseId("C1");
    private static final BookingId ANY_BOOKING_ID = mock(BookingId.class);
    private static final TravelerId ANY_TRAVELER_ID = new TravelerId();

    private static final CruiseDateTime DEPARTURE = new CruiseDateTime(LocalDateTime.parse("2084-04-10T10:00"));
    private static final CruiseDateTime END       = new CruiseDateTime(LocalDateTime.parse("2084-05-01T00:00"));
    private static final CruiseDateTime EXP_BEFORE_DEPARTURE = new CruiseDateTime(LocalDateTime.parse("2084-04-10T09:59"));
    private static final CruiseDateTime EXP_AFTER_DEPARTURE  = new CruiseDateTime(LocalDateTime.parse("2084-04-10T10:01"));

    @Mock private CabinAvailabilities cabinAvailabilitiesMock;
    @Mock private Bookings bookingsMock;
    @Mock private ZeroGravityExperience zeroGMock;
    @Mock private Itinerary itineraryMock;

    private Cruise cruise;

    @BeforeEach
    public void createCruise() {
        cruise = new Cruise(ANY_CRUISE_ID, DEPARTURE, END, cabinAvailabilitiesMock, bookingsMock, zeroGMock, itineraryMock);
    }

    @Test
    public void givenExperienceBookingDateAfterCruiseDeparture_whenBook_thenThrowZeroGravityExperienceBookingTimeException() {
        Executable book = () -> cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, EXP_AFTER_DEPARTURE);

        assertThrows(ZeroGravityExperienceBookingTimeException.class, book);
        verifyNoInteractions(bookingsMock);
    }

    @Test
    public void givenBookingNotFound_whenBook_thenThrowTravelerNotFoundException() {
        when(bookingsMock.findById(ANY_BOOKING_ID)).thenReturn(null);

        Executable book = () -> cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, EXP_BEFORE_DEPARTURE);

        assertThrows(TravelerNotFoundException.class, book);
        verify(bookingsMock).findById(ANY_BOOKING_ID);
        verifyNoInteractions(zeroGMock);
    }

    @Test
    public void givenValidInput_whenBook_thenDelegateToBookingWithTravelerAndZeroG() {
        Booking bookingMock = mock(Booking.class);
        when(bookingsMock.findById(ANY_BOOKING_ID)).thenReturn(bookingMock);

        assertDoesNotThrow(() -> cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, EXP_BEFORE_DEPARTURE));

        verify(bookingsMock).findById(ANY_BOOKING_ID);
        verify(bookingMock).bookZeroGravityExperience(ANY_TRAVELER_ID, zeroGMock);
        verifyNoMoreInteractions(bookingMock);
    }
}
