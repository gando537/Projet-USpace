package uspace.domain.cruise.booking;

import java.util.HashMap;

public class Bookings {
    private final HashMap<BookingId, Booking> bookings;

    public Bookings() {
        bookings = new HashMap<>();
    }

    public void add(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    public Booking findById(BookingId bookingId) {
        return bookings.get(bookingId);
    }
}
