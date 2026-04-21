package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.repository.MemberRepository;

import java.util.UUID;

public class MemberService{
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) { this.repository = repository; }

    public MemberEntity create(MemberDTO dto) throws Exception {

        if (!dto.registrationFeePaid || !dto.membershipDuesPaid) {
            throw new BadRequestException("Registration or membership dues not paid.");
        }

        MemberEntity m = new MemberEntity();
        m.setFirstName(dto.firstName);
        m.setLastName(dto.lastName);
        String generatedId = repository.save(m, dto.collectivityIdentifier);

        m.setId(Integer.parseInt(generatedId));

        return m;
    }
}