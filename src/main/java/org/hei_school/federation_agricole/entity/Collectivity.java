package org.hei_school.federation_agricole.entity;

import java.util.List;

public class Collectivity {
    private String id;
    private String location;
    private String number;
    private String name;
    private CollectivityStructure structure;
    private List<MemberEntity> members;


    public Collectivity() {
    }

    public Collectivity(String id, String location, String number, String name, CollectivityStructure structure, List<MemberEntity> members) {
        this.id = id;
        this.location = location;
        this.number = number;
        this.name = name;
        this.structure = structure;
        this.members = members;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CollectivityStructure getStructure() {
        return structure;
    }

    public void setStructure(CollectivityStructure structure) {
        this.structure = structure;
    }

    public List<MemberEntity> getMembers() {
        return members;
    }

    public void setMembers(List<MemberEntity> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Collectivity{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", structure=" + structure +
                ", members=" + members +
                '}';
    }
}
