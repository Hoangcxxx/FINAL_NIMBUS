package com.example.duantn.controller.admin;

import com.example.duantn.entity.Voucher;
import com.example.duantn.service.VoucherService; // Giả sử bạn đã tạo VoucherService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;


    @PostMapping("/apply")
    public ResponseEntity<String> applyVoucher(@RequestParam String maVoucher) {
        try {
            boolean success = voucherService.applyVoucher(maVoucher);
            if (success) {
                return ResponseEntity.ok("Voucher applied successfully.");
            } else {
                return ResponseEntity.badRequest().body("Voucher is invalid or out of stock.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/tuong_ung/{tongTien}")
    public ResponseEntity<Voucher> getVoucherTuongUng(@PathVariable BigDecimal tongTien) {
        System.out.println("Tong Tien: " + tongTien); // Ghi log để kiểm tra
        Optional<Voucher> voucher = voucherService.timVoucherTuongUng(tongTien);
        if (voucher.isPresent()) {
            return ResponseEntity.ok(voucher.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Không có voucher phù hợp
    }

}

