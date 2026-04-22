package org.hei_school.federation_agricole.entity;

import java.time.LocalDate;

public class MemberPayment {
    private String id;
    private double amount;
    private PaymentMode payment;
    private MemberShipFee memberShipFee;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;


}
