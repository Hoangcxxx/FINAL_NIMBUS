package com.example.duantn.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

@Service
public class addressSerice {
    private final String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data"; // Base URL for GHN API
    private final String token = "337cc41f-844d-11ef-8e53-0a00184fe694"; // Token from your config
    private final RestTemplate restTemplate;

    public addressSerice(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to get list of provinces from GHN
    public ResponseEntity<String> getProvinces() {
        String url = apiUrl + "/province";

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        // Create request entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send GET request to GHN API to get provinces
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Method to get list of districts by province ID from GHN
    public ResponseEntity<String> getDistricts(int provinceId) {
        String url = apiUrl + "/district?province_id=" + provinceId;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        // Create request entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send GET request to GHN API to get districts by provinceId
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Method to get list of wards by district ID from GHN
    public ResponseEntity<String> getWards(int districtId) {
        String url = apiUrl + "/ward?district_id=" + districtId;

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        // Create request entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send GET request to GHN API to get wards by districtId
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}