package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.entity.Voucher;
import com.example.duantn.service.SanPhamChiTietService;
import com.example.duantn.service.SanPhamService;
import com.example.duantn.service.TrangThaiHoaDonService;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin/ban_hang")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADBanHangController {
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;
    @GetMapping("/all")
    public List<SanPham> getSanPhamForBanHang() {
        return sanPhamService.getSanPhamForBanHang();
    }
    @GetMapping("/all_quay")
    public List<Map<String, Object>> getSPBanHangTaiQuay() {
        List<Map<String, Object>> results = sanPhamService.getSPBanHangTaiQuay();
        return results;
    }
    @GetMapping("/search/{idSanPhamChiTiet}")
    public ResponseEntity<SanPhamChiTiet> getSanPhamChiTietSeach(@PathVariable Integer idSanPhamChiTiet) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietById(idSanPhamChiTiet);
        if (sanPhamChiTiet != null) {
            return ResponseEntity.ok(sanPhamChiTiet);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/create-trang-thai/{hoaDonId}")
    public ResponseEntity<Map<String, String>> TrangThaiHoaDonKhiChonPTTT(@PathVariable Integer hoaDonId) {
        try {
            trangThaiHoaDonService.TrangThaiHoaDonKhiChonPTTT(hoaDonId);

            // Trả về thông báo thành công
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Cập nhật trạng thái hóa đơn thành công!");
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            // Trả về thông báo lỗi nếu có exception
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @GetMapping("/chi_tiet")
    public List<Map<String, Object>> getSanPhamChiTiet(@RequestParam("id_san_pham") Integer idSanPham) {
        // Gọi service để lấy dữ liệu từ database
        List<Object[]> rawResults = sanPhamService.getSanPhamChiTiet(idSanPham);

        // Chuyển đổi Object[] thành Map với tên trường rõ ràng
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] result : rawResults) {
            Map<String, Object> data = new HashMap<>();
            data.put("id_san_pham_chi_tiet", result[0]);
            data.put("ma_san_pham_chi_tiet", result[1]);
            data.put("id_san_pham", result[2]);
            data.put("ma_san_pham", result[3]);
            data.put("ten_san_pham", result[4]);
            data.put("so_luong", result[5]);
            data.put("ten_chat_lieu", result[6]);
            data.put("ten_mau_sac", result[7]);
            data.put("ten_kich_thuoc", result[8]);
            data.put("mo_ta", result[9]);
            data.put("ten_dot_giam_gia", result[10]);
            data.put("gia_khuyen_mai", result[11]);
            data.put("gia_tri_giam_gia", result[12]);
            data.put("kieu_giam_gia", result[13]);
            data.put("ngay_bat_dau", result[14]);
            data.put("ngay_ket_thuc", result[15]);
            data.put("gia_ban", result[16]);
            response.add(data);
        }

        return response;
    }


    @PostMapping("/use/{maVoucher}")
    public ResponseEntity<?> useVoucher(@PathVariable("maVoucher") String maVoucher,
                                        @RequestBody BigDecimal tongTien) {
        try {
            Voucher voucher = voucherService.useVoucher(maVoucher, tongTien);
            return ResponseEntity.ok(voucher);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/apma/{maVoucher}")
    public ResponseEntity<?> useVoucher2(@PathVariable("maVoucher") String maVoucher,
                                         @RequestBody BigDecimal tongTien) {
        try {
            Voucher voucher = voucherService.apdungvoucher(maVoucher, tongTien);
            return ResponseEntity.ok(voucher);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
    @GetMapping("/valid/{tongTien}")
    public ResponseEntity<List<Voucher>> getValidVouchers(@PathVariable BigDecimal tongTien) {
        try {
            if (tongTien.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(null);
            }

            List<Voucher> validVouchers = voucherService.getValidVouchers(tongTien);
            if (validVouchers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(validVouchers);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/allvoucher/{tongTien}")
    public ResponseEntity<List<Voucher>> getAllVouchers(@PathVariable BigDecimal tongTien) {
        try {
            List<Voucher> allVouchers = voucherService.getAllVouchersWithStatus(tongTien);
            return ResponseEntity.ok(allVouchers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
