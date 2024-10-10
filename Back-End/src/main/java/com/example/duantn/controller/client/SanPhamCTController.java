package com.example.duantn.controller.client;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/san_pham_chi_tiet")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamCTController {
    @Autowired
    private SanPhamChiTietService service;

    private Map<String, Object> getAllSanPhamChiTiet(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]); // Id_san_pham
        map.put("tenSanPham", row[1]); // ten_san_pham
        map.put("giaBan", row[2]); // gia_ban
        map.put("moTa", row[3]); // mo_ta_spct
        return map;
    }
    private Map<String, Object> getAllMauSacChiTiet(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]); // Id_san_pham
        map.put("tenMauSac", row[1]); // ten_mau_sac
        return map;
    }
    private List<Map<String, Object>> mapSanPhamCTs(List<Object[]> results) {
        return results.stream().map(this::getAllSanPhamChiTiet).collect(Collectors.toList());
    }

    @GetMapping("/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getById(@PathVariable Integer idSanPhamCT) {
        List<Object[]> sanPhamChiTiet = service.getById(idSanPhamCT);
        if (sanPhamChiTiet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> mappedList = mapSanPhamCTs(sanPhamChiTiet);
        return ResponseEntity.ok(mappedList);
    }
    @GetMapping("/mau_sac/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getMauSacById(@PathVariable Integer idSanPhamCT) {
        List<Object[]> sanPhamChiTiet = service.getMauSacById(idSanPhamCT);
        if (sanPhamChiTiet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> mappedList = sanPhamChiTiet.stream()
                .map(this::getAllMauSacChiTiet)
                .collect(Collectors.toList());

        return ResponseEntity.ok(mappedList);
    }

}
