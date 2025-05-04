package com.example.duantn.controller.client;

import com.example.duantn.service.VoucherNguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung/vouchernguoidung")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class VoucherNguoiDungController {

    @Autowired
    private VoucherNguoiDungService voucherNguoiDungService;

    // Đếm số lần sử dụng voucher của người dùng
    @GetMapping("/count/{voucherId}/{nguoiDungId}")
    public boolean checkVoucherUsage(@PathVariable Integer voucherId, @PathVariable Integer nguoiDungId) {
        return voucherNguoiDungService.isVoucherEligibleForUse(voucherId, nguoiDungId);
    }
}
