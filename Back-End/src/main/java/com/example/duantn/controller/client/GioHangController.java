package com.example.duantn.controller.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/api/nguoi_dung/gio_hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @PostMapping("/add")
    public ResponseEntity<GioHang> addProductToGioHang(@RequestParam Integer idUser,
                                                       @RequestBody GioHangChiTietDTO gioHangChiTietDTO) {
        GioHang gioHang = gioHangService.addGioHang(idUser, gioHangChiTietDTO);
        // Tạo một map để trả về thông báo
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sản phẩm đã được thêm vào giỏ hàng.");
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
    public ResponseEntity<List<Map<String, Object>>> getGioHangChiTiet(@PathVariable Integer idNguoiDung) {
        List<Object[]> sanPhams = gioHangService.getGioHangChiTiets(idNguoiDung);
        List<Map<String, Object>> filteredProducts = mapGioHangs(sanPhams);
        return ResponseEntity.ok(filteredProducts);
    }
    @DeleteMapping("/clear/{idGioHang}")
    public ResponseEntity<String> clearGioHang(@PathVariable Integer idGioHang) {
        gioHangService.clearGioHang(idGioHang);
        return ResponseEntity.ok("Giỏ hàng đã được xóa sạch.");
    }
    private Map<String, Object> mapGioHangDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPhamCT", row[0]);        // spct.Id_san_pham_chi_tiet
        map.put("idSanPham", row[1]);          // sp.Id_san_pham
        map.put("maSanPham", row[2]);          // sp.ma_san_pham
        map.put("tenSanPham", row[3]);         // sp.ten_san_pham
        map.put("giaBan", row[4]);             // sp.gia_ban
        map.put("soLuong", row[5]);            // spct.so_luong
        map.put("trangThai", row[6]);          // spct.trang_thai

        // Cập nhật kieuGiamGia từ dgg.kieu_giam_gia
        map.put("kieuGiamGia", row[7]);        // dgg.kieu_giam_gia

        // Thêm thông tin kích thước, màu sắc, chất liệu
        map.put("kichThuoc", row[8]);          // ktt.ten_kich_thuoc
        map.put("mauSac", row[9]);             // mst.ten_mau_sac
        map.put("chatLieu", row[10]);          // clt.ten_chat_lieu

        // Thêm thông tin từ giỏ hàng chi tiết
        map.put("soLuongGioHang", row[11]);    // ghct.so_luong
        map.put("donGia", row[12]);            // ghct.don_gia
        map.put("thanhTien", row[13]);         // ghct.thanh_tien

        // Thêm thông tin về hình ảnh
        map.put("urlAnh", row[14]);            // ha.url_anh
        map.put("thuTu", row[15]);             // ha.thu_tu

        // Thêm thông tin giảm giá nếu có
        map.put("tenDotGiamGia", row[16]);    // dgg.ten_dot_giam_gia
        map.put("giaKhuyenMai", row[17]);     // ggsp.gia_khuyen_mai
        map.put("giaTriGiamGia", row[18]);    // dgg.gia_tri_giam_gia
        map.put("ngayBatDau", row[19]);       // dgg.ngay_bat_dau
        map.put("ngayKetThuc", row[20]);      // dgg.ngay_ket_thuc

        return map;
    }


    private List<Map<String, Object>> mapGioHangs(List<Object[]> results) {
        return results.stream().map(this::mapGioHangDetail).collect(Collectors.toList());
    }
}
