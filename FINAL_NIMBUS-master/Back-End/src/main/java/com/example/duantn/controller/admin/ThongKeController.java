package com.example.duantn.controller.admin;

import com.example.duantn.dto.ThongKeDoanhThuDTO;
import com.example.duantn.dto.ThongKeTongDoanhThuDTO;
import com.example.duantn.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/thong-ke")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ThongKeController {

    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/doanh-thu-san-pham")
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuSanPham() {
        return thongKeService.thongKeDoanhThuSanPham();
    }
    @GetMapping("/tong-doanh-thu")
    public ThongKeTongDoanhThuDTO thongKeTongDoanhThu() {
        return thongKeService.thongKeTongDoanhThu();
    }
    @GetMapping("/doanh-thu")
    public List<ThongKeDoanhThuDTO> getDoanhThuTheoNgay(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date toDate) {
        return thongKeService.thongKeDoanhThuTheoNgay(fromDate, toDate);
    }
    @GetMapping("/doanh-thu-theo-danh-muc")
    public List<ThongKeDoanhThuDTO> getDoanhThuTheoDanhMuc(@RequestParam String tenDanhMuc) {
        System.out.println("Fetching revenue for category name: " + tenDanhMuc);
        List<ThongKeDoanhThuDTO> result = thongKeService.thongKeDoanhThuTheoDanhMuc(tenDanhMuc);
        System.out.println("Result: " + result);
        return result;
    }
}