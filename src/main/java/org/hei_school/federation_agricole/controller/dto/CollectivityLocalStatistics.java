package org.hei_school.federation_agricole.controller.dto;

public record CollectivityLocalStatistics(
        MemberDescription memberDescription,
        Double earnedAmount,
        Double unpaidAmount
) {}
