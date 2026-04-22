package org.hei_school.federation_agricole.entity;

import java.time.LocalDate;

public class MemberShipFee {
    private String id;

    private String label;
    private Double amount;
    private LocalDate eligibleFrom;
    private Frequence frequency;
    private ActivityStatus status;
    private Collectivity collectivity;
}
