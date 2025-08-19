package com.example.movie_app.dtos.requests;

public class UserRequest {
    private String email;
    private String password;
    private String fullname;
    private String username;

    public UserRequest(String email, String username, String fullname, String password) {
        this.email = email;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
