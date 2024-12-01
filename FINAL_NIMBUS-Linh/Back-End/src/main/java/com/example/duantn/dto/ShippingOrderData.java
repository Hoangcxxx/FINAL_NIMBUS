package com.example.duantn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    // Getters and Setters

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public int getToProvinceId() {
        return toProvinceId;
    }

    public void setToProvinceId(int toProvinceId) {
        this.toProvinceId = toProvinceId;
    }

    public int getToDistrictId() {
        return toDistrictId;
    }

    public void setToDistrictId(int toDistrictId) {
        this.toDistrictId = toDistrictId;
    }

    public String getToWardCode() {
        return toWardCode;
    }

    public void setToWardCode(String toWardCode) {
        this.toWardCode = toWardCode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getInsuranceValue() {
        return insuranceValue;
    }

    public void setInsuranceValue(Integer insuranceValue) {
        this.insuranceValue = insuranceValue;
    }

    public Integer getCodFailedAmount() {
        return codFailedAmount;
    }

    public void setCodFailedAmount(Integer codFailedAmount) {
        this.codFailedAmount = codFailedAmount;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
