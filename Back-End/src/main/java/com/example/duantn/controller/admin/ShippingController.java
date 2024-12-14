package com.example.duantn.controller.client;

import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.PhiVanChuyen;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.PhiVanChuyenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/nguoi_dung/test")
public class ShippingController {

    private final PhiVanChuyenRepository phiVanChuyenRepository;
    private final HoaDonRepository hoaDonRepository;
    private final DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    public ShippingController(PhiVanChuyenRepository phiVanChuyenRepository,
                              HoaDonRepository hoaDonRepository,
                              DiaChiVanChuyenRepository diaChiVanChuyenRepository) {
        this.phiVanChuyenRepository = phiVanChuyenRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.diaChiVanChuyenRepository = diaChiVanChuyenRepository;
    }

    @GetMapping("/shipping-fee/{cityCode}/{districtCode}/{wardCode}")
    public ResponseEntity<Integer> getShippingFee(@PathVariable Integer cityCode,
                                                  @PathVariable Integer districtCode,
                                                  @PathVariable Integer wardCode) {
        Integer fee = 22000; // Mặc định phí 22k


        return ResponseEntity.ok(fee);
    }
}