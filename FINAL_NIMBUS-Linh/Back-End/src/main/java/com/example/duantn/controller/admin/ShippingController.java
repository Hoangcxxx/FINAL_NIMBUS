package com.example.duantn.controller.admin;

import com.example.duantn.dto.ShippingOrderData;
import com.example.duantn.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
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

