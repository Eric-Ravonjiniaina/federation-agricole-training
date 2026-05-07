package org.hei_school.federation_agricole.controller.mapper;


import lombok.RequiredArgsConstructor;
import org.hei_school.federation_agricole.controller.dto.CreateCollectivity;
import org.hei_school.federation_agricole.controller.dto.CreateCollectivityStructure;
import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.CollectivityStructure;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectivityDtoMapper {
    private final MemberRepository memberRepository;
    private final MemberDtoMapper memberDtoMapper;

    public org.hei_school.federation_agricole.controller.dto.Collectivity mapToDto(Collectivity collectivity) {
        CollectivityStructure collectivityStructure = collectivity.getCollectivityStructure();
        return org.hei_school.federation_agricole.controller.dto.Collectivity.builder()
                .id(collectivity.getId())
                .name(collectivity.getName())
                .number(collectivity.getNumber())
                .location(collectivity.getLocation())
                .structure(collectivityStructure == null ? null : org.hei_school.federation_agricole.controller.dto.CollectivityStructure.builder()
                                                                  .president(memberDtoMapper.mapToDto(collectivityStructure.getPresident()))
                                                                  .vicePresident(memberDtoMapper.mapToDto(collectivityStructure.getVicePresident()))
                                                                  .treasurer(memberDtoMapper.mapToDto(collectivityStructure.getTreasurer()))
                                                                  .secretary(memberDtoMapper.mapToDto(collectivityStructure.getSecretary()))
                                                                  .build())
                .members(collectivity.getMembers().stream()
                        .map(memberDtoMapper::mapToDto)
                        .toList())
                .build();
    }

    public CollectivityStructure mapToEntity(CreateCollectivityStructure createCollectivityStructure) {
        return CollectivityStructure.builder()
                .president(memberRepository.findById(createCollectivityStructure.getPresident())
                        .orElseThrow(() -> new NotFoundException("Member.id" + createCollectivityStructure.getPresident() + " not found")))
                .vicePresident(memberRepository.findById(createCollectivityStructure.getVicePresident())
                        .orElseThrow(() -> new NotFoundException("Member.id" + createCollectivityStructure.getVicePresident() + " not found")))
                .treasurer(memberRepository.findById(createCollectivityStructure.getTreasurer())
                        .orElseThrow(() -> new NotFoundException("Member.id" + createCollectivityStructure.getTreasurer() + " not found")))
                .secretary(memberRepository.findById(createCollectivityStructure.getSecretary())
                        .orElseThrow(() -> new NotFoundException("Member.id" + createCollectivityStructure.getSecretary() + " not found")))
                .build();
    }


    public Collectivity mapToEntity(CreateCollectivity createCollectivity) {
        var createCollectivityStructure = createCollectivity.getStructure();
        return Collectivity.builder()
                .location(createCollectivity.getLocation())
                .collectivityStructure(mapToEntity(createCollectivityStructure))
                .federationApproval(createCollectivity.getFederationApproval())
                .members(createCollectivity.getMembers().stream()
                        .map(memberIdentifier -> memberRepository.findById(memberIdentifier).orElseThrow(() -> new NotFoundException("Member.id=" + memberIdentifier + " not found")))
                        .toList())
                .build();
    }

}
