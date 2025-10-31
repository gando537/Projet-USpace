package uspace.api.cruise.planets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.cruise.dtos.newPlanet.NewPlanetDto;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;

class NewPlanetDtoValidatorTest {

    private static final String ANY_NAME = "Mars";
    private static final String ANY_ISO_ARRIVAL = "2084-04-08T12:30";
    private static final String ANY_ISO_DEPARTURE = "2084-04-11T12:30";
    private static final String BAD_FORMAT_DATETIME = "2084/04/08 12:30";

    private NewPlanetDtoValidator validator;

    @BeforeEach
    public void createValidator() {
        validator = new NewPlanetDtoValidator();
    }

    @Test
    public void givenDtoWithoutName_whenValidate_thenThrowMissingParameter() {
        NewPlanetDto dto = new NewPlanetDto();
        dto.arrivalDateTime = ANY_ISO_ARRIVAL;
        dto.departureDateTime = ANY_ISO_DEPARTURE;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    public void givenDtoWithoutArrival_whenValidate_thenThrowMissingParameter() {
        NewPlanetDto dto = new NewPlanetDto();
        dto.name = ANY_NAME;
        dto.departureDateTime = ANY_ISO_DEPARTURE;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    public void givenDtoWithoutDeparture_whenValidate_thenThrowMissingParameter() {
        NewPlanetDto dto = new NewPlanetDto();
        dto.name = ANY_NAME;
        dto.arrivalDateTime = ANY_ISO_ARRIVAL;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    public void givenBadDateFormat_whenValidate_thenThrowInvalidDateFormat() {
        NewPlanetDto dto = new NewPlanetDto();
        dto.name = ANY_NAME;
        dto.arrivalDateTime = BAD_FORMAT_DATETIME; // mauvais format
        dto.departureDateTime = ANY_ISO_DEPARTURE;

        Executable validate = () -> validator.validate(dto);

        assertThrows(InvalidDateFormatException.class, validate);
    }

    @Test
    public void givenAllFieldsAndIsoFormat_whenValidate_thenOk() {
        NewPlanetDto dto = new NewPlanetDto();
        dto.name = ANY_NAME;
        dto.arrivalDateTime = ANY_ISO_ARRIVAL;
        dto.departureDateTime = ANY_ISO_DEPARTURE;

        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
