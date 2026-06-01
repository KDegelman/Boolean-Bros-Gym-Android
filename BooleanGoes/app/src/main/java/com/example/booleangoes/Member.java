package com.example.booleangoes;

public class Member {
    public String memberNumber;
    public String fullName;
    public String dateOfBirth;
    public String gender;
    public String phoneNumber;
    public String email;
    public String city;

    public Member(String memberNumber, String fullName, String dateOfBirth,
                  String gender, String phoneNumber, String email, String city) {
        this.memberNumber = memberNumber;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
    }
}