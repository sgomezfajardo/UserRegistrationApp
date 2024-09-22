package com.example.userregistrationapp.model;

public class User {
    private String fullName;
    private String username;
    private String email;
    private String address;
    private String password;
    private String role;
    private String birthDate;
    private String gender;

    public User(String fullName, String username, String email, String address, String password, String role, String birthDate, String gender) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.role = role;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Otros getters y setters según sea necesario
}
