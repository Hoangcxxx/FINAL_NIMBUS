package com.example.duantn.controller.admin;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/nguoi_dung")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADNguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    // Lấy tất cả người dùng có vai trò id = 2
    @GetMapping("/list")
    public ResponseEntity<List<NguoiDung>> getAllNguoiDungsByRoleId(@RequestParam("roleId") int roleId) {
        List<NguoiDung> nguoiDungs = nguoiDungService.getAllNguoiDungsByRoleId(roleId);
        if (nguoiDungs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(nguoiDungs, HttpStatus.OK);
    }

    // Lấy người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getNguoiDungById(@PathVariable int id) {
        Optional<NguoiDung> nguoiDung = nguoiDungService.getNguoiDungById(id);
        return nguoiDung.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Thêm người dùng
    @PostMapping("/create")
    public ResponseEntity<?> createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.addNguoiDung(nguoiDung);
    }

    // Cập nhật người dùng
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateNguoiDung(@PathVariable int id, @RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.updateNguoiDung(id, nguoiDung);
    }

    // Xóa người dùng
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteNguoiDung(@PathVariable int id) {
        return nguoiDungService.deleteNguoiDung(id);
    }


}
