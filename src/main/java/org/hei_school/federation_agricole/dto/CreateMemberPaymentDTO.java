package org.hei_school.federation_agricole.dto;

import org.hei_school.federation_agricole.entity.PaymentMode;

public record CreateMemberPaymentDTO(
        Integer amount,
        String membershipFeeIdentifier,
        String accountCreditedIdentifier,
        PaymentMode paymentMode
) {}