package com.example.tyminiproject.Model;

public class MessUser {
    private String Address;
    private String Name;
    private String Password;
    private String RegNo;
    private String Phone;

    public MessUser() {
    }
    public MessUser(String address,String name, String password ,String regno ) {
        Address = address;
        Name = name;
        Password = password;
        RegNo = regno;
    }
    public MessUser(String address,String name, String password,String regNo, String phone) {
        Address = address;
        Name = name;
        Password = password;
        Phone = phone;
    }

    public String getAddress() {
        return Address;

    }

    public String getRegNo() {
        return RegNo;
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

    public void setAddress(String address) {
        Address = address;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }
}
