package com.example.duantn.controller;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoadon")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    // Lấy danh sách hóa đơn theo ID người dùng
    @GetMapping("/user/{idNguoiDung}")
    public ResponseEntity<List<HoaDonDTO>> getHoaDonByNguoiDungId(@PathVariable Integer idNguoiDung) {
        List<HoaDonDTO> hoaDonDTOs = hoaDonService.layHoaDonTheoNguoiDungId(idNguoiDung);
        return ResponseEntity.ok(hoaDonDTOs);
    }
}
