package org.hei_school.federation_agricole.dto;

public record MemberDescription(
        String id,
        String firstName,
        String lastName,
        String email,
        String occupation
) {}