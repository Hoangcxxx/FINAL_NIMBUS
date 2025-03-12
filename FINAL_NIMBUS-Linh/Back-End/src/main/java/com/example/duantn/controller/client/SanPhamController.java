package com.example.duantn.controller.client;

import com.example.duantn.dto.SanPhamDTO;
import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.DotGiamGiaRepository;
import com.example.duantn.repository.SanPhamRepository;
import com.example.duantn.service.DotGiamGiaService;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nguoi_dung/san_pham")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private DotGiamGiaService dotGiamGiaService;
    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;
    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("maSanPham", row[1]);
        map.put("tenSanPham", row[2]);
        map.put("giaBan", row[3]);
        map.put("moTa", row[4]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[5]); // Cập nhật chỉ số cho trạng thái
        map.put("tenDotGiamGia", row[6]); // Cập nhật chỉ số cho trạng thái
        map.put("giaKhuyenMai", row[7]);  // Cập nhật chỉ số cho giá bán
        map.put("giaTriKhuyenMai", row[8]);  // Cập nhật chỉ số cho giá bán
        map.put("kieuGiamGia", row[9]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayBatDau", row[10]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayKetThuc", row[11]);  // Cập nhật chỉ số cho giá bán
        map.put("urlAnh", row[12]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[13]);       // Danh sách kích thước
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
        map.put("giaBan", row[3]);
        map.put("moTa", row[4]);    // Cập nhật chỉ số cho mô tả
        map.put("tenDanhMuc", row[5]); // Cập nhật chỉ số cho trạng thái
        map.put("tenDotGiamGia", row[6]); // Cập nhật chỉ số cho trạng thái
        map.put("giaKhuyenMai", row[7]);  // Cập nhật chỉ số cho giá bán
        map.put("giaTriKhuyenMai", row[8]);  // Cập nhật chỉ số cho giá bán
        map.put("kieuGiamGia", row[9]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayBatDau", row[10]);  // Cập nhật chỉ số cho giá bán
        map.put("ngayKetThuc", row[11]);  // Cập nhật chỉ số cho giá bán
        map.put("urlAnh", row[12]);    // Cập nhật chỉ số cho thứ tự
        map.put("thuTu", row[13]);       // Danh sách kích thước
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
    @GetMapping("/findDotGiamGia/{idDotGiamGia}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDotGiamGia(@PathVariable Integer idDotGiamGia) {
        return ResponseEntity.ok(mapSanPhamGiamGias(sanPhamService.getSanPhamsByIdDotGiamGia(idDotGiamGia)));
    }
    @GetMapping("/dot_giam_gia")
    public List<DotGiamGia> getAllDotGiamGia() {
        List<DotGiamGia> list = dotGiamGiaRepository.findAll();
        return list.stream()
                .sorted(Comparator.comparing(DotGiamGia::getNgayCapNhat).reversed()) // Sắp xếp giảm dần
                .collect(Collectors.toList());
    }
    // API kiểm tra trạng thái của sản phẩm
    @GetMapping("/{id}/trang-thai")
    public Boolean checkSanPhamTrangThai(@PathVariable Integer id) {
        return sanPhamService.checkTrangThaiSanPham(id);
    }

    @GetMapping("/search")
    public List<SanPhamDTO> searchProducts(@RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice,
                                           @RequestParam(required = false) Integer danhMucId,
                                           @RequestParam(required = false) Integer chatLieuId,
                                           @RequestParam(required = false) Integer mauSacId,
                                           @RequestParam(required = false) Integer kichThuocId) {
        return sanPhamService.searchProducts(minPrice, maxPrice, danhMucId, chatLieuId, mauSacId, kichThuocId);
    }
    @GetMapping("/phan_trang")
    public ResponseEntity<Map<String, Object>> getSanPhamPhanTrang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idSanPham").ascending());
        Page<SanPham> sanPhamPage = sanPhamRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("sanPhams", sanPhamPage.getContent());
        response.put("totalPages", sanPhamPage.getTotalPages());
        response.put("totalElements", sanPhamPage.getTotalElements());
        response.put("currentPage", sanPhamPage.getNumber());

        return ResponseEntity.ok(response);
    }

}
