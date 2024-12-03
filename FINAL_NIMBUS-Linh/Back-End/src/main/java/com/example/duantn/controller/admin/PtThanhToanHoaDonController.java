package com.example.duantn.controller.admin;

import com.example.duantn.entity.*;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.PhuongThucThanhToanService;
import com.example.duantn.service.PtThanhToanHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/hoa-don")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PtThanhToanHoaDonController {

    @Autowired
    private PtThanhToanHoaDonService ptThanhToanHoaDonService;
    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;
    @Autowired
    private HoaDonService hoaDonService;

    @PostMapping("/{id}/thanh-toan")
    public ResponseEntity<PtThanhToanHoaDon> createPaymentMethod(@PathVariable Integer id, @RequestBody PtThanhToanHoaDon paymentMethod) {
        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon == null) {
            return ResponseEntity.notFound().build();
        }

        PhuongThucThanhToan phuongThuc = phuongThucThanhToanService.findById(paymentMethod.getPhuongThucThanhToan().getId());
        if (phuongThuc == null) {
            return ResponseEntity.badRequest().body(null);
        }
        String maHoaDon = hoaDon.getMaHoaDon();

        paymentMethod.setPhuongThucThanhToan(phuongThuc);
        paymentMethod.setHoaDon(hoaDon);
        paymentMethod.setTrangThai("Hoàn Thành");

        PtThanhToanHoaDon createdPaymentMethod = ptThanhToanHoaDonService.createPaymentMethod(paymentMethod);

        return new ResponseEntity<>(createdPaymentMethod, HttpStatus.CREATED);
    }
}
