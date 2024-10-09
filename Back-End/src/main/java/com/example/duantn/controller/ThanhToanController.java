package com.example.duantn.controller;


import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.PhuongThucThanhToan;
import com.example.duantn.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/thanhtoan")
public class ThanhToanController {

    @Autowired
    private PhuongThucThanhToanRepository ghrp;



    @GetMapping("/hienthi")
    public ResponseEntity<List<PhuongThucThanhToan>> getAllGioHang() {
        List<PhuongThucThanhToan> gh = ghrp.findAll();
        return ResponseEntity.ok().body(gh);
    }
}
