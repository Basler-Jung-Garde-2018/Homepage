package ch.junggarde.api.adapter.in;


import ch.junggarde.api.application.AppointmentService;
import ch.junggarde.api.application.dto.AppointmentDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
        return Response.ok().entity(appointMentService.getAppointments()).build();
    }

    @POST
    public Response saveAppointments(List<AppointmentDTO> appointments) {
        log.info("HTTP POST /appointments {}", appointments);
        return Response.ok().entity(appointMentService.saveAppointments(appointments)).build();
    }
}
