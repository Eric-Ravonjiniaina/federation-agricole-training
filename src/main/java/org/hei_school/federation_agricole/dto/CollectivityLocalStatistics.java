package org.hei_school.federation_agricole.dto;

public record CollectivityLocalStatistics(
        MemberDescription memberDescription,
        Double earnedAmount,
        Double unpaidAmount
) {}