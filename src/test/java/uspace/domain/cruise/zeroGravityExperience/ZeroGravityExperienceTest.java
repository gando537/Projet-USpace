package uspace.domain.cruise.zeroGravityExperience;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceAlreadyBookedException;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceFullException;

import static org.junit.jupiter.api.Assertions.*;

class ZeroGravityExperienceTest {

    private static final int CAPACITY_TWO = 2;
    private static final TravelerId T1 = new TravelerId();
    private static final TravelerId T2 = new TravelerId();
    private static final TravelerId T3 = new TravelerId();

    private ZeroGravityExperience zeroG;

    @BeforeEach
    public void createZeroG() {
        zeroG = new ZeroGravityExperience(CAPACITY_TWO);
    }

    @Test
    public void givenSlotsRemaining_whenBook_thenSuccess() {
        assertDoesNotThrow(() -> zeroG.book(T1));
        assertTrue(zeroG.hasTravelerBooked(T1));
    }

    @Test
    public void givenSameTravelerTwice_whenBook_thenThrowAlreadyBooked() {
        zeroG.book(T1);

        Executable bookTwice = () -> zeroG.book(T1);

        assertThrows(ZeroGravityExperienceAlreadyBookedException.class, bookTwice);
    }

    @Test
    public void givenCapacityReached_whenBook_thenThrowFull() {
        zeroG.book(T1);
        zeroG.book(T2);

        Executable bookThird = () -> zeroG.book(T3);

        assertThrows(ZeroGravityExperienceFullException.class, bookThird);
    }

    @Test
    public void givenTravelerNotBooked_whenHasTravelerBooked_thenFalse() {
        assertFalse(zeroG.hasTravelerBooked(T1));
    }
}
