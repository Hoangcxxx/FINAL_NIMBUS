package com.example.duantn.controller.admin;

import com.example.duantn.entity.Voucher;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

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
        public ResponseEntity<List<Voucher>> getAllVouchers(@PathVariable("tongTien") BigDecimal tongTien) {
            try {
                List<Voucher> allVouchers = voucherService.getAllVouchersWithStatus(tongTien);
                return ResponseEntity.ok(allVouchers);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

}
