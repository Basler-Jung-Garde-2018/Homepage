package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.MemberService;
import ch.junggarde.api.application.dto.AdministrativeMemberDTO;
import ch.junggarde.api.application.dto.MemberDTO;
import ch.junggarde.api.model.member.MemberNotFound;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Path("/members")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Slf4j
public class MemberResource {
    @Inject
    MemberService memberService;

    @GET
    public Response getMembers() {
        log.info("HTTP GET /members");
        return Response.ok(memberService.getMembers()).build();
    }

    @POST
    public Response addMembers(List<MemberDTO> members) {
        log.info("add {} Members", members.size());
        return Response.ok().entity(memberService.addMembers(members)).build();
    }

    @Path("/administrative")
    @GET
    public Response getAdministrative() {
        log.info("HTTP GET /members/administrative");
        try {
            return Response.ok().entity(memberService.getAdministrativeMembers()).build();
        } catch (MemberNotFound e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("/administrative")
    @POST
    public Response addAdministrativeMembers(List<AdministrativeMemberDTO> administrativeMemberDTOS) {
        log.info("add {} AdministrativeMembers", administrativeMemberDTOS.size());
        memberService.addAdministrativeMembers(administrativeMemberDTOS);
        return Response.ok().build();
    }

}
