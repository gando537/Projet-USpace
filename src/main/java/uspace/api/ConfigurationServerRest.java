package uspace.api;

import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import planetValidatorVowelSystem.PlanetValidatorVowelSystemAdapter;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.zeroG.ZeroGBookingDtoValidator;
import uspace.application.cruise.planetvalidation.PlanetValidator;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.application.cruise.booking.BookingAssembler;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.CruiseAssembler;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.booking.BookingTravelerAssembler;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingFactory;
import uspace.domain.cruise.booking.Bookings;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.infra.persistence.inMemory.InMemoryCruiseRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationServerRest extends AbstractBinder {

    @Override
    protected void configure() {
        bindAsContract(TravelerFactory.class);
        bindAsContract(BookingFactory.class);

        bindAsContract(BookingTravelerAssembler.class);
        bindAsContract(BookingAssembler.class);
        bindAsContract(CruiseAssembler.class);

        bind(DateTimeFormatter.ISO_LOCAL_DATE_TIME).to(DateTimeFormatter.class);
        bindAsContract(LocalDateTimeParser.class);

        bind(InMemoryCruiseRepository.class).to(CruiseRepository.class);

        bindAsContract(NewBookingDtoValidator.class);
        bindAsContract(ZeroGBookingDtoValidator.class);

        bind(PlanetValidatorVowelSystemAdapter.class)
                .to(PlanetValidator.class)
                .in(Singleton.class);

        bindAsContract(BookingService.class);
        bindAsContract(CruiseService.class);

        seedDemoCruise42();
    }

    private void seedDemoCruise42() {
        CruiseId cruiseId = new CruiseId("42");
        CruiseDateTime departure = new CruiseDateTime(LocalDateTime.parse("2084-04-01T00:00:00"));
        CruiseDateTime end       = new CruiseDateTime(LocalDateTime.parse("2084-04-20T00:00:00"));

        // Itinerary vide + ZeroG capacité 10 (pour tester "full")
        Cruise demo = getCruise(cruiseId, departure, end);

        InMemoryCruiseRepository repo = new InMemoryCruiseRepository();
        repo.save(demo);
    }

    private static Cruise getCruise(CruiseId cruiseId, CruiseDateTime departure, CruiseDateTime end) {
        Itinerary itinerary = new Itinerary(new ArrayList<>());
        ZeroGravityExperience zeroG = new ZeroGravityExperience(10);

        // *** Ces deux-là NE DOIVENT PAS être null ***
        CabinAvailabilities cabinAvailabilities = new CabinAvailabilities(
                new HashMap<>(Map.of(
                        CabinType.STANDARD, 500,
                        CabinType.DELUXE,   100,
                        CabinType.SUITE,    10
                ))
        );
        Bookings bookings = new Bookings();

        return new Cruise(cruiseId, departure, end, cabinAvailabilities, bookings, zeroG, itinerary);
    }

}
