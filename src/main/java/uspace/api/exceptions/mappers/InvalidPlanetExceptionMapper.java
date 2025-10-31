package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.cruise.planetvalidation.InvalidPlanetException;

@Provider
public class InvalidPlanetExceptionMapper implements ExceptionMapper<InvalidPlanetException> {
    @Override public Response toResponse(InvalidPlanetException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("INVALID_PLANET", ex.getMessage()))
                .build();
    }
}