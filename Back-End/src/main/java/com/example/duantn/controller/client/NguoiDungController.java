package com.example.duantn.controller.client;

import com.example.duantn.dto.LoginRequest;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.DangNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class NguoiDungController {
    @Autowired
    private DangNhapService dangNhapService;

    // Phương thức đăng ký
    @PostMapping("/dang_ky")
    public ResponseEntity<NguoiDung> dangKy(@RequestBody NguoiDung nguoiDung) {
        try {
            NguoiDung nguoiDungMoi = dangNhapService.dangKy(nguoiDung);
            return ResponseEntity.ok(nguoiDungMoi);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Phương thức đăng nhập
    @PostMapping("/dang_nhap")
    public ResponseEntity<NguoiDung> dangNhap(@RequestBody LoginRequest loginRequest) {
        try {
            NguoiDung nguoiDung = dangNhapService.dangNhap(loginRequest.getEmail(), loginRequest.getMatKhau());
            return ResponseEntity.ok(nguoiDung);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
