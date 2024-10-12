package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPham;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ad_san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;
    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("tenSanPham", row[0]);
        map.put("moTa", row[1]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[2]); // Cập nhật chỉ số cho trạng thái
        return map;
    }
    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }
    // Thêm sản phẩm
    @PostMapping
    public ResponseEntity<Void> createSanPham(@RequestBody Map<String, Object> requestBody) {
        Integer idDanhMuc = (Integer) requestBody.get("idDanhMuc");
        String tenSanPham = (String) requestBody.get("tenSanPham");
        String moTa = (String) requestBody.get("moTa");
        sanPhamService.addSanPham(idDanhMuc, tenSanPham, moTa);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<SanPham> updateSanPham(@PathVariable Integer id, @RequestBody SanPham sanPham) {
        sanPham.setIdSanPham(id);
        SanPham updatedSanPham = sanPhamService.updateSanPham(id, sanPham);
        return new ResponseEntity<>(updatedSanPham, HttpStatus.OK);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer id) {
        sanPhamService.deleteSanPham(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Lấy tất cả sản phẩm (nếu cần)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhamAD();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }

}
