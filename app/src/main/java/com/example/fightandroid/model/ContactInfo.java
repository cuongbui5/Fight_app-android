package com.example.fightandroid.model;




public class ContactInfo {

    private Long id;
    private String firstName;
    private String lastName;
    private String numberPhone;
    private String email;

    public ContactInfo(String firstName, String lastName, String numberPhone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberPhone = numberPhone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
