package uspace.domain.cruise.booking.traveler;

import uspace.domain.cruise.booking.ZeroGBookingContext;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

public class SeniorTraveler extends Traveler {

    public SeniorTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.SENIOR, badges);
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience exp, ZeroGBookingContext ctx) {
        exp.book(getId());
        markReserved(exp);
        earnBadge(Badge.ZERO_G);
        earnBadge(Badge.STILL_GOT_IT);
    }
}
