package uspace.domain.cruise.booking;

import java.util.UUID;

public class BookingId {
    private final String id;

    public BookingId() {
        this.id = UUID.randomUUID().toString();
    }

    public BookingId(String id) {
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
        BookingId other = (BookingId) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
