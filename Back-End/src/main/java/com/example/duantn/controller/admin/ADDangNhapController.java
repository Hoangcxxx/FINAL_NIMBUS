package com.example.duantn.controller.admin;

import com.example.duantn.dto.LoginRequest;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.DangNhapService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADDangNhapController {
    @Autowired
    private DangNhapService dangNhapService;
    // Phương thức đăng nhập
    @PostMapping("/dang_nhap")
    public ResponseEntity<NguoiDung> dangNhap(@RequestBody LoginRequest loginRequest) {
        try {
            NguoiDung nguoiDung = dangNhapService.dangNhap(loginRequest.getEmail(), loginRequest.getMatKhau());

            // In ra vai trò của người dùng khi đăng nhập thành công
            System.out.println("Người dùng '" + nguoiDung.getEmail() + "' đăng nhập thành công với vai trò: " + nguoiDung.getVaiTro().getTen());

            // Kiểm tra vai trò của người dùng để phân quyền truy cập
            if (nguoiDung.getVaiTro().getIdVaiTro() == 1) {
                // Quản trị viên, trả về quyền truy cập đầy đủ
                return ResponseEntity.ok(nguoiDung);
            }  else {
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

            // Trả về thông báo đăng xuất thành công dưới dạng JSON
            return ResponseEntity.ok().body(new ResponseMessage("Đăng xuất thành công"));
        } catch (Exception e) {
            // Trả về lỗi đăng xuất thất bại dưới dạng JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Đăng xuất thất bại"));
        }
    }

    // Lớp ResponseMessage dùng để trả về thông báo dưới dạng JSON
    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }




}
