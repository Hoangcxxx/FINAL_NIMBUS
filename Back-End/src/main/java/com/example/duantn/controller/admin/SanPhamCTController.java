package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham-chi-tiet")
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class SanPhamCTController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping
    public ResponseEntity<List<SanPhamChiTiet>> getAllSanPhamCT() {
        List<SanPhamChiTiet> sanPhamCT = sanPhamChiTietRepository.findAll();
        return ResponseEntity.ok(sanPhamCT);
    }

}
