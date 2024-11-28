package com.example.duantn.city;

import com.fasterxml.jackson.annotation.JsonProperty;

public class District {
    @JsonProperty("DistrictID")
    private int id;

    @JsonProperty("DistrictName")
    private String name;

    // Getter và Setter
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
