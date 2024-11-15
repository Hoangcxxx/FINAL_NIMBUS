package com.example.duantn.controller.client;

import com.example.duantn.dto.LoginRequest;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.DangNhapService;
import com.example.duantn.service.NguoiDungService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class NguoiDungController {
    @Autowired
    private DangNhapService dangNhapService;
    @Autowired
    private NguoiDungService nguoiDungService;

    // Phương thức đăng ký
    @PostMapping("/dang_ky")
    public ResponseEntity<NguoiDung> dangKy(@RequestBody NguoiDung nguoiDung) {
        try {
            NguoiDung nguoiDungMoi = dangNhapService.dangKy(nguoiDung);
            return ResponseEntity.ok(nguoiDungMoi);
        } catch (Exception e) {
            System.out.println("Đăng ký thất bại: " + e.getMessage()); // Log lỗi khi đăng ký thất bại
            e.printStackTrace(); // In ra stack trace để dễ dàng theo dõi lỗi
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Phương thức đăng nhập
    @PostMapping("/dang_nhap")
    public ResponseEntity<NguoiDung> dangNhap(@RequestBody LoginRequest loginRequest) {
        try {
            NguoiDung nguoiDung = dangNhapService.dangNhap(loginRequest.getEmail(), loginRequest.getMatKhau());
            return ResponseEntity.ok(nguoiDung);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    // Phương thức đăng xuất
    @PostMapping("/dang_xuat")
    public ResponseEntity<?> dangXuat(HttpServletRequest request) {
        try {
            // Xóa thông tin xác thực của người dùng
            SecurityContextHolder.clearContext();  // Xóa Spring Security context
            // Trả về thông báo đăng xuất thành công
            return ResponseEntity.ok("Đăng xuất thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đăng xuất thất bại");
        }
    }
    // Lấy thông tin người dùng theo id
    @GetMapping("/{id}")
    public ResponseEntity<NguoiDung> getNguoiDungById(@PathVariable Integer id) {
        NguoiDung nguoiDung = nguoiDungService.getNguoiDungById(id);
        if (nguoiDung != null) {
            return ResponseEntity.ok(nguoiDung);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateNguoiDung(@PathVariable Integer id, @RequestBody @Valid NguoiDung nguoiDung) {
        NguoiDung updatedNguoiDung = nguoiDungService.updateNguoiDung(id, nguoiDung);
        if (updatedNguoiDung != null) {
            return ResponseEntity.ok(updatedNguoiDung);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
