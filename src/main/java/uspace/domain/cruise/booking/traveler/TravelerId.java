package uspace.domain.cruise.booking.traveler;

import java.util.UUID;

public class TravelerId {

    private final String id;

    public TravelerId() {
        this.id = UUID.randomUUID().toString();
    }

    public TravelerId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        TravelerId other = (TravelerId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
