package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.entity.*;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public MemberEntity create(MemberDTO dto) throws Exception {
        // Validations OAS
        if (!dto.isRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee not paid");
        }
        if (!dto.isMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues not paid");
        }
        if (dto.getRefereeIds() == null || dto.getRefereeIds().size() < 2) {
            throw new BadRequestException("At least 2 referees required");
        }

        MemberEntity m = new MemberEntity();
        m.setId(UUID.randomUUID().toString());
        m.setFirstName(dto.getFirstName());
        m.setLastName(dto.getLastName());
        if (dto.getBirthDate() != null) m.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        if (dto.getGender() != null) m.setGender(GenderEnum.valueOf(dto.getGender()));
        m.setAddress(dto.getAddress());
        m.setProfession(dto.getProfession());
        m.setPhoneNumber(dto.getPhoneNumber());
        m.setEmail(dto.getEmail());
        if (dto.getOccupation() != null) m.setOccupation(MemberOccupation.valueOf(dto.getOccupation()));

        repository.save(m);

        // Vérifier et sauvegarder les référents
        for (String refId : dto.getRefereeIds()) {
            repository.findById(refId)
                    .orElseThrow(() -> new NotFoundException("Referee not found: " + refId));
        }
        repository.saveReferees(m.getId(), dto.getRefereeIds());

        // Charger les référents pour la réponse
        List<MemberEntity> referees = repository.findRefereesByMemberId(m.getId());
        m.setReferees(referees);

        return m;
    }

    public Optional<MemberEntity> findById(String id) {
        return repository.findById(id);
    }
}