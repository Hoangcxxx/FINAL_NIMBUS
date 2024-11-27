package com.example.duantn.controller.admin;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.KichThuoc;
import com.example.duantn.service.HoaDonChiTietService;
import com.example.duantn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADHoaDonController {
    @Autowired
    private HoaDonService hoaDonService;

    private Map<String, Object> mapHoaDon(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idHoaDon", row[0]);
        map.put("maHoaDon", row[1]);
        map.put("tenNguoiDung", row[2]);
        map.put("ngayTao", row[3]);
        map.put("thanhTien", row[4]);
        map.put("trangThaiHoaDon", row[5]);
        return map;
    }

    private List<Map<String, Object>> mapHoaDons(List<Object[]> results) {
        return results.stream().map(this::mapHoaDon).collect(Collectors.toList());
    }


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllThongKe() {
        List<Object[]> hoaDons = hoaDonService.getAllHoaDon();
        List<Map<String, Object>> filteredProducts = mapHoaDons(hoaDons);
        return ResponseEntity.ok(filteredProducts);
    }

    @GetMapping("/{idHoaDon}")
    public ResponseEntity<Map<String, Object>> getHoaDonById(@PathVariable Integer idHoaDon) {
        Optional<HoaDon> hoaDonOptional = hoaDonService.getHoaDonById(idHoaDon);

        if (hoaDonOptional.isPresent()) {
            HoaDon hoaDon = hoaDonOptional.get();

            // Chỉ lấy thông tin của hóa đơn, không lấy chi tiết hóa đơn hay các đối tượng liên quan
            Map<String, Object> result = new HashMap<>();
            result.put("idHoaDon", hoaDon.getIdHoaDon());
            result.put("maHoaDon", hoaDon.getMaHoaDon());
            result.put("tenNguoiNhan", hoaDon.getTenNguoiNhan());
            result.put("emailNguoiNhan", hoaDon.getNguoiDung().getEmail());
            result.put("diaChi", hoaDon.getDiaChi());
            result.put("sdtNguoiNhan", hoaDon.getSdtNguoiNhan());
            result.put("phiShip", hoaDon.getPhiShip());
            result.put("thanhTien", hoaDon.getThanhTien());
            result.put("trangThai", hoaDon.getTrangThai());
            result.put("ngayTao", hoaDon.getNgayTao());
            result.put("ngayThanhToan", hoaDon.getNgayThanhToan());
            result.put("idDiaChiVanChuyen", hoaDon.getDiaChiVanChuyen() != null ? hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen() : null);
            result.put("idVoucher", hoaDon.getVoucher() != null ? hoaDon.getVoucher().getIdVoucher() : null);
            result.put("idPhuongThucThanhToan", hoaDon.getPhuongThucThanhToanHoaDon() != null ? hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon() : null);
            result.put("tenPhuongThucThanhToan", hoaDon.getPhuongThucThanhToanHoaDon() != null ? hoaDon.getPhuongThucThanhToanHoaDon().getPhuongThucThanhToan().getTenPhuongThuc() : null);
//            result.put("trangThaiHoaDon", hoaDon.getTrangThaiHoaDon() != null ? hoaDon.getTrangThaiHoaDon().getTenTrangThai() : null);

            return ResponseEntity.ok(result);
        } else {
            // Trường hợp không tìm thấy hóa đơn
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
