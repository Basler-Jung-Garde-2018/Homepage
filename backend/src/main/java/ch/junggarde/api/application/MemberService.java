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

import java.util.ArrayList;
import java.util.List;

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

        return administrativeMembers.stream()
                .map(administrativeMember -> AdministrativeMemberDTO.fromDomainModel(
                                administrativeMember,
                                members.stream()
                                        .filter(member -> member.getId().equals(administrativeMember.getId()))
                                        .findFirst()
                                        .orElseThrow(() -> new MemberNotFound(administrativeMember.getId())),
                                images.stream()
                                        .filter(image -> image.getId().equals(administrativeMember.getImageId()))
                                        .findFirst()
                                        .orElse(new Image())
                        )
                ).toList();
    }

    public List<MemberDTO> addMembers(List<MemberDTO> memberRequests) throws MemberNotFound {
        if (memberRequests.isEmpty()) {
            return new ArrayList<>();
        }
        List<Member> members = memberRequests.stream().map(member -> new Member(member.firstname(), member.lastname(), Function.valueOf(member.function()))).toList();
        memberRepository.saveMembers(members);

        return members.stream().map(MemberDTO::fromDomainModel).toList();
    }

    public List<AdministrativeMemberDTO> addAdministrativeMembers(List<AdministrativeMemberDTO> administrativeMemberRequests) throws MemberNotFound {
        if (administrativeMemberRequests.isEmpty()) {
            return new ArrayList<>();
        }
        List<AdministrativeMemberDTO> response = new ArrayList<>(administrativeMemberRequests.size());
        List<Image> images = new ArrayList<>();
        List<AdministrativeMember> administrativeMembers = new ArrayList<>(administrativeMemberRequests.size());
        List<Member> members = new ArrayList<>(administrativeMemberRequests.size());

        administrativeMemberRequests.forEach(administrativeMemberRequest -> {
            Member member = new Member(
                    administrativeMemberRequest.firstname(),
                    administrativeMemberRequest.lastname()
            );
            Image image = new Image(
                    administrativeMemberRequest.imageBase64()
            );

            AdministrativeMember administrativeMember = new AdministrativeMember(
                    member.getId(),
                    Role.valueOf(administrativeMemberRequest.role()),
                    administrativeMemberRequest.jobTitle(),
                    administrativeMemberRequest.description(),
                    image.getId(),
                    administrativeMemberRequest.supervisorId()
            );
            images.add(image);
            administrativeMembers.add(administrativeMember);
            members.add(member);

            response.add(AdministrativeMemberDTO.fromDomainModel(administrativeMember, member, image));
        });

        imageRepository.saveImages(images);
        memberRepository.saveMembers(members);
        administrativeMemberRepository.saveAdministrativeMembers(administrativeMembers);

        return response;
    }
}
