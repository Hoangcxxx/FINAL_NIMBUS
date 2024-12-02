package com.example.duantn.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingOrderData {


    @JsonProperty("from_province_id")
    private Integer fromProvinceId;

    @JsonProperty("from_district_id")
    private Integer fromDistrictId;

    @JsonProperty("from_ward_id")
    private Integer fromWardId;

    @JsonProperty("to_province_id")
    private Integer toProvinceId;

    @JsonProperty("to_district_id")
    private Integer toDistrictId;

    @JsonProperty("to_ward_id")
    private Integer toWardId;


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
