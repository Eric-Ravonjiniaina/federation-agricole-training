package org.hei_school.federation_agricole.dto;

import java.util.List;

import java.util.List;

public class MemberDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String address;
    private String profession;
    private int phoneNumber;
    private String email;
    private String occupation;
    private List<MemberDTO> referees;

    // champs CreateMember uniquement (ignorés en réponse)
    private String collectivityIdentifier;
    private List<String> refereeIds;
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public int getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public List<MemberDTO> getReferees() { return referees; }
    public void setReferees(List<MemberDTO> referees) { this.referees = referees; }
    public String getCollectivityIdentifier() { return collectivityIdentifier; }
    public void setCollectivityIdentifier(String collectivityIdentifier) { this.collectivityIdentifier = collectivityIdentifier; }
    public List<String> getRefereeIds() { return refereeIds; }
    public void setRefereeIds(List<String> refereeIds) { this.refereeIds = refereeIds; }
    public boolean isRegistrationFeePaid() { return registrationFeePaid; }
    public void setRegistrationFeePaid(boolean registrationFeePaid) { this.registrationFeePaid = registrationFeePaid; }
    public boolean isMembershipDuesPaid() { return membershipDuesPaid; }
    public void setMembershipDuesPaid(boolean membershipDuesPaid) { this.membershipDuesPaid = membershipDuesPaid; }
}