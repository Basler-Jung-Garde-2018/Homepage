package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.MemberService;
import ch.junggarde.api.model.member.MemberNotFound;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/members")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class MemberResource {
    @Inject
    MemberService memberService;

    @GET
    public Response getMembers() {
        return Response.ok(memberService.getMembers()).build();
    }

    @Path("/administrative")
    @GET
    public Response getAdministrative() {
        try {
            return Response.ok().entity(memberService.getAdministrativeMembers()).build();
        } catch (MemberNotFound e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
