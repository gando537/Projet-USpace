package uspace.api.cruise.zeroG;

import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ZeroGBookingDtoValidator {

    public void validate(ZeroGBookingDto dto) {
        if (dto == null) throw new MissingParameterException("body");
        if (isBlank(dto.experienceBookingDateTime)) throw new MissingParameterException("experienceBookingDateTime");

        // format ISO (accepte les secondes, ex: 2084-04-08T12:30:00)
        try {
            LocalDateTime.parse(dto.experienceBookingDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
