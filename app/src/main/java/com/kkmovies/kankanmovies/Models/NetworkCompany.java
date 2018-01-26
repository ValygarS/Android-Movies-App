package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;

public class NetworkCompany {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public NetworkCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
