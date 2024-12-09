package ch.junggarde.api.model.member;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class MemberNotFound extends RuntimeException {
    public MemberNotFound(UUID administrativeMemberId) {
        super("Member not found");
        log.error("Member of with administrative Id {} not found", administrativeMemberId);
    }
}
