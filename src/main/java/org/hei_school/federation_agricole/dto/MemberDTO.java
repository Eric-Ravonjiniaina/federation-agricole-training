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
}
