
package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import uspace.domain.cruise.booking.ZeroGBookingContext;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceChildCriteriaException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelerTest {

    private static final TravelerId ANY_ID = new TravelerId("T-1");
    private static final TravelerName ANY_NAME = new TravelerName("Bob");

    @Mock private ZeroGravityExperience expMock;
    @Mock private ZeroGBookingContext ctxMock;

    private Traveler adult;  // Traveler de base = ADULT
    private Traveler child;
    private Traveler senior;

    @BeforeEach
    public void createTravelers() {
        adult  = new Traveler(ANY_ID, ANY_NAME, TravelerCategory.ADULT,  new ArrayList<>());
        child  = new ChildTraveler(ANY_ID, ANY_NAME, new ArrayList<>());
        senior = new SeniorTraveler(ANY_ID, ANY_NAME, new ArrayList<>());
    }

    @Test
    public void givenAdult_whenReserve_thenGetsZERO_G() {
        adult.bookZeroGravityExperience(expMock, ctxMock);

        assertTrue(adult.getBadges().contains(Badge.ZERO_G));
        verify(expMock).book(ANY_ID);
    }

    @Test
    public void givenSenior_whenReserve_thenGetsZERO_G_and_STILL_GOT_IT() {
        senior.bookZeroGravityExperience(expMock, ctxMock);

        assertTrue(senior.getBadges().contains(Badge.ZERO_G));
        assertTrue(senior.getBadges().contains(Badge.STILL_GOT_IT));
        verify(expMock).book(ANY_ID);
    }

    @Test
    public void givenChild_withoutAdultOrSeniorPreReserved_whenReserve_thenThrowCriteriaException() {
        when(ctxMock.hasAdultOrSeniorWhoReserved(expMock)).thenReturn(false);

        Executable reserve = () -> child.bookZeroGravityExperience(expMock, ctxMock);

        assertThrows(ZeroGravityExperienceChildCriteriaException.class, reserve);
        verify(expMock, never()).book(any());
    }

    @Test
    public void givenChild_withAdultOrSeniorPreReserved_whenReserve_thenGetsMINI_ZERO_G() {
        when(ctxMock.hasAdultOrSeniorWhoReserved(expMock)).thenReturn(true);

        child.bookZeroGravityExperience(expMock, ctxMock);

        assertTrue(child.getBadges().contains(Badge.MINI_ZERO_G));
        verify(expMock).book(ANY_ID);
    }
}
