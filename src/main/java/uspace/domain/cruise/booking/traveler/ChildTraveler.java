package uspace.domain.cruise.booking.traveler;

import uspace.domain.cruise.booking.ZeroGBookingContext;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceChildCriteriaException;

import java.util.List;

public class ChildTraveler extends Traveler {

    public ChildTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.CHILD, badges);
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience exp, ZeroGBookingContext ctx) {
        if (!ctx.hasAdultOrSeniorWhoReserved(exp)) {
            throw new ZeroGravityExperienceChildCriteriaException();
        }
        // Réserve puis badge MINI_ZERO_G
        exp.book(getId());
        markReserved(exp);
        earnBadge(Badge.MINI_ZERO_G);
    }
}
