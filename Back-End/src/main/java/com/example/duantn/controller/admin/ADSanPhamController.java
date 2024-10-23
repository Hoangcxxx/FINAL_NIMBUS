package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import com.example.duantn.service.SanPhamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ad_san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("giaBan", row[2]);
        map.put("moTa", row[3]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[4]); // Cập nhật chỉ số cho trạng thái
        map.put("trangThai", row[5]); // Cập nhật chỉ số cho trạng thái
        return map;
    }

    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }

    // Thêm sản phẩm
    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Integer>> createSanPham(@RequestBody Map<String, Object> requestBody) {
        Integer idDanhMuc = (Integer) requestBody.get("idDanhMuc");
        String tenSanPham = (String) requestBody.get("tenSanPham");

        Object giaBanObj = requestBody.get("giaBan");
        BigDecimal giaBan;

        if (giaBanObj instanceof Integer) {
            giaBan = BigDecimal.valueOf((Integer) giaBanObj);
        } else if (giaBanObj instanceof BigDecimal) {
            giaBan = (BigDecimal) giaBanObj;
        } else {
            throw new IllegalArgumentException("Giá bán không hợp lệ");
        }

        String moTa = (String) requestBody.get("moTa");
        List<String> urlsHinhAnh = (List<String>) requestBody.get("hinhAnh");

        Boolean trangThai = true;
        Date ngayTao = new Date();
        Date ngayCapNhat = new Date();

        // Thêm sản phẩm vào cơ sở dữ liệu (phương thức sẽ lưu và không trả về ID)
        sanPhamService.addSanPham(idDanhMuc, tenSanPham, giaBan, moTa, ngayTao, ngayCapNhat, trangThai);
        System.out.println("Sản phẩm đã được thêm.");

        // Lấy ID sản phẩm mới nhất
        Integer idSanPham = sanPhamService.getLatestSanPhamId();

        // Kiểm tra xem ID sản phẩm có hợp lệ không
        if (idSanPham != null && idSanPham > 0) {
            // Thêm hình ảnh vào sản phẩm (nếu có)
            if (urlsHinhAnh != null && !urlsHinhAnh.isEmpty()) {
                for (int i = 0; i < urlsHinhAnh.size(); i++) {
                    sanPhamService.addHinhAnhSanPham(idSanPham, urlsHinhAnh.get(i), i + 1, "loai_hinh_anh");
                }
            }
        } else {
            throw new IllegalStateException("ID sản phẩm không hợp lệ sau khi thêm");
        }

        // Trả về ID sản phẩm vừa thêm
        Map<String, Integer> response = new HashMap<>();
        response.put("idSanPham", idSanPham);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<SanPham> updateSanPham(@PathVariable Integer id, @RequestBody SanPham sanPham) {
        sanPham.setIdSanPham(id);
        SanPham updatedSanPham = sanPhamService.updateSanPham(id, sanPham);
        return new ResponseEntity<>(updatedSanPham, HttpStatus.OK);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{idSanPham}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer idSanPham) {
        sanPhamService.deleteSanPham(idSanPham);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Lấy tất cả sản phẩm (nếu cần)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = sanPhamService.getAllSanPhamAD();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<SanPhamChiTiet>> createMultiple(@RequestBody List<SanPhamChiTiet> sanPhamChiTietList) throws IOException {
        if (sanPhamChiTietList.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        SanPhamChiTiet firstChiTiet = sanPhamChiTietList.get(0);
        if (firstChiTiet.getSanPham() == null || firstChiTiet.getSanPham().getIdSanPham() == null) {
            return ResponseEntity.badRequest().body(null); // Handle null cases
        }

        Integer idSanPham = firstChiTiet.getSanPham().getIdSanPham();
        List<SanPhamChiTiet> savedProducts = sanPhamChiTietService.createMultiple(sanPhamChiTietList, idSanPham);
        return ResponseEntity.ok(savedProducts);
    }

    @PutMapping("/update_status/{idSanPham}")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer idSanPham) {
        sanPhamService.toggleStatusById(idSanPham);
        return ResponseEntity.ok().build();
    }

}
