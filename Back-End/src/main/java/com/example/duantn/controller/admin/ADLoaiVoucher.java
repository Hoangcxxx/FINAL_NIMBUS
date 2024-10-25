package com.example.duantn.controller.admin;

import com.example.duantn.entity.LoaiVoucher;
import com.example.duantn.service.LoaiVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ad_loai_voucher")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADLoaiVoucher {
    @Autowired
    private LoaiVoucherService loaiVoucherService;

    // Phương thức chuyển đổi một hàng dữ liệu thành Map
    private Map<String, Object> mapLoaiVoucher(LoaiVoucher loaiVoucher) {
        Map<String, Object> map = new HashMap<>();
        map.put("idLoaiVoucher", loaiVoucher.getIdLoaiVoucher()); // Giả sử bạn có phương thức getId()
        map.put("tenLoaiVoucher", loaiVoucher.getTenLoaiVoucher()); // Giả sử bạn có phương thức getTen()
        return map;
    }

    // Phương thức chuyển đổi danh sách LoaiVoucher thành danh sách Map
    private List<Map<String, Object>> mapLoaiVouchers(List<LoaiVoucher> results) {
        return results.stream()
                .map(this::mapLoaiVoucher)
                .collect(Collectors.toList());
    }

    // Endpoint để lấy danh sách loại voucher
    @GetMapping
    public List<Map<String, Object>> getLoaiVouchers() {
        List<LoaiVoucher> loaiVoucherData = loaiVoucherService.getAllLoaiVouchers();
        return mapLoaiVouchers(loaiVoucherData);
    }
}
