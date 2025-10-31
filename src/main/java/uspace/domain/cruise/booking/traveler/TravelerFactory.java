package uspace.domain.cruise.booking.traveler;

import java.util.ArrayList;

public class TravelerFactory {
    public Traveler create(String travelerNameStr, TravelerCategory travelerCategory) {
        TravelerId travelerId = new TravelerId();
        TravelerName travelerName = new TravelerName(travelerNameStr);

        // Retourne la bonne sous-classe selon la catégorie
        return switch (travelerCategory) {
            case CHILD -> new ChildTraveler(travelerId, travelerName, new ArrayList<>());
            case SENIOR -> new SeniorTraveler(travelerId, travelerName, new ArrayList<>());
            default -> new Traveler(travelerId, travelerName, travelerCategory, new ArrayList<>());
        };
    }
}
