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
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;
    @GetMapping("/hien_thi")
    public ResponseEntity<List<Map<String, Object>>> hienthi() {
        List<Object[]> results = sanPhamService.getAllSanPhams();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Object[] result : results) {
            if (result.length < 5) continue; // Kiểm tra số lượng trường

            Map<String, Object> responseMap = new HashMap<>();

            // Lấy thông tin sản phẩm từ Object[]
            Integer idSanPham = (Integer) result[0];
            String tenSanPham = (String) result[1];
            String moTa = (String) result[2];
            BigDecimal giaBan = (BigDecimal) result[3];
            String urlAnh = (String) result[4];

            // Thêm thông tin vào map
            responseMap.put("idSanPham", idSanPham);
            responseMap.put("tenSanPham", tenSanPham);
            responseMap.put("moTa", moTa);
            responseMap.put("giaBan", giaBan.toString());
            responseMap.put("urlAnh", urlAnh);

            responseList.add(responseMap);
        }

        return ResponseEntity.ok(responseList);
    }
    @GetMapping
    public List<Map<String, Object>> getAllSanPhams() {
        List<Object[]> results = sanPhamService.getAllSanPhams();
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tenSanPham", row[0]);
            map.put("soLuong", row[1]);
            map.put("tenDanhMuc", row[2]);
            map.put("tenLoaiVoucher", row[3]);
            map.put("trangThai", row[4]);
            map.put("mauSac", row[5]);
            map.put("kichThuoc", row[6]);
            map.put("chatLieu", row[7]);
            return map;
        }).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getSanPham(@PathVariable Integer id) {
        SanPham sanPham = sanPhamService.getSanPhamById(id);
        return sanPham != null ? ResponseEntity.ok(sanPham) : ResponseEntity.notFound().build();
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
