package uspace.application.cruise;

import jakarta.inject.Inject;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.dtos.newPlanet.NewPlanetDto;
import uspace.application.cruise.planetvalidation.InvalidPlanetException;
import uspace.application.cruise.planetvalidation.PlanetValidator;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;
import uspace.domain.exceptions.InvalidDateFormatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CruiseService {

    private final CruiseRepository cruiseRepository;
    private final CruiseAssembler cruiseAssembler;
    private final PlanetValidator planetValidator;

    @Inject
    public CruiseService(CruiseRepository cruiseRepository, CruiseAssembler cruiseAssembler, PlanetValidator planetValidator) {
        this.cruiseRepository = cruiseRepository;
        this.cruiseAssembler = cruiseAssembler;
        this.planetValidator = planetValidator;
    }

    public CruiseDto findCruise(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        return cruiseAssembler.toDto(cruise);
    }

    public void addPlanetToItinerary(String cruiseId, NewPlanetDto dto) {
        // 1) Validation via système externe
        if (!planetValidator.isValid(dto.name)) {
            throw new InvalidPlanetException();
        }

        // 2) Charger la croisière
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) throw new CruiseNotFoundException();

        // 3) Parser les dates (format ISO), la validation de format a déjà été faite par le validator DTO
        LocalDateTime arrival = LocalDateTime.parse(dto.arrivalDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime departure = LocalDateTime.parse(dto.departureDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 4) Construire le domaine et déléguer
        Planet planet = new Planet(
                new PlanetName(dto.name),
                new CruiseDateTime(arrival),
                new CruiseDateTime(departure)
        );

        cruise.addPlanetToItinerary(planet);

        // 5) Persister
        cruiseRepository.save(cruise);
    }

    public void bookZeroGravityExperience(String cruiseId, String bookingId, String travelerId, String experienceBookingDateTimeStr) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) throw new CruiseNotFoundException();

        // parse ISO, sinon InvalidDateFormatException
        final LocalDateTime ldt;
        try {
            ldt = LocalDateTime.parse(experienceBookingDateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }

        CruiseDateTime cdt = new CruiseDateTime(ldt);

        cruise.bookZeroGravityExperience(new BookingId(bookingId), new TravelerId(travelerId), cdt);
        cruiseRepository.save(cruise);
    }

}
