package uspace.domain.cruise;

import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.Bookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.itinerary.planet.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetAlreadyExistsException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.util.List;

public class Cruise {
    private final CruiseId id;
    private final CruiseDateTime departureDateTime;
    private final CruiseDateTime endDateTime;
    private final CabinAvailabilities cabinAvailabilities;
    private final Bookings bookings;
    private final ZeroGravityExperience zeroGravityExperience;
    private final Itinerary itinerary;

    public Cruise(CruiseId id, CruiseDateTime departureDateTime, CruiseDateTime endDateTime, CabinAvailabilities cabinAvailabilities,
                  Bookings bookings, ZeroGravityExperience zeroGravityExperience, Itinerary itinerary) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.endDateTime = endDateTime;
        this.cabinAvailabilities = cabinAvailabilities;
        this.bookings = bookings;
        this.zeroGravityExperience = zeroGravityExperience;
        this.itinerary = itinerary;
    }

    public CruiseId getId() {
        return id;
    }

    public CruiseDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public CruiseDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<Planet> getPlanetsInItinerary() {
        return itinerary.getPlanets();
    }

    public void processBooking(Booking booking) {
        booking.validate(departureDateTime);
        cabinAvailabilities.bookCabin(booking.getCabinType());
        bookings.add(booking);
    }

    public Booking findBookingById(BookingId bookingId) {
        return bookings.findById(bookingId);
    }

    public void bookZeroGravityExperience(BookingId bookingId, TravelerId travelerId, CruiseDateTime experienceBookingDateTime) {
        if (experienceBookingDateTime.isAfter(departureDateTime)) {
            throw new ZeroGravityExperienceBookingTimeException();
        }

        Booking booking = bookings.findById(bookingId);
        if (booking == null) {
            throw new TravelerNotFoundException();
        }
        booking.bookZeroGravityExperience(travelerId, zeroGravityExperience);
    }

    public void addPlanetToItinerary(Planet planet) throws PlanetAlreadyExistsException, InvalidPlanetDateException {
        itinerary.addPlanet(planet, this.departureDateTime, this.endDateTime);
    }
}
