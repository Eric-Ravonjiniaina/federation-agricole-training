package org.hei_school.federation_agricole.entity;

public class CollectivityStructure {

    private MemberEntity president;
    private MemberEntity vicePresident;
    private MemberEntity treasurer;
    private MemberEntity secretary;

    public CollectivityStructure() {
    }

    public CollectivityStructure(MemberEntity president, MemberEntity vicePresident, MemberEntity treasurer, MemberEntity secretary) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.treasurer = treasurer;
        this.secretary = secretary;
    }

    public MemberEntity getPresident() {
        return president;
    }

    public void setPresident(MemberEntity president) {
        this.president = president;
    }

    public MemberEntity getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(MemberEntity vicePresident) {
        this.vicePresident = vicePresident;
    }

    public MemberEntity getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(MemberEntity treasurer) {
        this.treasurer = treasurer;
    }

    public MemberEntity getSecretary() {
        return secretary;
    }

    public void setSecretary(MemberEntity secretary) {
        this.secretary = secretary;
    }

    @Override
    public String toString() {
        return "CollectivityStructure{" +
                "president=" + president +
                ", vicePresident=" + vicePresident +
                ", treasurer=" + treasurer +
                ", secretary=" + secretary +
                '}';
    }
}
