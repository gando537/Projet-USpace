package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import uspace.domain.cruise.booking.traveler.*;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceChildCriteriaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingZeroGContextTest {

    private static final String ADULT_NAME  = "Alice";
    private static final String CHILD_NAME  = "Ben";
    private static final String SENIOR_NAME = "Charles";

    @Mock private ZeroGravityExperience experienceMock;

    private TravelerFactory travelerFactory;
    private Traveler adult;
    private Traveler child;
    private Traveler senior;
    private Booking booking;

    @BeforeEach
    public void createBookingAndTravelers() {
        travelerFactory = new TravelerFactory();

        adult  = travelerFactory.create(ADULT_NAME,  TravelerCategory.ADULT);
        child  = travelerFactory.create(CHILD_NAME,  TravelerCategory.CHILD);
        senior = travelerFactory.create(SENIOR_NAME, TravelerCategory.SENIOR);

        List<Traveler> travelers = new ArrayList<>();
        travelers.add(adult);
        travelers.add(child);
        travelers.add(senior);

        // BookingId/CabinType/CruiseDateTime ne sont pas utilisés par ces tests => valeurs quelconques.
        BookingId anyBookingId = mock(BookingId.class);
        CabinType anyCabinType = null;
        CruiseDateTime anyDate = new CruiseDateTime(LocalDateTime.parse("2084-01-01T00:00"));

        booking = new Booking(anyBookingId, travelers, anyCabinType, anyDate);
    }

    @Test
    public void givenChildBeforeAnyAdultOrSenior_whenBookZeroG_thenThrowCriteriaException() {
        Executable reserve = () -> booking.bookZeroGravityExperience(child.getId(), experienceMock);

        assertThrows(ZeroGravityExperienceChildCriteriaException.class, reserve);
        verify(experienceMock, never()).book(any());
        assertFalse(child.getBadges().contains(Badge.MINI_ZERO_G));
    }

    @Test
    public void givenAdultReserved_whenChildBooksSameExperience_thenChildAllowedAndGetsMiniZeroG() {
        booking.bookZeroGravityExperience(adult.getId(), experienceMock); // adulte d'abord

        assertDoesNotThrow(() -> booking.bookZeroGravityExperience(child.getId(), experienceMock));

        verify(experienceMock).book(adult.getId());
        verify(experienceMock).book(child.getId());
        assertTrue(child.getBadges().contains(Badge.MINI_ZERO_G));
    }

    @Test
    public void givenSeniorReserved_whenChildBooksSameExperience_thenChildAllowed() {
        booking.bookZeroGravityExperience(senior.getId(), experienceMock);

        assertDoesNotThrow(() -> booking.bookZeroGravityExperience(child.getId(), experienceMock));

        verify(experienceMock).book(senior.getId());
        verify(experienceMock).book(child.getId());
        assertTrue(child.getBadges().contains(Badge.MINI_ZERO_G));
    }
}
