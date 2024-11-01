package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.member.AdministrativeMember;
import ch.junggarde.api.model.member.Member;
import jakarta.annotation.Nullable;

public record AdministrativeMemberDTO(
        @Nullable
        String id,
        String firstname,
        String lastname,
        String role,
        String jobTitle,
        String description,
        @Nullable
        String supervisorId,
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
                image.getBase64()
        );
    }
}
