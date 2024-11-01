package ch.junggarde.api.adapter.in;


import ch.junggarde.api.application.AppointmentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AppointmentResource {
    @Inject
    AppointmentService appointMentService;

    @GET
    public Response getAppointments() {
        return Response.ok().entity(appointMentService.getAppointments()).build();
    }
}
