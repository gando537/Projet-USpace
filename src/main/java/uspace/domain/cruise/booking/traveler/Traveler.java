package uspace.domain.cruise.booking.traveler;

import uspace.domain.cruise.booking.ZeroGBookingContext;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Traveler {
    private final TravelerId id;
    private final TravelerName name;
    private final TravelerCategory category;
    private final List<Badge> badges;

    private final Set<ZeroGravityExperience> reservedExperiences = new HashSet<>();


    public Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.badges = badges;
    }

    public TravelerId getId() { return id; }
    public TravelerName getName() { return name; }
    public TravelerCategory getCategory() { return category; }
    public List<Badge> getBadges() { return badges; }

    // === Méthode historique (reste dispo) ===
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience)
    {
        // Adult par défaut
        bookZeroGravityExperience(zeroGravityExperience, ZeroGBookingContext.ALWAYS_TRUE);
    }

    // === NOUVEL OVERLOAD : prend un contexte ===
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                          ZeroGBookingContext ctx)
    {
        zeroGravityExperience.book(id);
        markReserved(zeroGravityExperience);
        earnBadge(Badge.ZERO_G); // comportement ADULT par défaut
    }

    protected void markReserved(ZeroGravityExperience exp) {
        reservedExperiences.add(exp);
    }

    public boolean hasReservedZeroG(ZeroGravityExperience exp) {
        return reservedExperiences.contains(exp);
    }


    // --- changement: protected pour que les sous-classes attribuent leurs badges ---
    protected void earnBadge(Badge badge) {
        if (!badges.contains(badge)) {
            badges.add(badge);
        }
    }
}
