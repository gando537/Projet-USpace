package uspace.domain.cruise.booking;

import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

public interface ZeroGBookingContext {
    /** Retourne true si un ADULT ou un SENIOR de la même réservation a déjà réservé exp. */
    boolean hasAdultOrSeniorWhoReserved(ZeroGravityExperience exp);

    // Contexte par défaut : toujours vrai (utile pour compat)
    ZeroGBookingContext ALWAYS_TRUE = exp -> true;
}
