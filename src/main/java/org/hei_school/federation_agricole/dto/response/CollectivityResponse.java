package org.hei_school.federation_agricole.dto.response;

import org.hei_school.federation_agricole.dto.MemberDTO;

import java.util.List;

public class CollectivityResponse {
    private String id;
    private String location;
    private CollectivityStructureResponse structure;
    private List<MemberDTO> members;

    public CollectivityResponse() {
    }

    public CollectivityResponse(String location, String id, CollectivityStructureResponse structure, List<MemberDTO> members) {
        this.location = location;
        this.id = id;
        this.structure = structure;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CollectivityStructureResponse getStructure() {
        return structure;
    }

    public void setStructure(CollectivityStructureResponse structure) {
        this.structure = structure;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }
}
