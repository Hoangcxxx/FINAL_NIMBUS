package com.example.duantn.controller.client;

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
@RequestMapping("/api/san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("soLuong", row[2]);
        map.put("giaBan", row[3]);
        map.put("moTa", row[4]);
        map.put("tenDanhMuc", row[5]);
        map.put("tenLoaiVoucher", row[6]);
        map.put("trangThai", row[7]);
        map.put("mauSac", row[8]);
        map.put("kichThuoc", row[9]);
        map.put("chatLieu", row[10]);
        map.put("urlAnh", row[11]);  // Hình ảnh đầu tiên đã được lọc
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

    @PostMapping
    public ResponseEntity<SanPham> createSanPham(@RequestBody SanPham sanPham) {
        return new ResponseEntity<>(sanPhamService.createSanPham(sanPham), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPham> updateSanPham(@PathVariable Integer id, @RequestBody SanPham sanPham) {
        return ResponseEntity.ok(sanPhamService.updateSanPham(id, sanPham));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.noContent().build();
    }
}
