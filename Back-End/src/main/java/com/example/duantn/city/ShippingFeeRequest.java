package com.example.duantn.city;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingFeeRequest {
    private Integer fromDistrictId;
    private Integer toDistrictId;
    private String toWardCode;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer insuranceValue;
    private Integer serviceId;
}
