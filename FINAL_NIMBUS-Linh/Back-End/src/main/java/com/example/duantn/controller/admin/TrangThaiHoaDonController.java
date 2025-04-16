package com.example.duantn.controller.admin;

import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.service.TrangThaiHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/trang_thai_hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class TrangThaiHoaDonController {

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

//    @PostMapping("/create/{id_hoa_don}")
//    public ResponseEntity<TrangThaiHoaDon> createTrangThaiHoaDon(@PathVariable Integer id_hoa_don) {
//        return ResponseEntity.ok(trangThaiHoaDonService.createTrangThaiHoaDon(id_hoa_don));
//    }
}
