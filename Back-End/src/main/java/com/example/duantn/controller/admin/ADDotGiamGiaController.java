package com.example.duantn.controller.admin;

import com.example.duantn.dto.DotGiamGiaDetail;
import com.example.duantn.dto.DotGiamGiaRequest;
import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.GiamGiaSanPham;
import com.example.duantn.entity.Voucher;
import com.example.duantn.service.DotGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/dot_giam_gia")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADDotGiamGiaController {

    @Autowired
    private DotGiamGiaService dotGiamGiaService;
    @GetMapping("/search/tenDotGiamGia")
    public List<DotGiamGia> searchByTenVoucher(@RequestParam String tenVoucher) {
        return dotGiamGiaService.findByTenVoucher(tenVoucher);
    }
    @GetMapping("/search/kieuGiamGia")
    public List<DotGiamGia> searchByKieuGiamGia(@RequestParam Boolean kieuGiamGia) {
        return dotGiamGiaService.findByKieuGiamGia(kieuGiamGia);
    }
    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("giaBan", row[2]);
        map.put("moTa", row[3]);
        map.put("tenDanhMuc", row[4]);
        map.put("urlAnh", row[5]);
        return map;
    }

    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }


    private Map<String, Object> mapDanhMucDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPham", row[0]);
        map.put("tenSanPham", row[1]);
        map.put("giaBan", row[2]);
        map.put("moTa", row[3]);
        map.put("tenDanhMuc", row[4]);
        map.put("urlAnh", row[5]);
        return map;
    }
    private List<Map<String, Object>> mapDanhMucs(List<Object[]> results) {
        return results.stream().map(this::mapDanhMucDetail).collect(Collectors.toList());
    }

    @GetMapping("/san_pham_chua_giam_gia")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams() {
        List<Object[]> sanPhams = dotGiamGiaService.getAllSanPhamChuaGiamGia();
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping("/san_pham_da_giam_gia/{idDotGiamGia}")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhamDaGiamGias(@PathVariable Integer idDotGiamGia) {
        List<Object[]> sanPhams = dotGiamGiaService.getAllSanPhamDaGiamGia(idDotGiamGia);
        List<Map<String, Object>> filteredProducts = mapSanPhams(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping
    public List<DotGiamGia> getAllDotGiamGia() {
        return dotGiamGiaService.getAllDotGiamGia();
    }

    @PostMapping("/create_with_san_pham")
    public ResponseEntity<?> createDotGiamGiaWithSanPham(@RequestBody DotGiamGiaRequest dotGiamGiaRequest) {
        String tenDotGiamGia = dotGiamGiaRequest.getTenDotGiamGia();

        // Validate tên đợt giảm giá
        if (tenDotGiamGia == null || tenDotGiamGia.isEmpty()) {
            return ResponseEntity.badRequest().body("Tên đợt giảm giá không được để trống.");
        }
        if (tenDotGiamGia.length() < 3 || tenDotGiamGia.length() > 50) {
            return ResponseEntity.badRequest().body("Tên đợt giảm giá phải từ 3 đến 50 ký tự.");
        }
        if (!tenDotGiamGia.matches("^[\\p{L}0-9\\s\\p{Punct}]+$")) {
            return ResponseEntity.badRequest().body("Tên đợt giảm giá chỉ được chứa chữ cái, số, dấu cách và ký tự đặc biệt hợp lệ.");
        }

        if (dotGiamGiaService.isTenDotGiamGiaExists(tenDotGiamGia)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên đợt giảm giá đã tồn tại."));
        }

        // Tạo đối tượng DotGiamGia và thêm sản phẩm như trước
        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setTenDotGiamGia(tenDotGiamGia);
        dotGiamGia.setGiaTriGiamGia(dotGiamGiaRequest.getGiaTriGiamGia());
        dotGiamGia.setMoTa(dotGiamGiaRequest.getMoTa());
        dotGiamGia.setNgayBatDau(dotGiamGiaRequest.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(dotGiamGiaRequest.getNgayKetThuc());
        dotGiamGia.setKieuGiamGia(dotGiamGiaRequest.isKieuGiamGia());

        // Tạo đợt giảm giá
        DotGiamGia createdDotGiamGia = dotGiamGiaService.addDotGiamGia(dotGiamGia);

        // Thêm sản phẩm liên quan
        for (GiamGiaSanPham giamGiaSanPham : dotGiamGiaRequest.getSanPhamList()) {
            dotGiamGiaService.addSanPhamToDotGiamGia(createdDotGiamGia.getIdDotGiamGia(), giamGiaSanPham);
        }

        return ResponseEntity.ok(createdDotGiamGia);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDotGiamGiaDetail(@PathVariable Integer id) {
        Optional<DotGiamGiaDetail> dotGiamGiaDetail = dotGiamGiaService.getDotGiamGiaDetailById(id);
        return dotGiamGiaDetail.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public DotGiamGia updateDotGiamGia(@PathVariable Integer id, @RequestBody DotGiamGia dotGiamGiaDetails) {
        return dotGiamGiaService.updateDotGiamGia(id, dotGiamGiaDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDotGiamGia(@PathVariable Integer id) {
        try {
            dotGiamGiaService.deleteDotGiamGia(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đợt giảm giá đã được xóa thành công!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/{idDotGiamGia}/add_san_pham")
    public ResponseEntity<?> addSanPhamToDotGiamGia(@PathVariable Integer idDotGiamGia, @RequestBody List<GiamGiaSanPham> giamGiaSanPhamList) {
        for (GiamGiaSanPham giamGiaSanPham : giamGiaSanPhamList) {
            dotGiamGiaService.addSanPhamToDotGiamGia(idDotGiamGia, giamGiaSanPham);
        }
        return ResponseEntity.ok("Sản phẩm đã được thêm thành công!");
    }
    @GetMapping("/findDanhMuc/{idDanhMuc}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamsByDanhMuc(@PathVariable Integer idDanhMuc) {
        return ResponseEntity.ok(mapDanhMucs(dotGiamGiaService.getSanPhamChuaGiamGiaByDanhMuc(idDanhMuc)));
    }
}
