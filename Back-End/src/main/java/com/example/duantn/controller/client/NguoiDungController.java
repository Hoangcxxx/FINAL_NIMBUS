package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.LoginRequest;
import com.example.duantn.dto.NguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.DangNhapService;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.NguoiDungService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/nguoi_dung")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class NguoiDungController {
    @Autowired
    private DangNhapService dangNhapService;
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private HoaDonService hoaDonService;
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

            // In ra vai trò của người dùng khi đăng nhập thành công
            System.out.println("Người dùng '" + nguoiDung.getEmail() + "' đăng nhập thành công với vai trò: " + nguoiDung.getVaiTro().getTen());

            // Kiểm tra vai trò của người dùng để phân quyền truy cập
            if (nguoiDung.getVaiTro().getIdVaiTro() == 2) {
                // Quản trị viên, trả về quyền truy cập đầy đủ
                return ResponseEntity.ok(nguoiDung);
            } else {
                // Vai trò không hợp lệ
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (Exception e) {
            System.out.println("Đăng nhập thất bại với email '" + loginRequest.getEmail() + "'. Lỗi: " + e.getMessage());
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




    // Phương thức quên mật khẩu
    @PostMapping("/quen_mat_khau")
    public ResponseEntity<?> quenMatKhau(@RequestParam String email) {
        try {
            String message = dangNhapService.quenMatKhau(email);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tìm thấy tài khoản với email đã cung cấp");
        }
    }

    // Phương thức xác nhận mã khôi phục
    @PostMapping("/xac-nhan-ma-khoi-phuc")
    public ResponseEntity<String> xacNhanMaKhôiPhuc(@RequestParam String makhophuc) {
        try {
            String response = dangNhapService.xacnhandoimatkhau(makhophuc);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Phương thức đổi mật khẩu mới
    @PostMapping("/doi-mat-khau")
    public ResponseEntity<String> doiMatKhau(@RequestParam String email, @RequestParam String matKhauMoi) {
        try {
            String response = dangNhapService.doiMatKhau(email, matKhauMoi);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
