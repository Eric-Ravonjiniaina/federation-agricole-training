package org.hei_school.federation_agricole.controller.mapper;


import lombok.RequiredArgsConstructor;
import org.hei_school.federation_agricole.controller.dto.CreateMemberPayment;
import org.hei_school.federation_agricole.entity.*;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.repository.FinancialAccountRepository;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.hei_school.federation_agricole.repository.MembershipFeeRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPaymentDtoMaper {
    private final FinancialAccountDtoMapper financialAccountDtoMapper;
    private final MemberRepository memberRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;

    public MemberPayment mapToEntity(String memberIdentifier, CreateMemberPayment createMemberPayment) {
        Member member = memberRepository.findById(memberIdentifier).orElseThrow(
                () -> new NotFoundException("Member.id=" + memberIdentifier + " not found")
        );
        MembershipFee membershipFee = membershipFeeRepository.findById(createMemberPayment.getMembershipFeeIdentifier())
                .orElseThrow(
                        () -> new NotFoundException("MembershipFee.id=" + createMemberPayment.getMembershipFeeIdentifier() + " not found")
                );
        FinancialAccount financialAccount = financialAccountRepository.findFinancialAccountById(createMemberPayment.getAccountCreditedIdentifier())
                .orElseThrow(() -> new NotFoundException("FinancialAccount.id=" + createMemberPayment.getAccountCreditedIdentifier() + " not found"));

        return MemberPayment.builder()
                .paymentMode(createMemberPayment.getPaymentMode() == null ? null : PaymentMode.valueOf(createMemberPayment.getPaymentMode().name()))
                .amount(createMemberPayment.getAmount())
                .memberOwner(member)
                .membershipFee(membershipFee)
                .accountCredited(financialAccount)
                .build();
    }

    public org.hei_school.federation_agricole.controller.dto.MemberPayment mapToDto(MemberPayment memberPayment) {
        return org.hei_school.federation_agricole.controller.dto.MemberPayment.builder()
                .id(memberPayment.getId())
                .paymentMode(memberPayment.getPaymentMode() == null ? null : org.hei_school.federation_agricole.controller.dto.PaymentMode.valueOf(memberPayment.getPaymentMode().name()))
                .accountCredited(financialAccountDtoMapper.mapToDto(memberPayment.getAccountCredited()))
                .creationDate(memberPayment.getCreationDate())
                .amount(memberPayment.getAmount())
                .build();
    }
}
