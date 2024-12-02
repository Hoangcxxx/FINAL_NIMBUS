package com.example.duantn.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GHNService {

    private static final String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2";
    private static final String TOKEN = "337cc41f-844d-11ef-8e53-0a00184fe694";

    private final RestTemplate restTemplate;

    public GHNService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Hàm tính phí vận chuyển
    public Map<String, Object> calculateShippingFee(Integer fromProvinceId, Integer fromDistrictId, Integer toProvinceId,
                                                    Integer toDistrictId, String toWardCode, Integer weight, Integer length,
                                                    Integer width, Integer height, Integer serviceId, Integer insuranceValue) {
        String url = API_URL + "/shipping-order/fee";

        // Tạo headers cho yêu cầu
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Token", TOKEN);

        // Tạo body yêu cầu
        Map<String, Object> body = new HashMap<>();
        body.put("from_province_id", fromProvinceId);    // Mã tỉnh gửi hàng
        body.put("from_district_id", fromDistrictId);    // Mã quận gửi hàng
        body.put("to_province_id", toProvinceId);        // Mã tỉnh nhận hàng
        body.put("to_district_id", toDistrictId);        // Mã quận nhận hàng
        body.put("to_ward_code", toWardCode);            // Mã phường nhận hàng
        body.put("weight", weight);                      // Trọng lượng gói hàng
        body.put("length", length);                      // Chiều dài gói hàng
        body.put("width", width);                        // Chiều rộng gói hàng
        body.put("height", height);                      // Chiều cao gói hàng
        body.put("service_id", serviceId);               // ID loại dịch vụ
        body.put("insurance_value", insuranceValue);     // Giá trị bảo hiểm

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu tới API GHN
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Kiểm tra phản hồi
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();  
        } else {
            throw new RuntimeException("Lỗi khi tính phí vận chuyển: " + response.getStatusCode());
        }
    }
}
