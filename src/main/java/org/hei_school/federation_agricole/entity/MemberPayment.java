package org.hei_school.federation_agricole.entity;

import java.time.LocalDate;
import java.util.Objects;

public class MemberPayment {
    private String id;
    private double amount;
    private PaymentMode payment;
    private MemberShipFee memberShipFee;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;

    public  MemberPayment() {

    }
    public MemberPayment(String id, double amount, PaymentMode payment, MemberShipFee memberShipFee, FinancialAccount accountCredited, LocalDate creationDate) {
        this.id = id;
        this.amount = amount;
        this.payment = payment;
        this.memberShipFee = memberShipFee;
        this.accountCredited = accountCredited;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMode getPayment() {
        return payment;
    }

    public void setPayment(PaymentMode payment) {
        this.payment = payment;
    }

    public MemberShipFee getMemberShipFee() {
        return memberShipFee;
    }

    public void setMemberShipFee(MemberShipFee memberShipFee) {
        this.memberShipFee = memberShipFee;
    }

    public FinancialAccount getAccountCredited() {
        return accountCredited;
    }

    public void setAccountCredited(FinancialAccount accountCredited) {
        this.accountCredited = accountCredited;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberPayment that = (MemberPayment) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(id, that.id) && payment == that.payment && Objects.equals(memberShipFee, that.memberShipFee) && Objects.equals(accountCredited, that.accountCredited) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, payment, memberShipFee, accountCredited, creationDate);
    }
}
