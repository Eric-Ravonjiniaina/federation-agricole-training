package org.hei_school.federation_agricole.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hei_school.federation_agricole.entity.ActivityType;
import org.hei_school.federation_agricole.entity.MemberOccupation;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCollectivityActivityDto {
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private MonthlyRecurrenceRuleDto recurrenceRule;
    private LocalDate executiveDate;
}
