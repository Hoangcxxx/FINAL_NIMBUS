package com.example.duantn.controller.client;

import com.example.duantn.service.LichSuThanhToanService;
import com.example.duantn.entity.LichSuThanhToan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/lich-su-thanh-toan")
public class LichSuThanhToanController {

    @Autowired
    private LichSuThanhToanService lichSuThanhToanService;

    @PostMapping("/create")
    public LichSuThanhToan createLichSuThanhToan(@RequestBody Map<String, Object> payload) {
        Integer idHoaDon = (Integer) payload.get("idHoaDon");
        Integer idNguoiDung = (Integer) payload.get("idNguoiDung");
        BigDecimal soTienThanhToan = new BigDecimal(payload.get("soTienThanhToan").toString());
        return lichSuThanhToanService.createLichSuThanhToan(idHoaDon, idNguoiDung, soTienThanhToan);
    }

}