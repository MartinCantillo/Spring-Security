package com.example.demo.Model;



public class AuthenticationRequest {

    private string username;
    private string password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(string username, string password) {
        this.username = username;
        this.password = password;
    }

    public string getUsername() {
        return this.username;
    }

    public void setUsername(string username) {
        this.username = username;
    }

    public string getPassword() {
        return this.password;
    }

    public void setPassword(string password) {
        this.password = password;
    }

}
