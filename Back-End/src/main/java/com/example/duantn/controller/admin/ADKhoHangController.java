package com.example.duantn.controller.admin;

import com.example.duantn.entity.DanhMuc;
import com.example.duantn.entity.KhoHang;
import com.example.duantn.service.DanhMucService;
import com.example.duantn.service.KhoHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/admin/kho_hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADKhoHangController {

    @Autowired
    private KhoHangService khoHangService;
    @Autowired
    private DanhMucService danhMucService;

    private Map<String, Object> mapSanPhamCTDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();

        if (row.length >= 13) { // Kiểm tra số lượng phần tử
            map.put("idSanPhamCT", row[0]);
            map.put("idSanPham", row[1]);
            map.put("tenSanPham", row[2]);
            map.put("soLuong", row[3]);
            map.put("idDanhMuc", row[4]);
            map.put("tenDanhMuc", row[5]);
            map.put("idChatLieu", row[6]);
            map.put("tenChatLieu", row[7]);
            map.put("idMauSac", row[8]);
            map.put("tenMauSac", row[9]);
            map.put("idKichThuoc", row[10]);
            map.put("tenKichThuoc", row[11]);
            map.put("trangThai", row[12]);
        } else {
            // Xử lý trường hợp không đủ dữ liệu
            System.out.println("Dữ liệu không đủ: " + row.length + " phần tử.");
        }

        return map;
    }


    private List<Map<String, Object>> mapSanPhamCTs(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamCTDetail).collect(Collectors.toList());
    }
    // 5. Get product details by size, material, and color
    @GetMapping("/product_details")
    public ResponseEntity<List<Map<String, Object>>> getProductDetails(
            @RequestParam Integer idSanPham,
            @RequestParam Integer idChatLieu,
            @RequestParam Integer idMauSac,
            @RequestParam Integer idKichThuoc) {
        List<Object[]> productDetails = khoHangService.searchProducts(idSanPham, idChatLieu, idMauSac, idKichThuoc);
        List<Map<String, Object>> mappedProductDetails = mapSanPhamCTs(productDetails);
        return ResponseEntity.ok(mappedProductDetails);
    }

    private Map<String, Object> mapSanPhamDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();

        if (row.length >= 2) { // Kiểm tra số lượng phần tử
            map.put("idSanPham", row[0]);
            map.put("tenSanPham", row[1]);
        } else {
            // Xử lý trường hợp không đủ dữ liệu
            System.out.println("Dữ liệu không đủ: " + row.length + " phần tử.");
        }

        return map;
    }


    private List<Map<String, Object>> mapSanPhams(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDetail).collect(Collectors.toList());
    }

    // 1. Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, Object>>> getCategories() {
        List<DanhMuc> categories = danhMucService.getAllCategories();
        List<Map<String, Object>> mappedCategories = categories.stream().map(category -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idDanhMuc", category.getIdDanhMuc());
            map.put("tenDanhMuc", category.getTenDanhMuc());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(mappedCategories);
    }


    // 2. Get products by category
    @GetMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> searchSanPham(@RequestParam Integer idDanhMuc) {
        List<Object[]> products = khoHangService.searchSanPham(idDanhMuc);
        List<Map<String, Object>> mappedProducts = mapSanPhams(products);
        return ResponseEntity.ok(mappedProducts);
    }

    // 3. Get materials by product
    @GetMapping("/materials")
    public ResponseEntity<List<Map<String, Object>>> searchChatLieu(@RequestParam Integer idSanPham) {
        List<Object[]> materials = khoHangService.searchChatLieu(idSanPham);
        return ResponseEntity.ok(mapMaterials(materials));
    }

    private List<Map<String, Object>> mapMaterials(List<Object[]> materials) {
        return materials.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idChatLieu", row[0]);
            map.put("tenChatLieu", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    // 4. Get colors by product and material
    @GetMapping("/colors")
    public ResponseEntity<List<Map<String, Object>>> searchMauSac(
            @RequestParam Integer idSanPham,
            @RequestParam Integer idChatLieu) {
        List<Object[]> colors = khoHangService.searchMauSac(idSanPham, idChatLieu);
        return ResponseEntity.ok(mapColors(colors));
    }

    private List<Map<String, Object>> mapColors(List<Object[]> colors) {
        return colors.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idMauSac", row[0]);
            map.put("tenMauSac", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    // 5. Get sizes by product, material, and color
    @GetMapping("/sizes")
    public ResponseEntity<List<Map<String, Object>>> searchKichThuoc(
            @RequestParam Integer idSanPham,
            @RequestParam Integer idChatLieu,
            @RequestParam Integer idMauSac) {
        List<Object[]> sizes = khoHangService.searchKichThuoc(idSanPham, idChatLieu, idMauSac);
        return ResponseEntity.ok(mapSizes(sizes));
    }

    private List<Map<String, Object>> mapSizes(List<Object[]> sizes) {
        return sizes.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idKichThuoc", row[0]);
            map.put("tenKichThuoc", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    // 6. Update stock
    @PutMapping("/update/{idSanPhamChiTiet}")
    public ResponseEntity<Void> updateStock(
            @PathVariable Integer idSanPhamChiTiet,
            @RequestParam Integer soLuongThem) {
        khoHangService.updateStock(idSanPhamChiTiet, soLuongThem);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update_status/{idSanPhamCT}")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer idSanPhamCT) {
        khoHangService.toggleStatusByIdSanPhamCT(idSanPhamCT);
        return ResponseEntity.ok().build();
    }
}
