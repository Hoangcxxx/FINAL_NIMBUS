package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPham;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public List<Map<String, Object>> getAllSanPhams() {
        List<Object[]> results = sanPhamService.getAllSanPhams();
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tenSanPham", row[0]);
            map.put("soLuong", row[1]);
            map.put("giaBan", row[2]);
            map.put("moTa", row[3]);
            map.put("tenDanhMuc", row[4]);
            map.put("tenLoaiVoucher", row[5]);
            map.put("trangThai", row[6]);
            map.put("mauSac", row[7]);
            map.put("kichThuoc", row[8]);
            map.put("chatLieu", row[9]);
            map.put("urlAnh", row[10]);
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/findSanPham/{id}")
    public ResponseEntity<SanPham> getSanPham(@PathVariable Integer id) {
        SanPham sanPham = sanPhamService.getSanPhamById(id);
        return sanPham != null ? ResponseEntity.ok(sanPham) : ResponseEntity.notFound().build();
    }

    @GetMapping("/findDanhMuc/{idDanhMuc}") // Thêm endpoint mới
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDanhMuc(@PathVariable Integer idDanhMuc) {
        List<Object[]> results = sanPhamService.getSanPhamsByDanhMuc(idDanhMuc);

        List<Map<String, Object>> sanPhams = results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tenSanPham", row[0]);
            map.put("soLuong", row[1]);
            map.put("giaBan", row[2]);
            map.put("moTa", row[3]);
            map.put("tenDanhMuc", row[4]);
            map.put("tenLoaiVoucher", row[5]);
            map.put("trangThai", row[6]);
            map.put("mauSac", row[7]);
            map.put("kichThuoc", row[8]);
            map.put("chatLieu", row[9]);
            map.put("urlAnh", row[10]); // Thêm trường hình ảnh sản phẩm
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(sanPhams);
    }



    @PostMapping
    public ResponseEntity<SanPham> createSanPham(@RequestBody SanPham sanPham) {
        return new ResponseEntity<>(sanPhamService.createSanPham(sanPham), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPham> updateSanPham(@PathVariable Integer id, @RequestBody SanPham sanPham) {
        SanPham updatedSanPham = sanPhamService.updateSanPham(id, sanPham);
        return ResponseEntity.ok(updatedSanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.noContent().build();
    }
}
