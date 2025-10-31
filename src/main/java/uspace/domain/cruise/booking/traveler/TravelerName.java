package uspace.domain.cruise.booking.traveler;

public class TravelerName {
    private final String name;

    public TravelerName(String name) {
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
        TravelerName other = (TravelerName) obj;
        return this.name.equals(other.name);
    }
}
