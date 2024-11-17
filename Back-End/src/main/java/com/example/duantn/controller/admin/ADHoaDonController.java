package com.example.duantn.controller.admin;

import com.example.duantn.service.HoaDonChiTietService;
import com.example.duantn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/admin/hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADHoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    private Map<String, Object> mapHoaDon(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("maHoaDon", row[0]);
        map.put("tenNguoiDung", row[1]);
        map.put("ngayTao", row[2]);
        map.put("thanhTien", row[3]);
        map.put("trangThaiHoaDon", row[4]);
        return map;
    }

    private List<Map<String, Object>> mapHoaDons(List<Object[]> results) {
        return results.stream().map(this::mapHoaDon).collect(Collectors.toList());
    }


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllThongKe() {
        List<Object[]> hoaDons = hoaDonService.getAllHoaDon();
        List<Map<String, Object>> filteredProducts = mapHoaDons(hoaDons);
        return ResponseEntity.ok(filteredProducts);
    }
}
