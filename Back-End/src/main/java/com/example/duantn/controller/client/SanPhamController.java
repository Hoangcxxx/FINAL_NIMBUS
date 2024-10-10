package com.example.duantn.controller.client;

import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("tenDanhMuc", row[2]);
        map.put("giaBan", row[3]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[4]);     // Cập nhật chỉ số cho mô tả
        map.put("trangThai", row[5]); // Cập nhật chỉ số cho trạng thái
        map.put("urlAnh", row[6]);   // Cập nhật chỉ số cho URL ảnh
        map.put("thuTu", row[7]);    // Cập nhật chỉ số cho thứ tự
        return map;
    }
    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhams();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }


    @GetMapping("/findSanPham/{idSanPham}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamById(@PathVariable String idSanPham) {
        List<Object[]> sanPhamDetails = sanPhamService.getSanPhamById(idSanPham);
        if (sanPhamDetails.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Trả về tất cả các sản phẩm chi tiết
        List<Map<String, Object>> productDetails = mapSanPhams(sanPhamDetails);
        return ResponseEntity.ok(productDetails);
    }


    @GetMapping("/findDanhMuc/{idDanhMuc}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDanhMuc(@PathVariable Integer idDanhMuc) {
        return ResponseEntity.ok(mapSanPhams(sanPhamService.getSanPhamsByDanhMuc(idDanhMuc)));
    }


}
