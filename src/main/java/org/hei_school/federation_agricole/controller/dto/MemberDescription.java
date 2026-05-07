package org.hei_school.federation_agricole.controller.dto;

public record MemberDescription(
        String id,
        String firstName,
        String lastName,
        String email,
        String occupation
) {}