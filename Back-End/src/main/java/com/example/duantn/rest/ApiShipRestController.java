package com.example.duantn.rest;

import com.example.duantn.city.District;
import com.example.duantn.city.Ward;
import com.example.duantn.reponse.ResponseDTO;
import com.example.duantn.request.RequestDTO;
import com.example.duantn.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/ship")
public class ApiShipRestController {

    @Autowired
    private GHNService ghnService;

    // Tính phí giao hàng
    @PostMapping("/calculate-fee")
    public ResponseDTO calculateFee(@RequestBody RequestDTO requestDTO) {
        return ghnService.getShippingFee(requestDTO);
    }

    // Lấy danh sách quận/huyện
    @GetMapping("/districts/{provinceId}")
    public List<District> getDistricts(@PathVariable int provinceId) {
        return ghnService.getDistricts(provinceId);
    }

    // Lấy danh sách phường/xã
    @GetMapping("/wards/{districtId}")
    public List<Ward> getWards(@PathVariable int districtId) {
        return ghnService.getWards(districtId);
    }
}
