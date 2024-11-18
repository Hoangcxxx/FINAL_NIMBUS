package com.example.duantn.service;

import com.example.duantn.dto.DeliveryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class GhnService {

    @Value("${ghn.api.url}")
    private String apiUrl;

    @Value("${ghn.api.key}")
    private String apiKey;  // Token của bạn

    private final RestTemplate restTemplate;

    public GhnService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // API GHN để tạo đơn hàng giao hàng nhanh
    public String createDelivery(DeliveryRequest deliveryRequest) {
        // Cấu hình thông tin gửi yêu cầu API GHN
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", apiKey);  // Sử dụng token từ cấu hình
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo body request
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("full_name", deliveryRequest.getHoTen());
        map.add("phone", deliveryRequest.getSoDienThoai());
        map.add("email", deliveryRequest.getEmail());
        map.add("address", deliveryRequest.getDiaChi());
        map.add("ward", deliveryRequest.getPhuong());
        map.add("district", deliveryRequest.getQuan());
        map.add("city", deliveryRequest.getTinh());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        // Gửi yêu cầu POST đến API GHN
        String response = restTemplate.exchange(
                apiUrl + "/order/create",
                HttpMethod.POST,
                entity,
                String.class
        ).getBody();

        return response;
    }
}