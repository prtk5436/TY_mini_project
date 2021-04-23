 package com.example.tyminiproject.Model;

public class MessUser {
    private String Image;
    private String Owner;
    private String Time;
    private String Address;
    private String Name;
    private String Password;
    private String RegNo;
    private String offDay;

    public String getOffDay() {
        return offDay;
    }

    public MessUser(String image, String owner, String time, String address, String name, String password, String regNo, String offDay, String phone) {
        Image = image;
        Owner = owner;
        Time = time;
        Address = address;
        Name = name;
        Password = password;
        RegNo = regNo;
        this.offDay = offDay;
        Phone = phone;
    }

    public void setOffDay(String offDay) {
        this.offDay = offDay;
    }

    public MessUser(String image, String owner, String time, String address, String name, String password, String regNo, String phone) {
        Image = image;
        Owner = owner;
        Time = time;
        Address = address;
        Name = name;
        Password = password;
        RegNo = regNo;
        Phone = phone;
    }

    private String Phone;

    public MessUser() {
    }

    public MessUser(String image, String owner, String time, String address, String name, String password, String regNo) {
        Image = image;
        Owner = owner;
        Time = time;
        Address = address;
        Name = name;
        Password = password;
        RegNo = regNo;
    }
    public String getImage() {
        return Image;
    }

    public String getOwner() {
        return Owner;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTime() {
        return Time;
    }


    public MessUser(String address, String name, String password, String regno) {
        Address = address;
        Name = name;
        Password = password;
        RegNo = regno;
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
