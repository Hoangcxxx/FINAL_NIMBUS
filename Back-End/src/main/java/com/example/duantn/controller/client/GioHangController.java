package com.example.duantn.controller.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.duantn.dto.SanPhamChiTietDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.duantn.dto.GioHangChiTietDTO;
import com.example.duantn.entity.GioHang;
import com.example.duantn.service.GioHangService;

@RestController
@RequestMapping("/api/gio_hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @PostMapping("/add")
    public ResponseEntity<GioHang> addProductToGioHang(@RequestParam Integer idUser,
                                                       @RequestBody GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangService.addGioHang(idUser, gioHangChiTietDTO);
        return ResponseEntity.ok(gioHang);
    }

    @PutMapping("/update")
    public ResponseEntity<GioHang> updateProductInGioHang(@RequestParam Integer idGioHang,
                                                          @RequestBody GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangService.updateGioHangChiTiet(idGioHang, gioHangChiTietDTO);
        return ResponseEntity.ok(gioHang);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteProductFromGioHang(
            @RequestParam Integer idGioHang,
            @RequestParam Integer idSanPhamChiTiet) {
        gioHangService.deleteGioHangChiTiet(idGioHang, idSanPhamChiTiet);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sản phẩm đã được xóa khỏi giỏ hàng.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idNguoiDung}")
    public ResponseEntity<List<SanPhamChiTietDTO>> getGioHangChiTiet(@PathVariable Integer idNguoiDung) {
        List<SanPhamChiTietDTO> sanpham = gioHangService.getGioHangChiTiet(idNguoiDung);
        return ResponseEntity.ok(sanpham);
    }

    @DeleteMapping("/clear/{idGioHang}")
    public ResponseEntity<String> clearGioHang(@PathVariable Integer idGioHang) {
        gioHangService.clearGioHang(idGioHang);
        return ResponseEntity.ok("Giỏ hàng đã được xóa sạch.");
    }

}
