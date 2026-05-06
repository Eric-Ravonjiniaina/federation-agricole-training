package org.hei_school.federation_agricole.mapper;

import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.dto.response.CollectivityResponse;
import org.hei_school.federation_agricole.dto.response.CollectivityStructureResponse;
import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.MemberEntity;

import java.util.Collections;
import java.util.List;

public class CollectivityMapper {

    public static CollectivityResponse toResponse(Collectivity c) {
        CollectivityResponse r = new CollectivityResponse();
        r.setId(c.getId());
        r.setLocation(c.getLocation());
        r.setName(c.getName());
        r.setNumber(c.getNumber());

        if (c.getMembers() != null) {
            r.setMembers(c.getMembers().stream()
                    .map(CollectivityMapper::toMemberDTO)
                    .toList());
        }

        if (c.getStructure() != null) {
            CollectivityStructureResponse s = new CollectivityStructureResponse();
            s.setPresident(toMemberDTO(c.getStructure().getPresident()));
            s.setVicePresident(toMemberDTO(c.getStructure().getVicePresident()));
            s.setTreasurer(toMemberDTO(c.getStructure().getTreasurer()));
            s.setSecretary(toMemberDTO(c.getStructure().getSecretary()));
            r.setStructure(s);
        }

        return r;
    }

    public static MemberDTO toMemberDTO(MemberEntity m) {
        if (m == null) return null;
        MemberDTO dto = new MemberDTO();
        dto.setId(m.getId());
        dto.setFirstName(m.getFirstName());
        dto.setLastName(m.getLastName());
        dto.setBirthDate(m.getBirthDate() != null ? m.getBirthDate().toString() : null);
        dto.setGender(m.getGender() != null ? m.getGender().name() : null);
        dto.setAddress(m.getAddress());
        dto.setProfession(m.getProfession());
        dto.setPhoneNumber(m.getPhoneNumber());
        dto.setEmail(m.getEmail());
        dto.setOccupation(m.getOccupation() != null ? m.getOccupation().name() : null);
        if (m.getReferees() != null) {
            dto.setReferees(m.getReferees().stream()
                    .map(CollectivityMapper::toMemberDTO)
                    .toList());
        } else {
            dto.setReferees(Collections.emptyList());
        }
        return dto;
    }
}
