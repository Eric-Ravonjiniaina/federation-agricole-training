package org.hei_school.federation_agricole.controller.dto;

public class CollectivityStatisticsResponse {

    private int newMembersCount;
    private double upToDateMembersPercentage;

    public int getNewMembersCount() {
        return newMembersCount;
    }

    public void setNewMembersCount(int newMembersCount) {
        this.newMembersCount = newMembersCount;
    }

    public double getUpToDateMembersPercentage() {
        return upToDateMembersPercentage;
    }

    public void setUpToDateMembersPercentage(double upToDateMembersPercentage) {
        this.upToDateMembersPercentage = upToDateMembersPercentage;
    }
}
