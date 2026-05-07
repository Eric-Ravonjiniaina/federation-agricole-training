package org.hei_school.federation_agricole.controller.dto;

import lombok.Data;
import org.hei_school.federation_agricole.entity.ActivityDayOfWeek;

@Data
public class MonthlyRecurrenceRuleDto {
    private Integer weekOrdinal;
    private ActivityDayOfWeek dayOfWeek;
}
