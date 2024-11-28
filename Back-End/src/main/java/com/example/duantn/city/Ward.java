package com.example.duantn.city;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ward {
    @JsonProperty("WardCode")
    private String code;

    @JsonProperty("WardName")
    private String name;

    // Getter và Setter
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
