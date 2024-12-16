package com.example.duantn.controller.client;

import com.example.duantn.service.addressSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung/address")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class AddressController {
    @Autowired
    private addressSerice addressSerice;

    // Endpoint to get all provinces
    @GetMapping("/provinces")
    public ResponseEntity<String> getProvinces() {
        return addressSerice.getProvinces();
    }

    // Endpoint to get districts by provinceId
    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<String> getDistricts(@PathVariable int provinceId) {
        return addressSerice.getDistricts(provinceId);
    }

    // Endpoint to get wards by districtId
    @GetMapping("/wards/{districtId}")
    public ResponseEntity<String> getWards(@PathVariable int districtId) {
        return addressSerice.getWards(districtId);
    }

}