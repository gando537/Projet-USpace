package uspace.api.cruise.planets;

import uspace.application.cruise.dtos.newPlanet.NewPlanetDto;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewPlanetDtoValidator {

    private void validateDateFormat(String dateTimeStr) {
        try {
            LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }
    }
    public void validate(NewPlanetDto dto) {
        if (dto == null) throw new MissingParameterException("body");
        if (isBlank(dto.name)) throw new MissingParameterException("name");
        if (isBlank(dto.arrivalDateTime)) throw new MissingParameterException("arrivalDateTime");
        if (isBlank(dto.departureDateTime)) throw new MissingParameterException("departureDateTime");

        // Valide juste le format ici (le sens métier sera validé en domaine)
        try {
            validateDateFormat(dto.arrivalDateTime);
            validateDateFormat(dto.departureDateTime);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }
    }

    private boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }


}
