package com.opensource.models;

public class AuthenticationResponse {

    public AuthenticationResponse(String username, String fullName) {
        Username = username;
        FullName = fullName;
    }

    public String Username;
    public String FullName;
}
