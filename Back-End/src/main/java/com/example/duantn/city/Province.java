package com.example.duantn.city;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Province {
    @JsonProperty("ProvinceID")
    private int id;

    @JsonProperty("ProvinceName")
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
