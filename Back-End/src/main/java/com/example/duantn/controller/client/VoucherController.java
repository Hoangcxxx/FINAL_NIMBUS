package com.example.duantn.controller.client;

import com.example.duantn.dto.VoucherDTO;
import com.example.duantn.entity.Voucher;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/vouchers")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/{tongTien}")
    public ResponseEntity<List<Voucher>> getAllVouchers(
            @PathVariable("tongTien") BigDecimal tongTien) {
        try {
            // Truyền thêm idNguoiDung vào service
            List<Voucher> allVouchers = voucherService.getAllVouchersWithStatus(tongTien);
            return ResponseEntity.ok(allVouchers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/apma/{maVoucher}/{tongTien}")
    public ResponseEntity<?> useVoucher2(@PathVariable("maVoucher") String maVoucher,
                                         @PathVariable("tongTien") BigDecimal tongTien,
                                         @PathVariable("idNguoiDung") Integer idNguoiDung) {
        try {
            Voucher voucher = voucherService.apdungvoucher(maVoucher, tongTien, idNguoiDung);
            return ResponseEntity.ok(voucher);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
    // Định nghĩa một API để lấy Voucher theo idVoucher
    @GetMapping("/vouchers/{idVoucher}")
    public VoucherDTO getVoucherById(@PathVariable("idVoucher") Integer idVoucher) {
        return voucherService.getVoucherByIdhaha(idVoucher);
    }
}