package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

@Provider
public class ZeroGravityExperienceBookingTimeExceptionMapper implements ExceptionMapper<ZeroGravityExperienceBookingTimeException> {
    @Override
    public Response toResponse(ZeroGravityExperienceBookingTimeException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ZERO_GRAVITY_EXPERIENCE_BOOKING_TIME",
                        "Zero gravity experience booking time must be before the cruise departure time."))
                .build();
    }
}
