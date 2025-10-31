package uspace.api.cruise;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.api.cruise.planets.NewPlanetDtoValidator;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.dtos.newPlanet.NewPlanetDto;

@Path("/cruises")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CruiseResource {

    private final CruiseService cruiseService;
    private final NewPlanetDtoValidator validator = new NewPlanetDtoValidator();

    @Inject
    public CruiseResource(CruiseService cruiseService) {
        this.cruiseService = cruiseService;
    }

    @GET
    @Path("{cruiseId}")
    public Response getCruise(@PathParam("cruiseId") String cruiseId) {
        CruiseDto cruiseDto = cruiseService.findCruise(cruiseId);
        return Response.ok(cruiseDto).build();
    }

    @POST
    @Path("/{cruiseId}/planets")
    public Response addPlanet(@PathParam("cruiseId") String cruiseId, NewPlanetDto dto) {
        validator.validate(dto);
        cruiseService.addPlanetToItinerary(cruiseId, dto);
        // 201 sans body (spécification). On pourrait mettre Location vers /cruises/{id}
        return Response.status(Response.Status.CREATED).build();
    }

}
