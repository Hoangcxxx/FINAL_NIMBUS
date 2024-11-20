package com.example.duantn.controller.client;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping( "/api/Test/")
public class LocationController {

    private final RestTemplate restTemplate;

    public LocationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Endpoint to get cities (provinces)
    @GetMapping("/api/cities")
    public List<?> getCities() {
        String url = "https://provinces.open-api.vn/api/?depth=1";  // Example external API
        return restTemplate.getForObject(url, List.class);
    }

    // Endpoint to get districts for a specific city (province)
    @GetMapping("/api/districts/{cityId}")
    public List<?> getDistricts(@PathVariable String cityId) {
        String url = "https://provinces.open-api.vn/api/p/" + cityId + "?depth=2";
        return restTemplate.getForObject(url, List.class);
    }

    // Endpoint to get wards for a specific district
    @GetMapping("/api/wards/{districtId}")
    public List<?> getWards(@PathVariable String districtId) {
        String url = "https://provinces.open-api.vn/api/d/" + districtId + "?depth=2";
        return restTemplate.getForObject(url, List.class);
    }
}
