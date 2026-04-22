package org.hei_school.federation_agricole.dto.request;

import java.util.List;

public class CreateCollectivityRequest {
    private String location;
    private List<String> members;
    private Boolean federationApproval;
    private CreateCollectivityStructureRequest structure;

    public CreateCollectivityRequest() {
    }

    public CreateCollectivityRequest(Boolean federationApproval, String location, List<String> members, CreateCollectivityStructureRequest structure) {
        this.federationApproval = federationApproval;
        this.location = location;
        this.members = members;
        this.structure = structure;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Boolean getFederationApproval() {
        return federationApproval;
    }

    public void setFederationApproval(Boolean federationApproval) {
        this.federationApproval = federationApproval;
    }

    public CreateCollectivityStructureRequest getStructure() {
        return structure;
    }

    public void setStructure(CreateCollectivityStructureRequest structure) {
        this.structure = structure;
    }
}
