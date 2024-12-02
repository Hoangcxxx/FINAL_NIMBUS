package com.example.duantn.controller;

import com.example.duantn.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/ghn")
public class GHNController {

    @Autowired
    private GHNService ghnService;

    // API tính phí vận chuyển
    @PostMapping("/calculate-shipping-fee")
    public Map<String, Object> calculateShippingFee(@RequestBody Map<String, Object> request) {
        Integer fromProvinceId = (Integer) request.get("from_province_id");
        Integer fromDistrictId = (Integer) request.get("from_district_id");
        Integer toProvinceId = (Integer) request.get("to_province_id");
        Integer toDistrictId = (Integer) request.get("to_district_id");
        String toWardCode = (String) request.get("to_ward_code");
        Integer weight = (Integer) request.get("weight");
        Integer length = (Integer) request.get("length");
        Integer width = (Integer) request.get("width");
        Integer height = (Integer) request.get("height");
        Integer serviceId = (Integer) request.get("service_id");
        Integer insuranceValue = (Integer) request.get("insurance_value");

        // Gọi dịch vụ tính phí vận chuyển
        return ghnService.calculateShippingFee(fromProvinceId, fromDistrictId, toProvinceId, toDistrictId, toWardCode,
                weight, length, width, height, serviceId, insuranceValue);
    }
}
