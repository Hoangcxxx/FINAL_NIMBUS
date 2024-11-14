//package com.example.duantn.controller.client;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.http.HttpEntity;
//
//@RestController
//@RequestMapping("/api/giao-hang")
//public class GhnController {
//    @Value("${ghn.token}")
//    private String ghnToken;  // Lấy token từ application.properties
//
//    private final RestTemplate restTemplate;
//
//    public GhnController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    // Lấy danh sách tỉnh từ API GHN
//    @GetMapping("/api/ghn/provinces")
//    public ResponseEntity<Object> getProvinces() {
//        String url = "https://api.ghn.vn/v1/apiv2/province";
//        try {
//            // Add token if needed in headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + ghnToken);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getResponseBodyAsString());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
//        }
//    }
//
//    // Lấy danh sách huyện theo tỉnh
//    @GetMapping("/api/ghn/districts/{provinceId}")
//    public ResponseEntity<Object> getDistricts(@PathVariable("provinceId") String provinceId) {
//        String url = String.format("https://api.ghn.vn/v1/apiv2/district?province_id=%s", provinceId);
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + ghnToken);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getResponseBodyAsString());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
//        }
//    }
//
//    // Lấy danh sách xã theo huyện
//    @GetMapping("/api/ghn/wards/{districtId}")
//    public ResponseEntity<Object> getWards(@PathVariable("districtId") String districtId) {
//        String url = String.format("https://api.ghn.vn/v1/apiv2/ward?district_id=%s", districtId);
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + ghnToken);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
//
//            return ResponseEntity.ok(response.getBody());
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getResponseBodyAsString());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
//        }
//    }
//}
