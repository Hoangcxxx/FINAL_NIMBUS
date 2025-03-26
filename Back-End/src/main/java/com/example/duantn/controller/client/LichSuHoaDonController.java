package com.example.duantn.controller.client;

import com.example.duantn.entity.LichSuHoaDon;
import com.example.duantn.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/lich_su_hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class LichSuHoaDonController {

    @Autowired
    private  LichSuHoaDonService lichSuHoaDonService;

    @GetMapping("/{nguoiDungId}")
    public List<LichSuHoaDon> getLichSuHoaDonByNguoiDungId(@PathVariable Integer nguoiDungId) {
        return lichSuHoaDonService.getLichSuHoaDonByNguoiDungId(nguoiDungId);
    }
}
