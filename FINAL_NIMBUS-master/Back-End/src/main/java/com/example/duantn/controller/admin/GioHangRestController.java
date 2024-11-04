package com.example.duantn.controller.admin;

import com.example.duantn.dto.*;
import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.GIoHangService;
import com.example.duantn.service.GioHangChiTietService;
import com.example.duantn.service.SanPhamChiTietService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/gio-hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class GioHangRestController {
    @Autowired
    private GIoHangService gioHangService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @PostMapping("/them/{idNguoiDung}")
    public ResponseEntity<Map<String, String>> themSanPhamVaoGioHang(@PathVariable Integer idNguoiDung, @RequestBody GioHangRequest request) {
        gioHangService.themSanPhamVaoGioHang(idNguoiDung, request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sản phẩm đã được thêm vào giỏ hàng.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lay/{idNguoiDung}")
    public ResponseEntity<List<GioHangResponse>> layDanhSachSanPhamTrongGioHang(@PathVariable Integer idNguoiDung) {
        List<GioHangResponse> response = gioHangService.layDanhSachSanPhamTrongGioHang(idNguoiDung);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/xoa-san-pham/{idNguoiDung}/{idSanPhamChiTiet}")
    public ResponseEntity<Map<String, String>> xoaSanPhamKhoiGioHang(
            @PathVariable Integer idNguoiDung,
            @PathVariable Integer idSanPhamChiTiet) {
        try {
            gioHangService.xoaSanPhamKhoiGioHang(idNguoiDung, idSanPhamChiTiet);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sản phẩm đã được xóa thành công khỏi giỏ hàng.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi khi xóa sản phẩm khỏi giỏ hàng: " + e.getMessage()));
        }
    }
    @DeleteMapping("/xoa-tat-ca/{idNguoiDung}")
    public ResponseEntity<String> xoaTatCaSanPhamKhoiGioHang(@PathVariable Integer idNguoiDung) {
        try {
            gioHangService.xoaTatCaSanPhamKhoiGioHangKhongCapNhatSoLuong(idNguoiDung);
            return ResponseEntity.ok("Đã xóa tất cả sản phẩm trong giỏ hàng.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/cap-nhat/{idNguoiDung}/{idSanPhamChiTiet}")
    public ResponseEntity<Map<String, String>> capNhatSoLuong(@PathVariable Integer idNguoiDung,
                                                              @PathVariable Integer idSanPhamChiTiet,
                                                              @RequestBody Map<String, Integer> request) {
        Integer soLuong = request.get("soLuong");
        try {
            gioHangService.capNhatSoLuong(idNguoiDung, idSanPhamChiTiet, soLuong);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Số lượng sản phẩm đã được cập nhật thành công.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Lỗi khi cập nhật số lượng: " + e.getMessage()));
        }
    }
    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<Map<String, String>> deleteCartItemsByUserId(@PathVariable Long userId) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa tất cả chi tiết giỏ hàng của người dùng thành công");
        return ResponseEntity.ok(response);
    }

}
