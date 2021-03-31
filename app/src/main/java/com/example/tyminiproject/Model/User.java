package com.example.tyminiproject.Model;

public class User {
    private String Name;

    public User(String name, String password, String messOwner) {
        Name = name;
        Password = password;
        MessOwner = messOwner;
    }

    private String Password;
    private String Phone;
    private String SecureCode;
    private String MessOwner;

    public User() {
    }

    public String getSecureCode() {
        return SecureCode;
    }

    public User(String name, String password, String phone, String secureCode, String messOwner) {
        Name = name;
        Password = password;
        Phone = phone;
        SecureCode = secureCode;
        MessOwner = messOwner;
    }

    public void setSecureCode(String secureCode) {
        this.SecureCode = secureCode;
    }
/*
    public User(String name, String password, String secureCode) {
        Name = name;
        Password = password;
        SecureCode = secureCode;
    }*/

    public User(String name, String password, String phone, String secureCode) {
        Name = name;
        Password = password;
        Phone = phone;
        SecureCode = secureCode;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMessOwner() {
        return MessOwner;
    }

    public void setMessOwner(String messOwner) {
        MessOwner = messOwner;
    }
}
