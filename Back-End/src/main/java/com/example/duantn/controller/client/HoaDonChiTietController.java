package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonCTDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.service.HoaDonCTSevice;
import com.example.duantn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoa-don-chi-tiet")
public class HoaDonChiTietController {
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @PostMapping
    public ResponseEntity<HoaDonChiTiet> taoChiTietHoaDon(@RequestBody HoaDonChiTiet hoaDonChiTiet) {
        HoaDonChiTiet chiTietMoi = hoaDonChiTietService.taoChiTietHoaDon(hoaDonChiTiet);
        return ResponseEntity.ok(chiTietMoi);
    }

    @GetMapping("/hoa-don/{hoaDonId}")
    public ResponseEntity<List<HoaDonChiTiet>> layChiTietTheoHoaDon(@PathVariable Long hoaDonId) {
        return ResponseEntity.ok(hoaDonChiTietService.layChiTietTheoHoaDon(hoaDonId));
    }
}
