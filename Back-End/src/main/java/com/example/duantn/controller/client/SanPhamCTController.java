package com.example.duantn.controller.client;

import com.example.duantn.entity.SanPham;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/san_pham_chi_tiet")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamCTController {
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
        return getResponse(service.getById(idSanPhamCT), "idSanPham", "tenSanPham", "giaBan", "moTa");
    }

    @GetMapping("/mau_sac/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getMauSacById(@PathVariable Integer idSanPhamCT) {
        return getResponse(service.getMauSacById(idSanPhamCT), "idMauSac", "tenMauSac");
    }

    @GetMapping("/kich_thuoc/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getKichThuocById(@PathVariable Integer idSanPhamCT) {
        return getResponse(service.getKichThuocById(idSanPhamCT), "idKichThuoc", "tenKichThuoc");
    }

    @GetMapping("/chat_lieu/{idSanPhamCT}")
    public ResponseEntity<List<Map<String, Object>>> getChatLieuById(@PathVariable Integer idSanPhamCT) {
        return getResponse(service.getChatLieuById(idSanPhamCT), "idChatLieu", "tenChatLieu");
    }




    @PostMapping("/multiple")
    public ResponseEntity<List<SanPhamChiTiet>> createMultiple(@RequestBody List<SanPhamChiTiet> sanPhamChiTietList) throws IOException {
        // Kiểm tra xem danh sách sản phẩm chi tiết có rỗng không
        if (sanPhamChiTietList.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu danh sách rỗng
        }

        // Lấy ID sản phẩm từ sản phẩm đầu tiên trong danh sách
        Integer idSanPham = sanPhamChiTietList.get(0).getSanPham().getIdSanPham();
        if (idSanPham == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu ID sản phẩm không được chọn
        }

        List<SanPhamChiTiet> savedProducts = service.createMultiple(sanPhamChiTietList, idSanPham);
        return ResponseEntity.ok(savedProducts); // Trả về danh sách đã lưu
    }

}
