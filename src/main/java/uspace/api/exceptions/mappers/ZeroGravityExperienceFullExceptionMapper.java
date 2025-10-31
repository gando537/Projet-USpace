package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceFullException;

@Provider
public class ZeroGravityExperienceFullExceptionMapper implements ExceptionMapper<ZeroGravityExperienceFullException> {
    @Override
    public Response toResponse(ZeroGravityExperienceFullException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ZERO_GRAVITY_EXPERIENCE_FULL",
                        "Zero gravity experience is full"))
                .build();
    }
}
