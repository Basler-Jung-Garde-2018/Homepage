package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.member.Member;

public record MemberDTO(
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
