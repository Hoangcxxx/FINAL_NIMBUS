package com.example.duantn.controller.admin;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherNguoiDung;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vouchers")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADVoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherService.getAllVouchers();
    }
    // Tìm kiếm theo tên voucher
    @GetMapping("/search/tenVoucher")
    public List<Voucher> searchByTenVoucher(@RequestParam String tenVoucher) {
        return voucherService.searchByTenVoucher(tenVoucher);
    }
    // Search by voucher code
    @GetMapping("/search")
    public List<Voucher> searchVouchers(
            @RequestParam(required = false) String maVoucher,
            @RequestParam(required = false) Integer trangThaiId,
            @RequestParam(required = false) Boolean kieuGiamGia) {
        return voucherService.searchVouchers(maVoucher, trangThaiId, kieuGiamGia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        return voucherService.getVoucherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Voucher createVoucher(@RequestBody Voucher voucher) {
        return voucherService.addVoucher(voucher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucherDetails) {
        Voucher updatedVoucher = voucherService.updateVoucher(id, voucherDetails);
        if (updatedVoucher != null) {
            return ResponseEntity.ok(updatedVoucher);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<Voucher> cancelVoucher(@PathVariable Integer id) {
        Voucher cancelledVoucher = voucherService.cancelVoucher(id);
        if (cancelledVoucher != null) {
            return ResponseEntity.ok(cancelledVoucher);
        }
        return ResponseEntity.notFound().build(); // Handle not found
    }


}
