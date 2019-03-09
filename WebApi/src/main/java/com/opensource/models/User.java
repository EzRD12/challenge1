package com.opensource.models;

public class User {
    public User() {
    }

    public User(String username, String password, String fullName) {
        Username = username;
        Password = password;
        FullName = fullName;
    }

    public int Id;
    public String Username;
    public String Password;
    public String FullName;
}
