package com.example.duantn.controller.client;

import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.service.TrangThaiHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trang-thai-hoa-don")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class TrangThaiHoaDonController {

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

    // Lấy tất cả trạng thái hóa đơn
    @GetMapping
    public ResponseEntity<List<TrangThaiHoaDon>> getAllTrangThaiHoaDon() {
        List<TrangThaiHoaDon> trangThaiHoaDons = trangThaiHoaDonService.getAllTrangThaiHoaDon();
        return new ResponseEntity<>(trangThaiHoaDons, HttpStatus.OK);
    }

    // Lấy trạng thái hóa đơn theo ID
    @GetMapping("/{id}")
    public ResponseEntity<TrangThaiHoaDon> getTrangThaiHoaDonById(@PathVariable Integer id) {
        Optional<TrangThaiHoaDon> trangThaiHoaDon = trangThaiHoaDonService.getTrangThaiHoaDonById(id);
        return trangThaiHoaDon.map(
                ResponseEntity::ok
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Tạo mới trạng thái hóa đơn
    @PostMapping
    public ResponseEntity<TrangThaiHoaDon> createTrangThaiHoaDon(@RequestBody TrangThaiHoaDon trangThaiHoaDon) {
        TrangThaiHoaDon createdTrangThaiHoaDon = trangThaiHoaDonService.createTrangThaiHoaDon(trangThaiHoaDon);
        return new ResponseEntity<>(createdTrangThaiHoaDon, HttpStatus.CREATED);
    }

    // Cập nhật trạng thái hóa đơn
    @PutMapping("/{id}")
    public ResponseEntity<TrangThaiHoaDon> updateTrangThaiHoaDon(@PathVariable Integer id, @RequestBody TrangThaiHoaDon trangThaiHoaDon) {
        TrangThaiHoaDon updatedTrangThaiHoaDon = trangThaiHoaDonService.updateTrangThaiHoaDon(id, trangThaiHoaDon);
        return new ResponseEntity<>(updatedTrangThaiHoaDon, HttpStatus.OK);
    }

    // Xóa trạng thái hóa đơn
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrangThaiHoaDon(@PathVariable Integer id) {
        trangThaiHoaDonService.deleteTrangThaiHoaDon(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
