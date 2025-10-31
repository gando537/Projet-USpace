package uspace.domain.cruise.booking;

import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.exceptions.InvalidBookingDateException;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.NoTravelerToBookException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.*;

public class Booking implements ZeroGBookingContext {
    private final BookingId id;
    private final List<Traveler> travelers;
    private final CabinType cabinType;
    private final CruiseDateTime bookingDateTime;


    public Booking(BookingId id, List<Traveler> travelers, CabinType cabinType, CruiseDateTime bookingDateTime) {
        this.id = id;
        this.travelers = travelers;
        this.cabinType = cabinType;
        this.bookingDateTime = bookingDateTime;
    }

    public BookingId getId() {
        return id;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public CruiseDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void validate(CruiseDateTime cruiseDepartureDateTime) {
        if (travelers.isEmpty()) {
            throw new NoTravelerToBookException();
        }

        if (!bookingDateIsBeforeCruiseDeparture(cruiseDepartureDateTime)) {
            throw new InvalidBookingDateException();
        }
    }

    public int getTravelersNumber() {
        return travelers.size();
    }

    public void bookZeroGravityExperience(TravelerId travelerId, ZeroGravityExperience zeroGravityExperience) {
        Traveler traveler = findTravelerById(travelerId);
        if (traveler == null) {
            throw new TravelerNotFoundException();
        }

        traveler.bookZeroGravityExperience(zeroGravityExperience, this);
    }

    private boolean bookingDateIsBeforeCruiseDeparture(CruiseDateTime cruiseDepartureDateTime) {
        return bookingDateTime.isBefore(cruiseDepartureDateTime);
    }

    private Traveler findTravelerById(TravelerId travelerId) {
        return travelers.stream()
                        .filter(traveler -> traveler.getId().equals(travelerId))
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public boolean hasAdultOrSeniorWhoReserved(ZeroGravityExperience exp) {
        for (Traveler t : travelers) {
            if (t.getCategory() == TravelerCategory.ADULT || t.getCategory() == TravelerCategory.SENIOR) {
                if (t.hasReservedZeroG(exp)) {
                    return true;
                }
            }
        }
        return false;
    }

}
