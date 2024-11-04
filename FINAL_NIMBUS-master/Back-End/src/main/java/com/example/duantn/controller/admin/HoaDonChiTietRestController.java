package com.example.duantn.controller.admin;

import com.example.duantn.dto.HoaDonChiTietDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoa-don-chi-tiet")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HoaDonChiTietRestController {
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @PostMapping("/create")
    public ResponseEntity<List<HoaDonChiTietDTO>> createMultipleHoaDonChiTiet(@RequestBody List<HoaDonChiTietDTO> dtoList,@RequestParam Integer userId) {
        if (dtoList == null || dtoList.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<HoaDonChiTietDTO> createdHoaDonChiTietList = hoaDonChiTietService.createMultipleHoaDonChiTiet(dtoList,userId);
        return ResponseEntity.ok(createdHoaDonChiTietList);
    }

}
