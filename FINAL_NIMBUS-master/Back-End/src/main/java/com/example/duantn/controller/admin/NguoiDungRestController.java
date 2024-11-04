package com.example.duantn.controller.admin;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.VaiTro;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi-dung")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class NguoiDungRestController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.getAllNguoiDung();
    }
    @GetMapping("/ban-hang")
    public List<NguoiDung> getAllNhanVienBH() {
        return nguoiDungService.getAllNhanVienBanHang() ;
    }

    @PostMapping("/add")
    public ResponseEntity<NguoiDung> themNguoiDung(@RequestBody NguoiDung nguoiDung) {
        VaiTro vaiTro = new VaiTro();
        vaiTro.setIdVaiTro(2);
        nguoiDung.setVaiTro(vaiTro);
        NguoiDung savedNguoiDung = nguoiDungService.addNguoiDung(nguoiDung);
        return new ResponseEntity<>(savedNguoiDung, HttpStatus.CREATED);
    }
}
