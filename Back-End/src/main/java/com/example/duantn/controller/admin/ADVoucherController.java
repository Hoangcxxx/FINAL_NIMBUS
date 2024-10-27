package com.example.duantn.controller.admin;

import com.example.duantn.entity.Voucher;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ad_vouchers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADVoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        // Kiểm tra nếu mã voucher không được cung cấp
        if (voucher.getMaVoucher() == null || voucher.getMaVoucher().isEmpty()) {
            // Tạo mã voucher mới ngẫu nhiên với 8 ký tự
            voucher.setMaVoucher(generateRandomVoucherCode(8));
        }
        // Tạo voucher
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        return new ResponseEntity<>(createdVoucher, HttpStatus.CREATED);
    }

    private String generateRandomVoucherCode(int length) {
        // Các ký tự có thể sử dụng cho mã voucher
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder voucherCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            voucherCode.append(characters.charAt(index));
        }

        return voucherCode.toString();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucherDetails) {
        Voucher updatedVoucher = voucherService.updateVoucher(id, voucherDetails);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }
}
