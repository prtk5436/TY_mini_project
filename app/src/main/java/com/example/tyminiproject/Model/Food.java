package com.example.tyminiproject.Model;

public class Food {
    String Name, Image, Price, Description, Discount, MenuId, MessName;

    public Food(String name, String image, String price, String description, String discount, String menuId, String messName) {
        Name = name;
        Image = image;
        Price = price;
        Description = description;
        Discount = discount;
        MenuId = menuId;
        MessName = messName;
    }

    public String getMessName() {
        return MessName;
    }

    public void setMessName(String messName) {
        MessName = messName;
    }

    public Food() {
    }

    public Food(String name, String image, String price, String description, String discount, String menuId) {
        Name = name;
        Image = image;
        Price = price;
        Description = description;
        Discount = discount;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
