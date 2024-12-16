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
        map.put("giaBan", row[1]);
        map.put("soLuongBanRa", row[2]);
        map.put("urlHinhAnh", row[3]);
        map.put("thuTu", row[4]);
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
    private Map<String, Object> mapThongKeTrangThai(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idLoaiTrangThai", row[0]);
        map.put("tenTrangThai", row[1]);
        map.put("soLuongHoaDon", row[2]);
        return map;
    }
    private List<Map<String, Object>> mapThongKeTrangThais(List<Object[]> results) {
        return results.stream().map(this::mapThongKeTrangThai).collect(Collectors.toList());
    }
    @GetMapping("/tong_so_luong_trang_thai_hoa_don")
    public ResponseEntity<List<Map<String, Object>>> getAllSoluongLoaiTrangThaiHoaDon() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllSoluongLoaiTrangThaiHoaDon();
        List<Map<String, Object>> filteredProducts = mapThongKeTrangThais(hoaDonChiTiets);
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping("/tong_so_luong_ban_ra_thanh_nay")
    public ResponseEntity<List<Map<String, Object>>> getAllSoLuongBanRa() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllSoLuongBanRa();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongSoLuongBanRa", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }

    @GetMapping("/san_pham_ban_ra_hom_nay")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhamBanRa() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllSanPhamBanRa();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("sanPhamBanRa", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }

    @GetMapping("/tong_hoa_don_thang_nay")
    public ResponseEntity<List<Map<String, Object>>> getAllTongDoanhThu() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllTongDoanhThu();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongHoaDonThangNay", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }
    @GetMapping("/tong_hoa_don_hom_nay")
    public ResponseEntity<List<Map<String, Object>>> getAllTongHoaDonHomNay() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllTongHoaDonHomNay();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongHoaDonHomNay", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }
    @GetMapping("/tong_san_pham_trong_thang_nay")
    public ResponseEntity<List<Map<String, Object>>> getAllTongSanPhamTrongThang() {
        List<Object[]> hoaDonChiTiets = hoaDonChiTietService.getAllTongSanPhamTrongThang();
        List<Map<String, Object>> filteredResults = hoaDonChiTiets.stream()
                .map(row -> Map.of("tongSanPhamTrongThangNay", row[0]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredResults);
    }



}
