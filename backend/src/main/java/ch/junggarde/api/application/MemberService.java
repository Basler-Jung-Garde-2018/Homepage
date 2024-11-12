package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AdministrativeMemberRepository;
import ch.junggarde.api.adapter.out.persistance.ImageRepository;
import ch.junggarde.api.adapter.out.persistance.MemberRepository;
import ch.junggarde.api.application.dto.AdministrativeMemberDTO;
import ch.junggarde.api.application.dto.MemberDTO;
import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.member.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@ApplicationScoped
@Log4j2
public class MemberService {
    @Inject
    MemberRepository memberRepository;

    @Inject
    ImageRepository imageRepository;

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

        List<Image> images = imageRepository.findImagesByIds(imageIds);

        List<FullAdministrativeMember> fullAdministrativeMembers = new ArrayList<>(administrativeMembers.size());
        administrativeMembers.forEach(administrativeMember -> {
            Member member = members.stream().filter(listMember -> administrativeMember.getMemberId().equals(listMember.getId())).findFirst().orElseThrow(() -> new MemberNotFound(administrativeMember.getId()));
            Image image = images.stream().filter(listImage -> administrativeMember.getImageId().equals(listImage.getId())).findFirst().orElse(null);
            fullAdministrativeMembers.add(
                    new FullAdministrativeMember(
                            administrativeMember.getId(),
                            member.getFirstname(),
                            member.getLastname(),
                            member.getFunction(),
                            administrativeMember.getRole(),
                            administrativeMember.getJobTitle(),
                            administrativeMember.getDescription(),
                            image != null ? image.getBase64() : "",
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
        List<Image> images = new ArrayList<>();
        List<AdministrativeMember> administrativeMembers = new ArrayList<>(administrativeMemberRequests.size());
        List<Member> members = new ArrayList<>(administrativeMemberRequests.size());

        administrativeMemberRequests.forEach(administrativeMemberRequest -> {
            Member member = new Member(
                    administrativeMemberRequest.firstname(),
                    administrativeMemberRequest.lastname()
            );

            Image image = new Image();
            if (administrativeMemberRequest.imageBase64().equals("BILD")) {
                image.setId(UUID.fromString("590363f1-1f66-4930-a268-daa391c32d0f"));
            } else {
                image.setBase64(administrativeMemberRequest.imageBase64());
                images.add(image);
            }

            AdministrativeMember administrativeMember = new AdministrativeMember(
                    member.getId(),
                    Role.valueOf(administrativeMemberRequest.role()),
                    administrativeMemberRequest.jobTitle(),
                    administrativeMemberRequest.description(),
                    image.getId(),
                    administrativeMemberRequest.supervisorId()
            );
            administrativeMembers.add(administrativeMember);
            members.add(member);
        });

        imageRepository.saveImages(images);
        memberRepository.saveMembers(members);
        administrativeMemberRepository.saveAdministrativeMembers(administrativeMembers);
    }
}
