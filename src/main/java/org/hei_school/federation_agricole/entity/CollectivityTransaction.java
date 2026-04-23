package org.hei_school.federation_agricole.entity;

import java.time.LocalDate;

public class CollectivityTransaction {

    private String id;
    private String collectivityId;
    private int memberId;
    private double amount;
    private String paymentMode;
    private LocalDate creationDate;
    private FinancialAccount accountCredited;
    private MemberEntity memberDebited;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectivityId() {
        return collectivityId;
    }

    public void setCollectivityId(String collectivityId) {
        this.collectivityId = collectivityId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public FinancialAccount getAccountCredited() {
        return accountCredited;
    }

    public MemberEntity getMemberDebited() {
        return memberDebited;
    }

    public void setAccountCredited(FinancialAccount account) {
        this.accountCredited = account;
    }

    public void setMemberDebited(MemberEntity member) {
        this.memberDebited = member;
    }

}
