package org.hei_school.federation_agricole.dto;

import java.util.List;

public class MemberDTO {
    private int id;
    public String firstName;
    public String lastName;
    public String birthDate;
    public String collectivityIdentifier;
    public List<String> referees;
    public boolean registrationFeePaid;
    public boolean membershipDuesPaid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCollectivityIdentifier() {
        return collectivityIdentifier;
    }

    public void setCollectivityIdentifier(String collectivityIdentifier) {
        this.collectivityIdentifier = collectivityIdentifier;
    }

    public List<String> getReferees() {
        return referees;
    }

    public void setReferees(List<String> referees) {
        this.referees = referees;
    }

    public boolean isRegistrationFeePaid() {
        return registrationFeePaid;
    }

    public void setRegistrationFeePaid(boolean registrationFeePaid) {
        this.registrationFeePaid = registrationFeePaid;
    }

    public boolean isMembershipDuesPaid() {
        return membershipDuesPaid;
    }

    public void setMembershipDuesPaid(boolean membershipDuesPaid) {
        this.membershipDuesPaid = membershipDuesPaid;
    }
}
