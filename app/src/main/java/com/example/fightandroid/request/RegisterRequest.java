package com.example.fightandroid.request;


public class RegisterRequest {

    private String username;

    private String password;
    private String passwordConfirm;

    public RegisterRequest(String username, String password, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
