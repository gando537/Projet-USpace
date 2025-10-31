package uspace.domain.cruise.itinerary.planet;

public class PlanetName {

    private final String name;

    public PlanetName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        PlanetName other = (PlanetName) obj;
        return this.name.equals(other.name);
    }
}
