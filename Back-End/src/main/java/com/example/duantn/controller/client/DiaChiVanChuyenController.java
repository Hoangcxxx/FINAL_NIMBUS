package com.example.duantn.controller.client;

import com.example.duantn.dto.*;
import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.entity.Huyen;
import com.example.duantn.entity.Tinh;
import com.example.duantn.entity.Xa;
import com.example.duantn.service.DiaChiVanChuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nguoi_dung/dia_chi")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DiaChiVanChuyenController {
    @Autowired
    private DiaChiVanChuyenService diaChiVanChuyenService;

    // Lấy danh sách địa chỉ vận chuyển của người dùng theo idNguoiDung

    // API để lấy địa chỉ vận chuyển theo id người dùng
    @GetMapping("/{idNguoiDung}")
    public List<DiaChiVanChuyen> getDiaChiByNguoiDung(@PathVariable Integer idNguoiDung) {
        return diaChiVanChuyenService.getDiaChiByNguoiDung(idNguoiDung);
    }
    // API lấy danh sách tỉnh
    @GetMapping("/tinh")
    public ResponseEntity<List<Tinh>> getAllTinh() {
        try {
            List<Tinh> tinhList = diaChiVanChuyenService.getAllTinh();
            return new ResponseEntity<>(tinhList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API lấy danh sách huyện theo tỉnh
    @GetMapping("/huyen")
    public ResponseEntity<List<Huyen>> getHuyenByTinh(@RequestParam Integer idTinh) {
        try {
            List<Huyen> huyenList = diaChiVanChuyenService.getHuyenByTinh(idTinh);
            return new ResponseEntity<>(huyenList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API lấy danh sách xã theo huyện
    @GetMapping("/xa")
    public ResponseEntity<List<Xa>> getXaByHuyen(@RequestParam Integer idHuyen) {
        try {
            List<Xa> xaList = diaChiVanChuyenService.getXaByHuyen(idHuyen);
            return new ResponseEntity<>(xaList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API để thêm địa chỉ vận chuyển mới
    @PostMapping("/add")
    public ResponseEntity<DiaChiVanChuyen> addDiaChiVanChuyen(@RequestBody DiaChiVanChuyenRequest request) {
        try {
            DiaChiVanChuyen diaChiVanChuyen = diaChiVanChuyenService.addDiaChiVanChuyen(
                    request.getIdTinh(),
                    request.getIdHuyen(),
                    request.getIdXa(),
                    request.getDiaChiCuThe(),
                    request.getMoTa(),
                    request.getIdNguoiDung()
            );
            return new ResponseEntity<>(diaChiVanChuyen, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
