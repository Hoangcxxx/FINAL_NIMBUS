package com.example.duantn.controller.client;

import com.example.duantn.DTO.DiaChiVanChuyenDTO;
import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.service.DiaChiVanChuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class DiaChiVanChuyenController {

    @Autowired
    private DiaChiVanChuyenService diaChiVanChuyenService;

    // API để lấy phí ship dựa trên tỉnh, huyện, xã
    @GetMapping("/shipping-fee/{tinh}/{huyen}/{xa}")
    public ResponseEntity<BigDecimal> getShippingFee(@PathVariable String tinh, @PathVariable String huyen, @PathVariable String xa) {
        BigDecimal fee = diaChiVanChuyenService.getShippingFee(tinh, huyen, xa);
        return new ResponseEntity<>(fee, HttpStatus.OK);
    }
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
