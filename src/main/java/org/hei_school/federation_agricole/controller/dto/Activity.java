package org.hei_school.federation_agricole.controller.dto;

import java.time.LocalDate;

public record Activity(
        String id,
        String label,
        String description,
        LocalDate activityDate
) {
}
