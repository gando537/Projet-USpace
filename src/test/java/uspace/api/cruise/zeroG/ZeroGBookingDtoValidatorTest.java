package uspace.api.cruise.zeroG;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;

class ZeroGBookingDtoValidatorTest {

    private static final String ANY_ISO = "2084-04-08T12:30:00";
    private static final String BAD = "2084/04/08 12:30";

    private ZeroGBookingDtoValidator validator;

    @BeforeEach
    public void createValidator() { validator = new ZeroGBookingDtoValidator(); }

    @Test
    public void givenNullBody_whenValidate_thenMissingBody() {
        Executable validate = () -> validator.validate(null);
        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    public void givenMissingDate_whenValidate_thenMissingParameter() {
        ZeroGBookingDto dto = new ZeroGBookingDto();
        Executable validate = () -> validator.validate(dto);
        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    public void givenBadFormat_whenValidate_thenInvalidDateFormat() {
        ZeroGBookingDto dto = new ZeroGBookingDto();
        dto.experienceBookingDateTime = BAD;

        Executable validate = () -> validator.validate(dto);
        assertThrows(InvalidDateFormatException.class, validate);
    }

    @Test
    public void givenIso_whenValidate_thenOk() {
        ZeroGBookingDto dto = new ZeroGBookingDto();
        dto.experienceBookingDateTime = ANY_ISO;

        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
