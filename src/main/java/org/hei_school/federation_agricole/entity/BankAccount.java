package org.hei_school.federation_agricole.entity;

public class BankAccount extends FinancialAccount{
    private String Name;
    private Bank bankName;
    private String bankCode;
    private String branchCode;
    private String accountNumber;
    private String keyRIB;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Bank getBankName() {
        return bankName;
    }

    public void setBankName(Bank bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getKeyRIB() {
        return keyRIB;
    }

    public void setKeyRIB(String keyRIB) {
        this.keyRIB = keyRIB;
    }
}
