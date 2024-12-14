package com.example.duantn.controller.admin;

import com.example.duantn.dto.LichSuThanhToanRequest;
import com.example.duantn.service.LichSuThanhToanService;
import com.example.duantn.entity.LichSuThanhToan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/lich_su_thanh_toan")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class LichSuThanhToanController {
    @Autowired
    private LichSuThanhToanService lichSuThanhToanService;
    private Map<String, Object> mapRow(Object[] row, String... keys) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], row[i]);
        }
        return map;
    }

    private <T> ResponseEntity<List<Map<String, Object>>> getResponse(List<Object[]> results, String... keys) {
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> mappedList = results.stream()
                .map(row -> mapRow(row, keys))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mappedList);
    }
    @GetMapping("/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getLichSuHoaDon(@PathVariable("idHoaDon") Integer idHoaDon) {
        return getResponse(lichSuThanhToanService.getlichSuHoaDonByidHoaDon(idHoaDon), "idLichSuThanhToan","soTienThanhToan", "ngayGiaoDich","trangThaiThanhToan","moTa", "tenNhanVien");
    }
    @PostMapping("/create")
    public LichSuThanhToan createLichSuThanhToan(@RequestBody Map<String, Object> payload) {
        Integer idHoaDon = (Integer) payload.get("idHoaDon");
        Integer idNguoiDung = (Integer) payload.get("idNguoiDung");
        BigDecimal soTienThanhToan = new BigDecimal(payload.get("soTienThanhToan").toString());
        return lichSuThanhToanService.createLichSuThanhToan(idHoaDon, idNguoiDung, soTienThanhToan);
    }

    @PutMapping("/update/{idHoaDon}")
    public ResponseEntity<Map<String, Object>> updateLichSuThanhToan(
            @PathVariable("idHoaDon") Integer idHoaDon, // ID của hóa đơn
            @RequestBody LichSuThanhToanRequest lichSuThanhToanRequest) {

        List<LichSuThanhToan> updatedLichSuThanhToanList = lichSuThanhToanService.updateLichSuThanhToanByHoaDon(
                idHoaDon, lichSuThanhToanRequest);

        if (updatedLichSuThanhToanList.isEmpty()) {
            // Trả về 404 nếu không tìm thấy lịch sử thanh toán
            return ResponseEntity.notFound().build();
        }

        // Tạo thông báo thành công
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cập nhật lịch sử thanh toán thành công!");

        // Trả về 200 với thông báo thành công
        return ResponseEntity.ok(response);
    }



}
