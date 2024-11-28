package com.example.duantn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDTO {

    @JsonProperty("ToProvinceID")
    private int provinceId;

    @JsonProperty("ToDistrictID")
    private int districtId;  // Đảm bảo giá trị này được gán hợp lệ

    @JsonProperty("ToWardCode")
    private int wardId;

    private int weight;

}
