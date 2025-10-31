package uspace.domain.cruise.itinerary;

import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetAlreadyExistsException;

import java.util.List;

public class Itinerary {
    private final List<Planet> planets;

    public Itinerary(List<Planet> planets) {
        this.planets = planets;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void addPlanet(Planet newPlanet, CruiseDateTime cruiseDeparture, CruiseDateTime cruiseEnd) {
        // 1) Unicité
        for (Planet p : planets) {
            if (p.getName().equals(newPlanet.getName())) {
                throw new PlanetAlreadyExistsException();
            }
        }

        // 2) Bornes croisière (strictes)
        if (!newPlanet.getArrivalDate().isAfter(cruiseDeparture)) {
            throw new InvalidPlanetDateException();
        }
        if (!newPlanet.getDepartureDate().isBefore(cruiseEnd)) {
            throw new InvalidPlanetDateException();
        }

        // 3) Séjour >= 3 jours (arrival + 3j <= departure)
        if (newPlanet.getArrivalDate().plusDays(3).isAfter(newPlanet.getDepartureDate())) {
            throw new InvalidPlanetDateException();
        }

        // 4) AUCUN recouvrement ni contact avec une planète existante
        //    Validité requiert (strict) : new.departure < p.arrival  OU  new.arrival > p.departure
        for (Planet p : planets) {
            boolean strictlyBefore = newPlanet.getDepartureDate().isBefore(p.getArrivalDate());
            boolean strictlyAfter  = newPlanet.getArrivalDate().isAfter(p.getDepartureDate());
            if (!(strictlyBefore || strictlyAfter)) {
                // chevauchement ou contact (==) -> invalide
                throw new InvalidPlanetDateException();
            }
        }

        // 5) Insérer et trier par date d'arrivée (sans s'appuyer sur toString)
        planets.add(newPlanet);
        planets.sort((a, b) -> {
            if (a.getArrivalDate().equals(b.getArrivalDate())) return 0;
            return a.getArrivalDate().isBefore(b.getArrivalDate()) ? -1 : 1;
        });
    }
}
