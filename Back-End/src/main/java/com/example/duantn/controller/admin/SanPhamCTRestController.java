package com.example.duantn.controller.admin;

    import com.example.duantn.dto.MuaSanPhamRequest;
    import com.example.duantn.dto.SanPhamChiTietDTO;
    import com.example.duantn.entity.SanPhamChiTiet;
    import com.example.duantn.service.SanPhamChiTietService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/san-pham-chi-tiet")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public class SanPhamCTRestController {
        @Autowired
        private SanPhamChiTietService sanPhamChiTietService;

        @GetMapping("/all")
        public List<SanPhamChiTietDTO> getAllSanPhamChiTiet() {
            return sanPhamChiTietService.getAllSanPhamChiTiet();
        }
        @PostMapping("/mua")
        public ResponseEntity<String> muaSanPham(@RequestBody   MuaSanPhamRequest request) {
            try {
                sanPhamChiTietService.muaSanPham(request.getIdSanPhamChiTiet(), request.getSoLuong());
                return ResponseEntity.ok("Chọn sản phẩm muốn mua thành công!");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        @PutMapping("/update-quantity")
        public ResponseEntity<?> updateProductQuantity(@RequestBody SanPhamChiTietDTO request) {
            SanPhamChiTiet productDetail = sanPhamChiTietService.findById(request.getIdSanPhamChiTiet());

            if (productDetail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Sản phẩm không tồn tại!"));
            }

            int updatedQuantity = productDetail.getSoLuong() + request.getSoLuong();

            if (updatedQuantity < 0) {
                return ResponseEntity.badRequest().body(("Số lượng không hợp lệ!"));
            }

            productDetail.setSoLuong(updatedQuantity);
            sanPhamChiTietService.save(productDetail);
            return ResponseEntity.ok(productDetail);
        }
    }