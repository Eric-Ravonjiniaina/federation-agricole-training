package org.hei_school.federation_agricole.controller.mapper;

import org.hei_school.federation_agricole.controller.dto.CollectivityActivityDto;
import org.hei_school.federation_agricole.controller.dto.CreateCollectivityActivityDto;
import org.hei_school.federation_agricole.controller.dto.MonthlyRecurrenceRuleDto;
import org.hei_school.federation_agricole.entity.CollectivityActivity;
import org.springframework.stereotype.Component;

@Component
public class ActivityDtoMapper {
    public CollectivityActivityDto mapToDto(CollectivityActivity activity) {
        CollectivityActivityDto dto = new CollectivityActivityDto();
        dto.setId(activity.getId());
        dto.setLabel(activity.getLabel());
        dto.setActivityType(activity.getActivityType());
        dto.setMemberOccupationConcerned(activity.getMemberOccupationConcerned());
        dto.setExecutiveDate(activity.getExecutiveDate());

        // Recurrence rule : seulement si weekOrdinal ET dayOfWeek sont présents
        if (activity.getWeekOrdinal() != null && activity.getDayOfWeek() != null) {
            MonthlyRecurrenceRuleDto recurrenceRule = new MonthlyRecurrenceRuleDto();
            recurrenceRule.setWeekOrdinal(activity.getWeekOrdinal());
            recurrenceRule.setDayOfWeek(activity.getDayOfWeek());
            dto.setRecurrenceRule(recurrenceRule);
        } else {
            dto.setRecurrenceRule(null);
        }

        return dto;
    }

    // DTO → Entity (pour la création)
    public CollectivityActivity mapToEntity(CreateCollectivityActivityDto dto) {
        CollectivityActivity activity = new CollectivityActivity();
        activity.setLabel(dto.getLabel());
        activity.setActivityType(dto.getActivityType());
        activity.setMemberOccupationConcerned(dto.getMemberOccupationConcerned());
        activity.setExecutiveDate(dto.getExecutiveDate());

        // Recurrence rule → extraire weekOrdinal et dayOfWeek
        if (dto.getRecurrenceRule() != null) {
            activity.setWeekOrdinal(dto.getRecurrenceRule().getWeekOrdinal());
            activity.setDayOfWeek(dto.getRecurrenceRule().getDayOfWeek());
        } else {
            activity.setWeekOrdinal(null);
            activity.setDayOfWeek(null);
        }

        return activity;
    }
}