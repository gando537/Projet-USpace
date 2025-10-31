package uspace.application.cruise.booking.dtos;

import java.util.List;

public class BookingDto {

    public String cruiseId;
    public String bookingId;
    public List<BookingTravelerDto> travelers;
    public String cabinType;
    public String bookingDateTime;

    public BookingDto(String cruiseId, String bookingId, List<BookingTravelerDto> travelers, String cabinType, String bookingDateTime) {
        this.cruiseId = cruiseId;
        this.bookingId = bookingId;
        this.travelers = travelers;
        this.cabinType = cabinType;
        this.bookingDateTime = bookingDateTime;
    }
}
