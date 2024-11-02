package ch.junggarde.api.model.member;

import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class MemberNotFound extends RuntimeException {
    public MemberNotFound(UUID administrativeMemberId) {
        super("Member not found");
        log.error("Member of with administrative Id {} not found", administrativeMemberId);
    }
}
