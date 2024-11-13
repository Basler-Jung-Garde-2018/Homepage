package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.member.FullAdministrativeMember;
import jakarta.annotation.Nullable;

import java.util.List;

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
        @Nullable
        List<AdministrativeMemberDTO> subordinates,
        String imageBase64
) {
    public static AdministrativeMemberDTO fromDomainModel(FullAdministrativeMember administrativeMember) {
        return new AdministrativeMemberDTO(
                administrativeMember.getId().toString(),
                administrativeMember.getFirstname(),
                administrativeMember.getLastname(),
                administrativeMember.getRole().toString(),
                administrativeMember.getJobTitle(),
                administrativeMember.getDescription(),
                administrativeMember.getSupervisorId() != null ? administrativeMember.getSupervisorId().toString() : null,
                administrativeMember.getSubordinates().stream().map(AdministrativeMemberDTO::fromDomainModel).toList(),
                administrativeMember.getBase64()
        );
    }
}
