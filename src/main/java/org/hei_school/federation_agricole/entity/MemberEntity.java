package org.hei_school.federation_agricole.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
public class MemberEntity {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private GenderEnum gender;
    private String adress;
    private String profession;
    private int phoneNumber;
    private String email;
    private MemberOccupation occupation;
    private List<MemberEntity> referees;

    public MemberEntity() {

    }

    public MemberEntity(String id,String firstName, String lastName, LocalDate birthDate, GenderEnum gender, String adress, String profession, int phoneNumber, String email, MemberOccupation occupation, List<MemberEntity> referees) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.adress = adress;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.occupation = occupation;
        this.referees = referees;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MemberOccupation getOccupation() {
        return occupation;
    }

    public void setOccupation(MemberOccupation occupation) {
        this.occupation = occupation;
    }

    public List<MemberEntity> getReferees() {
        return referees;
    }

    public void setReferees(List<MemberEntity> referees) {
        this.referees = referees;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return phoneNumber == that.phoneNumber && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(birthDate, that.birthDate) && gender == that.gender && Objects.equals(adress, that.adress) && Objects.equals(profession, that.profession) && Objects.equals(email, that.email) && occupation == that.occupation && Objects.equals(referees, that.referees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate, gender, adress, profession, phoneNumber, email, occupation, referees);
    }
}