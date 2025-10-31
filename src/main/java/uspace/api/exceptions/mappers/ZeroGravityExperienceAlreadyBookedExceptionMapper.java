package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceAlreadyBookedException;

@Provider
public class ZeroGravityExperienceAlreadyBookedExceptionMapper implements ExceptionMapper<ZeroGravityExperienceAlreadyBookedException> {
    @Override
    public Response toResponse(ZeroGravityExperienceAlreadyBookedException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ZERO_GRAVITY_EXPERIENCE_ALREADY_BOOKED",
                        "Zero gravity experience already booked by traveler"))
                .build();
    }
}
