package org.hei_school.federation_agricole.mapper;

import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.dto.response.CollectivityResponse;
import org.hei_school.federation_agricole.dto.response.CollectivityStructureResponse;
import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.entity.MemberEntity;

public class CollectivityMapper {

    public static CollectivityResponse toResponse(Collectivity c) {
        CollectivityResponse r = new CollectivityResponse();

        r.setId(c.getId());
        r.setLocation(c.getLocation());

        r.setMembers(
                c.getMembers().stream().map(m -> {
                    MemberDTO mr = new MemberDTO();
                    mr.setId(m.getId());
                    return mr;
                }).toList()
        );

        CollectivityStructureResponse s = new CollectivityStructureResponse();
        s.setPresident(toMember(c.getStructure().getPresident()));
        s.setVicePresident(toMember(c.getStructure().getVicePresident()));
        s.setTreasurer(toMember(c.getStructure().getTreasurer()));
        s.setSecretary(toMember(c.getStructure().getSecretary()));

        r.setStructure(s);

        return r;
    }

    private static MemberDTO toMember(MemberEntity m) {
        MemberDTO r = new MemberDTO();
        r.setId(m.getId());
        return r;
    }
}
