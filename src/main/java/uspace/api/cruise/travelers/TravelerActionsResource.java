package uspace.api.cruise.travelers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import uspace.api.cruise.zeroG.ZeroGBookingDto;
import uspace.api.cruise.zeroG.ZeroGBookingDtoValidator;
import uspace.application.cruise.CruiseService;

@Path("/cruises/{cruiseId}/bookings/{bookingId}/travelers/{travelerId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelerActionsResource {

    // On laisse JAX-RS injecter les path params sur des champs
    @PathParam("cruiseId")  private String cruiseId;
    @PathParam("bookingId") private String bookingId;
    @PathParam("travelerId") private String travelerId;

    private final CruiseService cruiseService;
    private final ZeroGBookingDtoValidator zeroGValidator;

    @Inject
    public TravelerActionsResource(CruiseService cruiseService, ZeroGBookingDtoValidator zeroGValidator) {
        this.cruiseService = cruiseService;
        this.zeroGValidator = zeroGValidator;
    }

    @POST
    @Path("/zeroGravityExperiences")
    public Response bookZeroGravity(ZeroGBookingDto dto) {
        zeroGValidator.validate(dto);
        cruiseService.bookZeroGravityExperience(cruiseId, bookingId, travelerId, dto.experienceBookingDateTime);
        return Response.ok().build();
    }
}
