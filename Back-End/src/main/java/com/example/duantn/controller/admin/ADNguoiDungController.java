package com.example.duantn.controller.admin;

import com.example.duantn.dto.NguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/nguoi_dung")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADNguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    // Lấy tất cả người dùng có vai trò id = 2
    @GetMapping("/list")
    public ResponseEntity<List<NguoiDung>> getAllNguoiDungsByRoleId() {
        List<NguoiDung> nguoiDungs = nguoiDungService.getAllNguoiDungsByRoleId();
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
    @DeleteMapping("/delete/{idNguoiDung}")
    public ResponseEntity<HttpStatus> deleteNguoiDung(@PathVariable Integer idNguoiDung) {
        return nguoiDungService.deleteNguoiDung(idNguoiDung);
    }
    // Tìm kiếm theo nhiều điều kiện
    @GetMapping("/search")
    public List<NguoiDung> searchNguoiDung(
            @RequestParam(required = false) String tenNguoiDung,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sdt) {
        return nguoiDungService.searchNguoiDung(tenNguoiDung, email, sdt);
    }

    // API để lấy tất cả người dùng có vai trò là 2 hoặc 4
    @GetMapping("/list/nguoidung")
    public List<NguoiDungDTO> getAllNguoiDung() {
        return nguoiDungService.getAllNguoiDung();  // Trả về danh sách người dùng đã lọc theo vai trò
    }
    @GetMapping("/list/nhanvien")
    public List<NguoiDungDTO> getAllNhanvien() {
        return nguoiDungService.getAllNhanvien();  // Trả về danh sách người dùng đã lọc theo vai trò
    }
    @PutMapping("/khoa/{id}")
    public ResponseEntity<?> lockUser(@PathVariable Integer id) {
        nguoiDungService.khoaNguoiDung(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Người dùng đã bị khóa.");
        return ResponseEntity.ok(response);
    }

    // API để mở khóa người dùng
    @PutMapping("/mo_khoa/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable Integer id) {
        nguoiDungService.moKhoaNguoiDung(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Người dùng đã được mở khóa.");
        return ResponseEntity.ok(response);
    }



    @GetMapping("/check_trang_thai/{idNguoiDung}")
    public ResponseEntity<?> checkTrangThaiNguoiDung(@PathVariable Integer idNguoiDung) {
        Optional<NguoiDung> optionalNguoiDung = Optional.ofNullable(nguoiDungService.findById(idNguoiDung));
        if (optionalNguoiDung.isPresent()) {
            NguoiDung nguoiDung = optionalNguoiDung.get();
            return ResponseEntity.ok(Map.of("trangThai", nguoiDung.getTrangThai()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng.");
        }
    }

}
