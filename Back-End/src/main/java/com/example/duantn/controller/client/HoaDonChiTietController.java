package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonChiTietDTO;

import com.example.duantn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/hoadonchitiet")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class HoaDonChiTietController {

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @PostMapping("/them")
    public ResponseEntity<String> placeOrderDetail(@RequestBody HoaDonChiTietDTO hoaDonChiTietDTO) {
        hoaDonChiTietService.createHoaDonChiTiet(hoaDonChiTietDTO);
        return ResponseEntity.ok("Hóa đơn chi tiết đã được thêm thành công!");
    }

//    @GetMapping("/hoadon/{idHoaDon}")
//    public ResponseEntity<List<HoaDonChiTiet>> getAllByHoaDonId(@PathVariable Integer idHoaDon) {
//        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietService.getAllByHoaDonId(idHoaDon);
//        return ResponseEntity.ok(hoaDonChiTietList);
//    }
}
