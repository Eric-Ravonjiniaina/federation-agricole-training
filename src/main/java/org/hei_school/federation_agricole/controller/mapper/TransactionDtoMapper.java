package org.hei_school.federation_agricole.controller.mapper;

import org.hei_school.federation_agricole.controller.dto.CollectivityTransaction;
import org.hei_school.federation_agricole.controller.dto.PaymentMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionDtoMapper {
    private final FinancialAccountDtoMapper financialAccountDtoMapper;
    private final MemberDtoMapper memberDtoMapper;

    public CollectivityTransaction mapToDto(org.hei_school.federation_agricole.entity.CollectivityTransaction collectivityTransaction) {
        return CollectivityTransaction.builder()
                .id(collectivityTransaction.getId())
                .amount(collectivityTransaction.getAmount())
                .creationDate(collectivityTransaction.getCreationDate())
                .paymentMode(collectivityTransaction.getPaymentMode() == null ? null : PaymentMode.valueOf(collectivityTransaction.getPaymentMode().name()))
                .accountCredited(financialAccountDtoMapper.mapToDto(collectivityTransaction.getAccountCredited()))
                .memberDebited(memberDtoMapper.mapToDto(collectivityTransaction.getMemberDebited()))
                .build();
    }
}
