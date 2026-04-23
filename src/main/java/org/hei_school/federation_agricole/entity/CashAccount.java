package org.hei_school.federation_agricole.entity;

public class CashAccount extends FinancialAccount {

    public CashAccount() {
        super();
    }

    public CashAccount(String id, Double amount, Collectivity collectivity) {
        this.setId(id);
        this.setAmount(amount);
        this.setCollectivity(collectivity);
    }
}