package com.example.duantn.controller.admin;

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
    // Tìm kiếm voucher theo mã voucher
    @GetMapping("/search/maVoucher")
    public Voucher searchByMaVoucher(@RequestParam String maVoucher) {
        return voucherService.findByMaVoucher(maVoucher);
    }

    // Tìm kiếm voucher theo tên voucher (tìm theo tên chứa chuỗi con)
    @GetMapping("/search/tenVoucher")
    public List<Voucher> searchByTenVoucher(@RequestParam String tenVoucher) {
        return voucherService.findByTenVoucher(tenVoucher);
    }
    @GetMapping("/search/kieuGiamGia")
    public List<Voucher> searchByKieuGiamGia(@RequestParam Boolean kieuGiamGia) {
        return voucherService.findByKieuGiamGia(kieuGiamGia);
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
    @PostMapping("/bulk/{idNguoiDungs}")
    public ResponseEntity<List<VoucherNguoiDung>> addVoucherForMultipleUsers(
            @PathVariable List<Integer> idNguoiDungs,
            @RequestBody Voucher voucher) {

        System.out.println("Nhận yêu cầu thêm voucher cho các người dùng: " + idNguoiDungs);
        System.out.println("Thông tin voucher nhận được: " + voucher);

        try {
            // Thêm voucher cho người dùng
            List<VoucherNguoiDung> voucherNguoiDungs = voucherService.addVoucherForUsers(idNguoiDungs, voucher);
            System.out.println("Thêm voucher thành công. Số lượng voucher đã thêm: " + voucherNguoiDungs.size());
            return ResponseEntity.ok(voucherNguoiDungs);
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);  // Trả về lỗi nếu có người dùng không tồn tại
        }
    }

}
