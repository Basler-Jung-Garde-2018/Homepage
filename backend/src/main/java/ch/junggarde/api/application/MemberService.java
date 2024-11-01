package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.AdministrativeMemberRepository;
import ch.junggarde.api.adapter.out.persistance.ImageRepository;
import ch.junggarde.api.adapter.out.persistance.MemberRepository;
import ch.junggarde.api.application.dto.in.AddToMembersRequest;
import ch.junggarde.api.application.dto.out.AdministrativeMemberDTO;
import ch.junggarde.api.application.dto.out.MemberDTO;
import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.member.AdministrativeMember;
import ch.junggarde.api.model.member.Member;
import ch.junggarde.api.model.member.MemberNotFound;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
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

    public List<MemberDTO> addMembers(List<AddToMembersRequest> memberRequests) throws MemberNotFound {
        if (memberRequests.isEmpty()) {
            return new ArrayList<>();
        }
        List<Member> members = memberRequests.stream().map(member -> new Member(member.firstname(), member.lastname(), member.function())).toList();
        memberRepository.saveMembers(members);

        return members.stream().map(MemberDTO::fromDomainModel).toList();
    }
}
