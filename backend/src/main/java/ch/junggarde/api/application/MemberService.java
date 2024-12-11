package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AdministrativeMemberRepository;
import ch.junggarde.api.adapter.out.persistance.MemberRepository;
import ch.junggarde.api.application.dto.AdministrativeMemberDTO;
import ch.junggarde.api.application.dto.MemberDTO;
import ch.junggarde.api.model.member.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@ApplicationScoped
@Slf4j
public class MemberService {
    @Inject
    MemberRepository memberRepository;

    @Inject
    AdministrativeMemberRepository administrativeMemberRepository;

    public List<MemberDTO> getMembers() {
        return memberRepository.findAll().stream().map(MemberDTO::fromDomainModel).toList();
    }

    public List<AdministrativeMemberDTO> getAdministrativeMembers() throws MemberNotFound {
        List<AdministrativeMember> administrativeMembers = administrativeMemberRepository.findAll();

        // Get all memberIds of the administrative members
        List<String> memberIds = administrativeMembers.stream()
                .map(administrativeMember -> administrativeMember.getMemberId().toString())
                .toList();

        List<Member> members = memberRepository.findAdministrativeMembers(memberIds);

        // Get all imageIds of the administrative members
        List<String> imageIds = administrativeMembers.stream()
                .map(administrativeMember -> administrativeMember.getImageId().toString())
                .toList();

        List<FullAdministrativeMember> fullAdministrativeMembers = new ArrayList<>(administrativeMembers.size());
        administrativeMembers.forEach(administrativeMember -> {
            Member member = members.stream().filter(listMember -> administrativeMember.getMemberId().equals(listMember.getId())).findFirst().orElseThrow(() -> new MemberNotFound(administrativeMember.getId()));
            fullAdministrativeMembers.add(
                    new FullAdministrativeMember(
                            administrativeMember.getId(),
                            member.getFirstname(),
                            member.getLastname(),
                            member.getFunction(),
                            administrativeMember.getRole(),
                            administrativeMember.getJobTitle(),
                            administrativeMember.getDescription(),
                            "", // todo change to image id
                            administrativeMember.getSupervisorId()
                    )
            );
        });

        return buildHierarchy(fullAdministrativeMembers).stream().map(AdministrativeMemberDTO::fromDomainModel).toList();
    }

    public List<FullAdministrativeMember> buildHierarchy(List<FullAdministrativeMember> members) {
        // Create a map for quick access to members by their IDs
        Map<UUID, FullAdministrativeMember> memberMap = new HashMap<>();
        for (FullAdministrativeMember member : members) {
            memberMap.put(member.getId(), member);
        }

        // Root members (those with no supervisor) will be added here
        List<FullAdministrativeMember> topLevelMembers = new ArrayList<>();

        // Build the hierarchy
        for (FullAdministrativeMember member : members) {
            if (member.getSupervisorId() == null) {
                // Top-level member (no supervisor)
                topLevelMembers.add(member);
            } else {
                // Find the supervisor and add this member as their subordinate
                FullAdministrativeMember supervisor = memberMap.get(member.getSupervisorId());
                if (supervisor != null) {
                    supervisor.getSubordinates().add(member);
                }
            }
        }

        return topLevelMembers;
    }

    public List<MemberDTO> addMembers(List<MemberDTO> memberRequests) throws MemberNotFound {
        if (memberRequests.isEmpty()) {
            return new ArrayList<>();
        }
        List<Member> members = memberRequests.stream().map(member -> new Member(member.firstname(), member.lastname(), Function.valueOf(member.function()))).toList();
        memberRepository.saveMembers(members);

        return members.stream().map(MemberDTO::fromDomainModel).toList();
    }

    public void addAdministrativeMembers(List<AdministrativeMemberDTO> administrativeMemberRequests) throws MemberNotFound {
        if (administrativeMemberRequests.isEmpty()) {
            return;
        }
        List<AdministrativeMember> administrativeMembers = new ArrayList<>(administrativeMemberRequests.size());
        List<Member> members = new ArrayList<>(administrativeMemberRequests.size());

        administrativeMemberRequests.forEach(administrativeMemberRequest -> {
            Member member = new Member(
                    administrativeMemberRequest.firstname(),
                    administrativeMemberRequest.lastname()
            );

            AdministrativeMember administrativeMember = new AdministrativeMember(
                    member.getId(),
                    Role.valueOf(administrativeMemberRequest.role()),
                    administrativeMemberRequest.jobTitle(),
                    administrativeMemberRequest.description(),
                    UUID.randomUUID(), // todo change to imageId
                    administrativeMemberRequest.supervisorId()
            );
            administrativeMembers.add(administrativeMember);
            members.add(member);
        });

        memberRepository.saveMembers(members);
        administrativeMemberRepository.saveAdministrativeMembers(administrativeMembers);
    }
}
