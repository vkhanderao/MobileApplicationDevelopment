package com.akshay.newsgateway1;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Sources implements Serializable {

    private String id;
    private String name;
    private String category;

    public Sources(){

    }
    public Sources(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    public String toString() {
        return name;
    }
}
