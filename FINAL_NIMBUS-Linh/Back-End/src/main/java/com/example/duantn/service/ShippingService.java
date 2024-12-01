package com.example.duantn.service;

import com.example.duantn.dto.ShippingOrderData;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShippingService {

    private final String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2";
    private final String token = "337cc41f-844d-11ef-8e53-0a00184fe694"; // Token from your config
    private final RestTemplate restTemplate;

    public ShippingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to calculate shipping fee
    public ResponseEntity<String> getShippingFee(ShippingOrderData orderData) {
        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", token);

        HttpEntity<ShippingOrderData> entity = new HttpEntity<>(orderData, headers);

        // API URL for shipping fee
        String url = apiUrl + "/shipping-order/fee";

        // Send the POST request to get the shipping fee
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
