package com.example.duantn.controller.admin;

import com.example.duantn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/thong_ke")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADHoaDonChiTietController {

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    private Map<String, Object> mapThongKe(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("tenSanPham", row[0]);
        map.put("soLuong", row[1]);
        map.put("doanhThu", row[2]);
        return map;
    }

    private List<Map<String, Object>> mapThongKes(List<Object[]> results) {
        return results.stream().map(this::mapThongKe).collect(Collectors.toList());
    }

    @GetMapping("/getAllThongKe")
    public ResponseEntity<List<Map<String, Object>>> getAllThongKe() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllThongKe();
        List<Map<String, Object>> filteredProducts = mapThongKes(hoaDonChiTiets);
        return ResponseEntity.ok(filteredProducts);
    }

    @GetMapping("/tong_so_luong_ban_ra")
    public ResponseEntity<List<Map<String, Object>>> getAllSoLuongBanRa() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllSoLuongBanRa();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongSoLuongBanRa", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }

    @GetMapping("/san_pham_ban_ra")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhamBanRa() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllSanPhamBanRa();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("sanPhamBanRa", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }

    @GetMapping("/tong_doanh_thu")
    public ResponseEntity<List<Map<String, Object>>> getAllTongDoanhThu() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllTongDoanhThu();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongDoanhThu", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }
    @GetMapping("/don_hang_cho")
    public ResponseEntity<Map<Object, Integer>> getDonHangCho(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        Integer soLuong;
        if (startDate == null || endDate == null) {
            soLuong = hoaDonChiTietService.getSoLuongDhCho(); // Trả về tổng số lượng
        } else {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            soLuong = hoaDonChiTietService.getSoLuongDonHangCho(start, end);
        }
        Map<Object, Integer> response = new HashMap<>();
        response.put("soLuongDonHangCho", soLuong);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/don_hang_dang_giao")
    public ResponseEntity<Map<Object, Integer>> getDonHangDangGiao(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        Integer soLuong;
        if (startDate == null || endDate == null) {
            soLuong = hoaDonChiTietService.getSoLuongDhDangGiao(); // Trả về tổng số lượng
        } else {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            soLuong = hoaDonChiTietService.getSoLuongDonHangDangGiao(start, end);
        }
        Map<Object, Integer> response = new HashMap<>();
        response.put("soLuongDonHangDangGiao", soLuong);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/don_hang_hoan_thanh")
    public ResponseEntity<Map<Object, Integer>> getDonHangHoanThanh(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        Integer soLuong;
        if (startDate == null || endDate == null) {
            soLuong = hoaDonChiTietService.getSoLuongDhHoanThanh(); // Trả về tổng số lượng
        } else {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            soLuong = hoaDonChiTietService.getSoLuongDonHangHoanThanh(start, end);
        }
        Map<Object, Integer> response = new HashMap<>();
        response.put("soLuongDonHangHoanThanh", soLuong);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/don_hang_huy_bo")
    public ResponseEntity<Map<Object, Integer>> getDonHangHuyBo(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        Integer soLuong;
        if (startDate == null || endDate == null) {
            soLuong = hoaDonChiTietService.getSoLuongDhHuyBo(); // Trả về tổng số lượng
        } else {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            soLuong = hoaDonChiTietService.getSoLuongDonHangHuyBo(start, end);
        }
        Map<Object, Integer> response = new HashMap<>();
        response.put("soLuongDonHangHuyBo", soLuong);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doanh_thu_theo_thang_nam")
    public ResponseEntity<Double> getDoanhThuTheoThangNam(@RequestParam int month, @RequestParam int year) {
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest().body(null);
        }
        if (year < 2000 || year > 2100) {
            return ResponseEntity.badRequest().body(null);
        }

        Double doanhThu = hoaDonChiTietService.findDoanhThuByMonthAndYear(month, year);
        return ResponseEntity.ok(doanhThu);
    }

    @GetMapping("/doanh_thu_theo_nam")
    public ResponseEntity<Double> getDoanhThuTheoNam(@RequestParam int year) {
        if (year < 2000 || year > 2100) {
            return ResponseEntity.badRequest().body(null);
        }

        Double doanhThu = hoaDonChiTietService.findDoanhThuByYear(year);
        return ResponseEntity.ok(doanhThu);
    }

}
