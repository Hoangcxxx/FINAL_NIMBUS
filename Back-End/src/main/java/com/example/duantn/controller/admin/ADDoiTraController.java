package com.example.duantn.controller.admin;

import com.example.duantn.service.DoiTraSevice.DoiTraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/admin/doi_tra")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADDoiTraController {

    @Autowired
    private DoiTraService doiTraService;

    private Map<String, Object> mapSanPhamDoiTraDetail(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("tenSanPham", row[0]);
        map.put("tenChatLieu", row[1]);
        map.put("tenMauSac", row[2]);
        map.put("tenKichThuoc", row[3]);
        map.put("soLuongHoanTra", row[4]);
        map.put("tongTienHoanTra", row[5]);
        map.put("lyDoHoanTra", row[6]);
        map.put("trangThaiHoanTra", row[7]);
        map.put("ngayCapNhat", row[8]);
        map.put("ngayTao", row[9]);
        return map;
    }

    private List<Map<String, Object>> mapSanPhamDoiTras(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamDoiTraDetail).collect(Collectors.toList());
    }

    @GetMapping("/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getAllSanPhams(@PathVariable Integer idHoaDon) {
        List<Object[]> doiTras = doiTraService.getAllSanPhamDoiTraByIdHoaDon(idHoaDon);
        List<Map<String, Object>> filteredProducts = mapSanPhamDoiTras(doiTras);
        return ResponseEntity.ok(filteredProducts);
    }
    @PutMapping("/cap-nhat-trang-thai/{idHoaDon}/{idLoaiTrangThai}/{idNhanVien}")
    public ResponseEntity<Map<String, Object>> capNhatTrangThaiHoanTra(
            @PathVariable Integer idHoaDon,
            @PathVariable Integer idLoaiTrangThai,
            @PathVariable Integer idNhanVien) {

        // Kiểm tra nếu cập nhật thành công
        boolean updated = doiTraService.capNhatTrangThaiHoanTra(idHoaDon, idLoaiTrangThai,idNhanVien);

        // Tạo đối tượng JSON để trả về
        Map<String, Object> response = new HashMap<>();
        if (updated) {
            response.put("status", "success");
            response.put("message", "Cập nhật trạng thái hoàn trả thành công.");
        } else {
            response.put("status", "error");
            response.put("message", "Không tìm thấy đơn đổi trả nào cho hóa đơn này.");
        }

        // Trả về đối tượng JSON
        return ResponseEntity.ok(response);
    }

}

