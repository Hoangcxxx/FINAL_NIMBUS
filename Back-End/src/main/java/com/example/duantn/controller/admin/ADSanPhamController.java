package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
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
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("moTa", row[2]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[3]); // Cập nhật chỉ số cho trạng thái
        map.put("trangThai", row[4]); // Cập nhật chỉ số cho trạng thái
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

        // Set default values
        Boolean trangThai = true; // Set status to true
        Date ngayTao = new Date(); // Current date
        Date ngayCapNhat = new Date(); // Current date

        sanPhamService.addSanPham(idDanhMuc, tenSanPham, moTa, ngayTao, ngayCapNhat, trangThai);
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

    @PostMapping("/multiple")
    public ResponseEntity<List<SanPhamChiTiet>> createMultiple(@RequestBody List<SanPhamChiTiet> sanPhamChiTietList) throws IOException {
        if (sanPhamChiTietList.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        SanPhamChiTiet firstChiTiet = sanPhamChiTietList.get(0);
        if (firstChiTiet.getSanPham() == null || firstChiTiet.getSanPham().getIdSanPham() == null) {
            return ResponseEntity.badRequest().body(null); // Handle null cases
        }

        Integer idSanPham = firstChiTiet.getSanPham().getIdSanPham();
        List<SanPhamChiTiet> savedProducts = sanPhamChiTietService.createMultiple(sanPhamChiTietList, idSanPham);
        return ResponseEntity.ok(savedProducts);
    }





}
