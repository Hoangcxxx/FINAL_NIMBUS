package com.example.duantn.controller.admin;

import com.example.duantn.dto.TimKiemNguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.VaiTro;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class NguoiDungRestController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/khach_hang")
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.getAllNguoiDungsByRoleId();
    }
    @GetMapping("/nhan_vien")
    public List<NguoiDung> getAllNhanVienBH() {
        return nguoiDungService.getAllNhanVienBanHang() ;
    }

    @PostMapping("/khach_hang/add")
    public ResponseEntity<NguoiDung> themNguoiDung(@RequestBody NguoiDung nguoiDung) {
        VaiTro vaiTro = new VaiTro();
        vaiTro.setIdVaiTro(2);
        nguoiDung.setVaiTro(vaiTro);
        NguoiDung savedNguoiDung = nguoiDungService.addNguoiDung1(nguoiDung);
        return new ResponseEntity<>(savedNguoiDung, HttpStatus.CREATED);
    }
    @GetMapping("/khach_hang/search")
    public ResponseEntity<List<TimKiemNguoiDungDTO>> searchCustomer(@RequestParam String phonePrefix) {
        List<TimKiemNguoiDungDTO> result = nguoiDungService.searchKhachHangByPhone(phonePrefix);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/khach_hang/check-sdt")
    public ResponseEntity<Boolean> checkDuplicatePhone(@RequestParam String sdt) {
        boolean isDuplicate = nguoiDungService.isPhoneDuplicate(sdt);
        return ResponseEntity.ok(isDuplicate);
    }

}
