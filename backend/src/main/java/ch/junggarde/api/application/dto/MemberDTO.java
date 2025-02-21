package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.member.Member;
import jakarta.annotation.Nullable;

public record MemberDTO(
        @Nullable
        String id,
        String firstname,
        String lastname,
        String function
) {
    public static MemberDTO fromDomainModel(final Member member) {
        return new MemberDTO(
                member.getId().toString(),
                member.getFirstname(),
                member.getLastname(),
                member.getFunction().toString()
        );

    }
}
