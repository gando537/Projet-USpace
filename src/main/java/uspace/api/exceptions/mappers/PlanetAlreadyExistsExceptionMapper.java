package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.PlanetAlreadyExistsException;

@Provider
public class PlanetAlreadyExistsExceptionMapper implements ExceptionMapper<PlanetAlreadyExistsException> {
    @Override public Response toResponse(PlanetAlreadyExistsException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PLANET_ALREADY_EXISTS", ex.getMessage()))
                .build();
    }
}