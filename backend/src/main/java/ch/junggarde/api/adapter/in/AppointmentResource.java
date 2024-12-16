package ch.junggarde.api.adapter.in;


import ch.junggarde.api.application.AppointmentService;
import ch.junggarde.api.application.dto.AppointmentRequestDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AppointmentResource {
    @Inject
    AppointmentService appointMentService;

    @GET
    public Response getAppointments() {
        log.info("HTTP GET /appointments");
        return Response.ok().entity(appointMentService.getPublicAppointments()).build();
    }

    @GET
    @Path("/private")
    public Response getPrivateAppointments() {
        log.info("HTTP GET /appointments/private");
        return Response.ok().entity(appointMentService.getAppointments()).build();
    }

    @POST
    @Path("/private")
    public Response saveAppointments(AppointmentRequestDTO appointment) {
        log.info("HTTP POST /appointments {}", appointment);
        return Response.ok().entity(appointMentService.saveAppointment(appointment)).build();
    }
}
