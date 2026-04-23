package org.hei_school.federation_agricole.entity;

public abstract class FinancialAccount {
    private String id;
    private Double amount;
    private Collectivity collectivity;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Collectivity getCollectivity() { return collectivity; }
    public void setCollectivity(Collectivity collectivity) { this.collectivity = collectivity; }
}

