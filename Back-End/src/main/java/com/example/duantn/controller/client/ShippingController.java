package com.example.duantn.controller.client;


import com.example.duantn.config.ShippingOrderData;
import com.example.duantn.rest.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung/shipping")
public class ShippingController {

    private final ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping("/get-shipping-fee")
    public ResponseEntity<String> getShippingFee(@RequestBody ShippingOrderData orderData) {
        return shippingService.getShippingFee(orderData);
    }
}

