package com.example.duantn.controller;

import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherNguoiDung;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nguoi_dung/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    // Lấy tất cả voucher
    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    // Lấy voucher theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm mới voucher
    @PostMapping
    public ResponseEntity<Voucher> addVoucher(@RequestBody Voucher voucher) {
        try {
            Voucher newVoucher = voucherService.addVoucher(voucher);
            return ResponseEntity.ok(newVoucher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Cập nhật voucher
    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucherDetails) {
        Voucher updatedVoucher = voucherService.updateVoucher(id, voucherDetails);
        if (updatedVoucher != null) {
            return ResponseEntity.ok(updatedVoucher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa voucher (đánh dấu là đã xóa)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Hủy voucher (đánh dấu trạng thái hủy)
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Voucher> cancelVoucher(@PathVariable Integer id) {
        try {
            Voucher cancelledVoucher = voucherService.cancelVoucher(id);
            return ResponseEntity.ok(cancelledVoucher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Thêm voucher cho nhiều người dùng
    @PostMapping("/add-for-users")
    public ResponseEntity<List<VoucherNguoiDung>> addVoucherForUsers(@RequestParam List<Integer> userIds, @RequestBody Voucher voucher) {
        try {
            List<VoucherNguoiDung> addedVouchers = voucherService.addVoucherForUsers(userIds, voucher);
            return ResponseEntity.ok(addedVouchers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
