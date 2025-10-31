package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.InvalidPlanetDateException;

@Provider
public class InvalidPlanetDateExceptionMapper implements ExceptionMapper<InvalidPlanetDateException> {
    @Override public Response toResponse(InvalidPlanetDateException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_PLANET_DATE", ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
