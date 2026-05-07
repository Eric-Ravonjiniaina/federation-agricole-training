package org.hei_school.federation_agricole.controller.mapper;

import lombok.RequiredArgsConstructor;
import org.hei_school.federation_agricole.controller.dto.CreateMembershipFee;
import org.hei_school.federation_agricole.entity.ActivityStatus;
import org.hei_school.federation_agricole.entity.Frequency;
import org.hei_school.federation_agricole.entity.MembershipFee;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipFeeDtoMapper {
    public MembershipFee mapToDto(
            org.hei_school.federation_agricole.entity.MembershipFee membershipFee) {
        return MembershipFee.builder()
                .id(membershipFee.getId())
                .label(membershipFee.getLabel())
                .amount(membershipFee.getAmount())
                .frequency(membershipFee.getFrequency() == null ? null : Frequency.valueOf(membershipFee.getFrequency().name()))
                .status(membershipFee.getStatus() == null ? null : ActivityStatus.valueOf(membershipFee.getStatus().name()))
                .eligibleFrom(membershipFee.getEligibleFrom())
                .build();
    }

    public org.hei_school.federation_agricole.entity.MembershipFee mapToEntity(CreateMembershipFee createMembershipFee) {
        return org.hei_school.federation_agricole.entity.MembershipFee.builder()
                .label(createMembershipFee.getLabel())
                .amount(createMembershipFee.getAmount())
                .frequency(createMembershipFee.getFrequency() == null ? null : org.hei_school.federation_agricole.entity.Frequency.valueOf(createMembershipFee.getFrequency().name()))
                .eligibleFrom(createMembershipFee.getEligibleFrom())
                .build();
    }
}