package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.CreateMemberPaymentDTO;
import org.hei_school.federation_agricole.entity.MemberPayment;

import java.time.LocalDate;

public class PaymentService {

    public MemberPayment processPayment(String memberId, CreateMemberPaymentDTO dto) {

        MemberPayment payment = new MemberPayment();
        payment.setAmount(dto.amount());
        payment.setCreationDate(LocalDate.now());

        return ;
    }
}