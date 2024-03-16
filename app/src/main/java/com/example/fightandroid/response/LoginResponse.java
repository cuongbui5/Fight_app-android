package com.example.fightandroid.response;


public class LoginResponse {
    private Long id;
    private String username;
    private String role;
    private String token;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
