package org.hei_school.federation_agricole.dto.response;

import org.hei_school.federation_agricole.dto.MemberDTO;

public class CollectivityStructureResponse {
    private MemberDTO president;
    private MemberDTO vicePresident;
    private MemberDTO treasurer;
    private MemberDTO secretary;

    public CollectivityStructureResponse() {
    }

    public CollectivityStructureResponse(MemberDTO president, MemberDTO vicePresident, MemberDTO treasurer, MemberDTO secretary) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.treasurer = treasurer;
        this.secretary = secretary;
    }

    public MemberDTO getPresident() {
        return president;
    }

    public void setPresident(MemberDTO president) {
        this.president = president;
    }

    public MemberDTO getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(MemberDTO vicePresident) {
        this.vicePresident = vicePresident;
    }

    public MemberDTO getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(MemberDTO treasurer) {
        this.treasurer = treasurer;
    }

    public MemberDTO getSecretary() {
        return secretary;
    }

    public void setSecretary(MemberDTO secretary) {
        this.secretary = secretary;
    }
}
