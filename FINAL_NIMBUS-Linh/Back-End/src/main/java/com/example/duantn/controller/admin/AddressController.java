package com.example.duantn.controller.admin;

import com.example.duantn.service.addressSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/address")
public class AddressController {

        private final addressSerice addressSerice;
    @Autowired
    public AddressController(com.example.duantn.service.addressSerice addressSerice) {
            this.addressSerice = addressSerice;
        }

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
