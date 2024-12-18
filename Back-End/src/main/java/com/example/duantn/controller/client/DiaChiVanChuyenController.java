package com.example.duantn.controller.client;

import com.example.duantn.dto.DiaChiVanChuyenDTO;
import com.example.duantn.service.DiaChiVanChuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class DiaChiVanChuyenController {

    @Autowired
    private DiaChiVanChuyenService diaChiVanChuyenService;

    @GetMapping("/all")
    public List<DiaChiVanChuyenDTO> getAllDiaChiVanChuyen() {
        return diaChiVanChuyenService.getAllDiaChiVanChuyen();
    }

    @GetMapping("/tinh/{tinh}")
    public List<DiaChiVanChuyenDTO> getDiaChiByTinh(@PathVariable String tinh) {
        return diaChiVanChuyenService.getDiaChiByTinh(tinh);
    }

    @GetMapping("/huyen/{huyen}")
    public List<DiaChiVanChuyenDTO> getDiaChiByHuyen(@PathVariable String huyen) {
        return diaChiVanChuyenService.getDiaChiByHuyen(huyen);
    }

    @GetMapping("/xa/{xa}")
    public List<DiaChiVanChuyenDTO> getDiaChiByXa(@PathVariable String xa) {
        return diaChiVanChuyenService.getDiaChiByXa(xa);
    }
}
