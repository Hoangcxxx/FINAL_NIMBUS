package com.example.duantn.controller.admin;

import com.example.duantn.entity.LoaiVoucher;
import com.example.duantn.service.LoaiVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ad_loai_vouchers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADLoaiVoucherController {
    @Autowired
    private LoaiVoucherService loaiVoucherService;

    @GetMapping
    public List<LoaiVoucher> getAllLoaiVouchers() {
        return loaiVoucherService.getAllLoaiVouchers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiVoucher> getLoaiVoucherById(@PathVariable Integer id) {
        Optional<LoaiVoucher> loaiVoucher = loaiVoucherService.getLoaiVoucherById(id);
        return loaiVoucher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LoaiVoucher> createLoaiVoucher(@RequestBody LoaiVoucher loaiVoucher) {
        LoaiVoucher createdLoaiVoucher = loaiVoucherService.createLoaiVoucher(loaiVoucher);
        return new ResponseEntity<>(createdLoaiVoucher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiVoucher> updateLoaiVoucher(@PathVariable Integer id, @RequestBody LoaiVoucher loaiVoucherDetails) {
        LoaiVoucher updatedLoaiVoucher = loaiVoucherService.updateLoaiVoucher(id, loaiVoucherDetails);
        return ResponseEntity.ok(updatedLoaiVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoaiVoucher(@PathVariable Integer id) {
        loaiVoucherService.deleteLoaiVoucher(id);
        return ResponseEntity.noContent().build();
    }
}
