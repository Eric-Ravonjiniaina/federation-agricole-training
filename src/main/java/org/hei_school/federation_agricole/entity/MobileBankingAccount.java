package org.hei_school.federation_agricole.entity;

public class MobileBankingAccount extends FinancialAccount {
    private String mobileNumber;
    private String serviceName;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}