package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TravelerFactoryTest {

    private static final String ANY_NAME = "Bob";
    private TravelerFactory factory;

    @BeforeEach
    public void createFactory() {
        factory = new TravelerFactory();
    }

    @Test
    public void givenAdultCategory_whenCreate_thenReturnBaseTravelerWithAdultCategory() {
        Traveler traveler = factory.create(ANY_NAME, TravelerCategory.ADULT);

        assertEquals(TravelerCategory.ADULT, traveler.getCategory());
        assertEquals(Traveler.class, traveler.getClass()); // classe de base pour ADULT
        assertNotNull(traveler.getId());
        assertEquals(ANY_NAME, traveler.getName().toString());
        assertTrue(traveler.getBadges().isEmpty());
    }

    @Test
    public void givenChildCategory_whenCreate_thenReturnChildTraveler() {
        Traveler traveler = factory.create(ANY_NAME, TravelerCategory.CHILD);

        assertEquals(TravelerCategory.CHILD, traveler.getCategory());
        assertInstanceOf(ChildTraveler.class, traveler);
        assertNotNull(traveler.getId());
        assertEquals(ANY_NAME, traveler.getName().toString());
        assertTrue(traveler.getBadges().isEmpty());
    }

    @Test
    public void givenSeniorCategory_whenCreate_thenReturnSeniorTraveler() {
        Traveler traveler = factory.create(ANY_NAME, TravelerCategory.SENIOR);

        assertEquals(TravelerCategory.SENIOR, traveler.getCategory());
        assertInstanceOf(SeniorTraveler.class, traveler);
        assertNotNull(traveler.getId());
        assertEquals(ANY_NAME, traveler.getName().toString());
        assertTrue(traveler.getBadges().isEmpty());
    }
}
