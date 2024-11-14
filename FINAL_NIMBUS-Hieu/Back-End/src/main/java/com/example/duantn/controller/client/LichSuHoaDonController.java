package com.example.duantn.controller.client;

import com.example.duantn.entity.LichSuHoaDon;
import com.example.duantn.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lich-su-hoa-don")
public class LichSuHoaDonController {

    @Autowired
    private  LichSuHoaDonService lichSuHoaDonService;


    @GetMapping("/nguoi-dung/{nguoiDungId}")
    public List<LichSuHoaDon> getLichSuHoaDonByNguoiDungId(@PathVariable Integer nguoiDungId) {
        return lichSuHoaDonService.getLichSuHoaDonByNguoiDungId(nguoiDungId);
    }
}
