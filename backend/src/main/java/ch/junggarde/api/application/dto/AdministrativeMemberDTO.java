package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.member.AdministrativeMember;
import ch.junggarde.api.model.member.Member;

public record AdministrativeMemberDTO(
        String id,
        String firstname,
        String lastname,
        String role,
        String jobTitle,
        String description,
        String supervisorId,
        String imageFormat,
        String imageBase64
) {
    public static AdministrativeMemberDTO fromDomainModel(AdministrativeMember administrativeMember, Member member, Image image) {
        return new AdministrativeMemberDTO(
                administrativeMember.getId().toString(),
                member.getFirstname(),
                member.getLastname(),
                administrativeMember.getRole().toString(),
                administrativeMember.getJobTitle(),
                administrativeMember.getDescription(),
                administrativeMember.getSupervisorId().toString(),
                image.getFormat(),
                image.getBase64()
        );
    }
}
