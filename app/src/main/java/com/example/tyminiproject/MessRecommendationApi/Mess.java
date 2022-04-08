package com.example.tyminiproject.MessRecommendationApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mess {
    @SerializedName("Categories")
    @Expose
    private String categories;
    @SerializedName("Mess_Id")
    @Expose
    private String messId;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
