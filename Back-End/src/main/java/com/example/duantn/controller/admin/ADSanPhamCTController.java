package com.example.duantn.controller.admin;

import com.example.duantn.dto.ProductDetailUpdateRequest;
import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/san_pham_chi_tiet")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADSanPhamCTController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private SanPhamChiTietService service;

    private Map<String, Object> mapRow(Object[] row, String... keys) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], row[i]);
        }
        return map;
    }

    private <T> ResponseEntity<List<Map<String, Object>>> getResponse(List<Object[]> results, String... keys) {
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> mappedList = results.stream()
                .map(row -> mapRow(row, keys))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mappedList);
    }

    @GetMapping("/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getById(@PathVariable Integer idSanPhamCT) {
        return getResponse(service.getById(idSanPhamCT), "idSanPham","maSanPham", "tenSanPham","tenDanhMuc","giaBan", "moTa", "ngayTao","tenDotGiamGia","giaKhuyenMai","giaTriGiamGia","kieuGiamGia","ngayBatDau","ngayKetThuc");
    }
    @GetMapping("/findSanPhamCTLonHon0/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamCTByIdSanPhamLonHon0(@PathVariable Integer idSanPhamCT) {
        return getResponse(service.getSanPhamCTByIdSanPhamLonHon0(idSanPhamCT), "idSanPhamCT","idSanPham","maSanPham", "tenSanPham", "soLuong","tenChatLieu","tenMauSac","tenKichThuoc", "moTa", "maSanPhamCT");
    }

    // DELETE method để xóa nhiều sản phẩm chi tiết
    @DeleteMapping
    public ResponseEntity<Void> deleteSanPhams(@RequestBody List<Integer> idSanPhamCTs) {
        sanPhamChiTietService.deleteByIds(idSanPhamCTs);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Xóa một sản phẩm chi tiết theo ID
    @DeleteMapping("/findSanPhamCT/{idSanPhamCT}")
    public ResponseEntity<Void> deleteSanPhamChiTietById(@PathVariable Integer idSanPhamCT) {
        sanPhamChiTietService.deleteByIdSanPhamCTs(idSanPhamCT);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/updateQuantities")
    public ResponseEntity<Void> updateSoLuongSanPhamCT(@RequestBody List<ProductDetailUpdateRequest> payload) throws IOException {
        // Gọi service để cập nhật số lượng cho từng sản phẩm chi tiết
        sanPhamChiTietService.updateSoLuongSanPhamCT(payload);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/findAllSanPhamCT/{idSanPham}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamCTById(@PathVariable Integer idSanPham) {
        return getResponse(service.getAllSanPhamCTById(idSanPham), "idSanPham","maSanPham", "tenSanPham", "soLuong","tenChatLieu","tenMauSac","tenKichThuoc", "moTa","idSanPhamCT","maSanPhamCT");
    }

}
