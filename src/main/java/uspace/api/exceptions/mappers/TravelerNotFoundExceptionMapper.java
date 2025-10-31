package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;

@Provider
public class TravelerNotFoundExceptionMapper implements ExceptionMapper<TravelerNotFoundException> {
    @Override public Response toResponse(TravelerNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("TRAVELER_NOT_FOUND", "Traveler not found"))
                .build();
    }
}
