package com.example.fightandroid.request;


public class CreateContactInfoRequest {
    private String firstName;
    private String lastName;
    private String numberPhone;
    private String email;

    public CreateContactInfoRequest(String firstName, String lastName, String numberPhone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberPhone = numberPhone;
        this.email = email;
    }
}
