package ch.junggarde.api.adapter.in;


import ch.junggarde.api.application.AppointmentService;
import ch.junggarde.api.application.dto.AppointmentDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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

    @POST
    public Response saveAppointments(List<AppointmentDTO> appointments) {
        return Response.ok().entity(appointMentService.saveAppointments(appointments)).build();
    }
}
