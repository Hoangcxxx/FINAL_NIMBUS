package com.example.duantn.controller.client;

import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nguoi_dung/san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("maSanPham", row[1]);
        map.put("tenSanPham", row[2]);
        map.put("trangThai", row[3]);
        map.put("giaBan", row[4]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[5]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[6]); // Cập nhật chỉ số cho trạng thái
        map.put("urlAnh", row[7]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[8]);       // Danh sách kích thước
        return map;
    }
    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }
    private Map<String, Object> mapSanPhamGiamGiaDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("maSanPham", row[1]);
        map.put("tenSanPham", row[2]);
        map.put("giaBan", row[3]);
        map.put("giaKhuyenMai", row[4]);  // Cập nhật chỉ số cho giá bán
        map.put("giaTriKhuyenMai", row[5]);  // Cập nhật chỉ số cho giá bán
        map.put("kieuGiamGia", row[6]);  // Cập nhật chỉ số cho giá bán
        map.put("tenDotGiamGia", row[7]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayBatDau", row[8]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayKetThuc", row[9]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[10]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[11]); // Cập nhật chỉ số cho trạng thái
        map.put("urlAnh", row[12]);    // Cập nhật chỉ số cho thứ tự
        return map;
    }
    private List<Map<String, Object>> mapSanPhamGiamGias(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamGiamGiaDetail).collect(Collectors.toList());
    }
    private Map<String, Object> mapDanhMucDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("maSanPham", row[1]);
        map.put("tenSanPham", row[2]);
        map.put("trangThai", row[3]);
        map.put("giaBan", row[4]);  // Cập nhật chỉ số cho giá bán
        map.put("moTa", row[5]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[6]); // Cập nhật chỉ số cho trạng thái
        map.put("urlAnh", row[7]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[8]);       // Danh sách kích thước
        return map;
    }
    private List<Map<String, Object>> mapDanhMucs(List<Object[]> results) {
        return results.stream().map(this::mapDanhMucDetail).collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhams();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping("/san_pham_giam_gia")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhamGiamGias() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhamGiamGia();
        List<Map<String, Object>> filteredProducts = mapSanPhamGiamGias(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }

    @GetMapping("/findDanhMuc/{idDanhMuc}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDanhMuc(@PathVariable Integer idDanhMuc) {
        return ResponseEntity.ok(mapDanhMucs(sanPhamService.getSanPhamsByDanhMuc(idDanhMuc)));
    }


}
