package com.example.duantn.controller.admin;

import com.example.duantn.dto.ProductDetailUpdateRequest;
import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ad_san_pham_ct")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADSanPhamCTController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    // DELETE method để xóa nhiều sản phẩm chi tiết
    @DeleteMapping
    public ResponseEntity<Void> deleteSanPhams(@RequestBody List<Integer> idSanPhamCTs) {
        sanPhamChiTietService.deleteByIds(idSanPhamCTs);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/updateQuantities")
    public ResponseEntity<Void> updateSoLuongSanPhamCT(@RequestBody List<ProductDetailUpdateRequest> payload) throws IOException {
        // Gọi service để cập nhật số lượng cho từng sản phẩm chi tiết
        sanPhamChiTietService.updateSoLuongSanPhamCT(payload);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
