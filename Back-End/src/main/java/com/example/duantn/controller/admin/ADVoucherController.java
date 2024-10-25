package com.example.duantn.controller.admin;

import com.example.duantn.entity.LoaiVoucher;
import com.example.duantn.entity.Voucher;
import com.example.duantn.entity.VoucherSanPham;
import com.example.duantn.service.LoaiVoucherService;
import com.example.duantn.service.VoucherSanPhamService;
import com.example.duantn.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ad_voucher")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADVoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private LoaiVoucherService loaiVoucherService;
    @Autowired
    private VoucherSanPhamService voucherSanPhamService;

    private Map<String, Object> mapVoucher(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idVoucher", row[0]);
        map.put("maVoucher", row[1]);
        map.put("tenLoaiGiamGia", row[2]);
        map.put("giaTriGiamGia", row[3]);
        map.put("soLuong", row[4]);
        map.put("ngayBatDau", row[5]);
        map.put("ngayKetThuc", row[6]);
        map.put("ngayTao", row[7]);
        map.put("ngayCapNhat", row[8]);
        map.put("trangThai", row[9]);
        return map;
    }

    private List<Map<String, Object>> mapVouchers(List<Object[]> results) {
        return results.stream().map(this::mapVoucher).collect(Collectors.toList());
    }

    @GetMapping("/list")
    public List<Map<String, Object>> getVouchers() {
        List<Object[]> voucherData = voucherService.getAllVouchers();
        return mapVouchers(voucherData);
    }


    @PostMapping("/add_loai_voucher")
    public LoaiVoucher addLoaiVoucher(@RequestBody LoaiVoucher loaiVoucher) {
        return loaiVoucherService.addLoaiVoucher(loaiVoucher);
    }

    @PostMapping("/add_voucher")
    public Voucher addVoucher(@RequestBody Voucher voucher) {
        System.out.println("Thêm voucher: " + voucher);
        return voucherService.addVoucher(voucher);
    }

    @PostMapping("/voucher_sp")
    public List<VoucherSanPham> addVoucherSanPhams(@RequestBody List<VoucherSanPham> voucherSanPhams) {
        return voucherSanPhamService.addVoucherSanPhams(voucherSanPhams);
    }

}

