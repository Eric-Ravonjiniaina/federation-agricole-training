package org.hei_school.federation_agricole.dto.response;

import org.hei_school.federation_agricole.dto.MemberDTO;

import java.util.List;

public class CollectivityResponse {
    private String id;
    private String name;
    private String number;
    private String location;
    private CollectivityStructureResponse structure;
    private List<MemberDTO> members;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public CollectivityStructureResponse getStructure() { return structure; }
    public void setStructure(CollectivityStructureResponse structure) { this.structure = structure; }
    public List<MemberDTO> getMembers() { return members; }
    public void setMembers(List<MemberDTO> members) { this.members = members; }
}
