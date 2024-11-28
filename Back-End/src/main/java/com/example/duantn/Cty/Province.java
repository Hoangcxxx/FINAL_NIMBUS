package com.example.duantn.Cty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Province {
    @JsonProperty("ProvinceID")
    private int id;

    @JsonProperty("ProvinceName")
    private String name;
}
