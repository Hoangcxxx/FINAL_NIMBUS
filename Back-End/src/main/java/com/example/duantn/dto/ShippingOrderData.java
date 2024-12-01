package com.example.duantn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ShippingOrderData {

    @JsonProperty("from_province_id")
    private int provinceId; // Mã tỉnh gửi hàng

    @JsonProperty("from_district_id")
    private int districtId; // Mã quận gửi hàng

    @JsonProperty("from_ward_id")
    private int wardId;     // Mã phường gửi hàng

    @JsonProperty("to_province_id")
    private int toProvinceId; // Mã tỉnh nhận hàng

    @JsonProperty("to_district_id")
    private int toDistrictId; // Mã quận nhận hàng

    @JsonProperty("to_ward_code")
    private String toWardCode;     // Mã phường nhận hàng

    @JsonProperty("weight")
    private int weight;    // Trọng lượng gói hàng (gram)

    @JsonProperty("length")
    private int length;    // Chiều dài gói hàng (cm)

    @JsonProperty("width")
    private int width;     // Chiều rộng gói hàng (cm)

    @JsonProperty("height")
    private int height;    // Chiều cao gói hàng (cm)

    @JsonProperty("service_id")
    private int serviceId; // Mã dịch vụ

    @JsonProperty("insurance_value")
    private Integer insuranceValue; // Giá trị bảo hiểm (nếu có, có thể để null)

    @JsonProperty("cod_failed_amount")
    private Integer codFailedAmount; // Số tiền thu hộ (nếu có, có thể để null)

    @JsonProperty("coupon")
    private String coupon; // Mã giảm giá (nếu có, có thể để null)

}