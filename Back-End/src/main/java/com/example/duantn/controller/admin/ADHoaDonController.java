package com.example.duantn.controller.admin;

import com.example.duantn.entity.*;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.LoaiTrangThaiService;
import com.example.duantn.service.TrangThaiHoaDonService;
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
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADHoaDonController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private LoaiTrangThaiService loaiTrangThaiService;
    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

    private Map<String, Object> mapHoaDon(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idHoaDon", row[0]);
        map.put("maHoaDon", row[1]);
        map.put("tenNguoiDung", row[2]);
        map.put("sdt", row[3]);
        map.put("thanhTien", row[4]);
        map.put("loai", row[5]);
        map.put("ngayTao", row[6]);
        map.put("tenLoaiTrangThaiHoaDon", row[7]);
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
    private Map<String, Object> mapTrangThai(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("maHoaDon", row[0]);
        map.put("moTa", row[1]);
        map.put("idLoaiTrangThaiHoaDon", row[2]);
        map.put("tenLoaiTrangThaiHoaDon", row[3]);
        map.put("ngayTao", row[4]);
        map.put("ngayCapNhat", row[5]);
        return map;
    }

    private List<Map<String, Object>> mapTrangThais(List<Object[]> results) {
        return results.stream().map(this::mapTrangThai).collect(Collectors.toList());
    }
    private Map<String, Object> mapVoucher(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("maVoucher", row[0]);
        map.put("tenVoucher", row[1]);
        map.put("giaTriGiamGia", row[2]);
        map.put("kieuGiamGia", row[3]);
        return map;
    }

    private List<Map<String, Object>> mapVouchers(List<Object[]> results) {
        return results.stream().map(this::mapVoucher).collect(Collectors.toList());
    }
    private Map<String, Object> mapSanPhamCT(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idSanPhamCT", row[0]);
        map.put("soLuong", row[1]);
        map.put("trangThai", row[2]);
        map.put("maSanPham", row[3]);
        map.put("tenSanPham", row[4]);
        map.put("giaBan", row[5]);
        map.put("tenChatLieu", row[6]);
        map.put("tenMauSac", row[7]);
        map.put("tenKichThuoc", row[8]);
        map.put("giaKhuyenMai", row[9]);
        map.put("giaTriGiamGia", row[10]);
        map.put("kieuGiamGia", row[11]);
        map.put("tenDotGiamGia", row[12]);
        map.put("tongTien", row[13]);
        return map;
    }

    private List<Map<String, Object>> mapSanPhamCTs(List<Object[]> results) {
        return results.stream().map(this::mapSanPhamCT).collect(Collectors.toList());
    }
    @GetMapping("/findTrangThaiHoaDon/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getTrangThaiHoaDonByIdHoaDon(@PathVariable Integer idHoaDon) {
        // Lấy danh sách các hóa đơn từ dịch vụ
        List<Object[]> hoaDons = hoaDonService.getTrangThaiHoaDonByIdHoaDon(idHoaDon);

        // Chuyển đổi danh sách kết quả thành danh sách các Map
        List<Map<String, Object>> filteredProducts = mapTrangThais(hoaDons);

        // Trả về ResponseEntity với dữ liệu là List<Map<String, Object>>
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping("/findVoucherHoaDon/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getVoucherHoaDonByIdHoaDon(@PathVariable Integer idHoaDon) {
        // Lấy danh sách các hóa đơn từ dịch vụ
        List<Object[]> hoaDons = hoaDonService.getVoucherHoaDonByIdHoaDon(idHoaDon);

        // Chuyển đổi danh sách kết quả thành danh sách các Map
        List<Map<String, Object>> filteredProducts = mapVouchers(hoaDons);

        // Trả về ResponseEntity với dữ liệu là List<Map<String, Object>>
        return ResponseEntity.ok(filteredProducts);
    }

    @GetMapping("/findSanPhamCTHoaDon/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getSanPhamCTHoaDonByIdHoaDon(@PathVariable Integer idHoaDon) {
        // Lấy danh sách các hóa đơn từ dịch vụ
        List<Object[]> hoaDons = hoaDonService.getSanPhamCTHoaDonByIdHoaDon(idHoaDon);

        // Chuyển đổi danh sách kết quả thành danh sách các Map
        List<Map<String, Object>> filteredProducts = mapSanPhamCTs(hoaDons);
        return ResponseEntity.ok(filteredProducts);
    }
    @GetMapping("/findHoaDonCT/{idHoaDon}")
    public ResponseEntity<Map<String, Object>> getHoaDonById(@PathVariable Integer idHoaDon) {
        Optional<HoaDon> hoaDonOptional = hoaDonService.getHoaDonById(idHoaDon);

        if (hoaDonOptional.isPresent()) {
            HoaDon hoaDon = hoaDonOptional.get();

            // Tạo map để lưu thông tin trả về
            Map<String, Object> result = new HashMap<>();
            result.put("idHoaDon", hoaDon.getIdHoaDon());
            result.put("maHoaDon", hoaDon.getMaHoaDon());
            result.put("tenNguoiNhan", hoaDon.getTenNguoiNhan());
            result.put("emailNguoiNhan", hoaDon.getNguoiDung() != null ? hoaDon.getNguoiDung().getEmail() : null);
            result.put("diaChi", hoaDon.getDiaChi());
            result.put("sdtNguoiNhan", hoaDon.getSdtNguoiNhan());
            result.put("phiShip", hoaDon.getPhiShip());
            result.put("thanhTien", hoaDon.getThanhTien());
            result.put("trangThai", hoaDon.getTrangThai() != null ? hoaDon.getTrangThai() : null);
            result.put("ngayTao", hoaDon.getNgayTao());
            result.put("ngayThanhToan", hoaDon.getNgayThanhToan());
            result.put("idDiaChiVanChuyen", hoaDon.getDiaChiVanChuyen() != null ? hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen() : null);
            result.put("idVoucher", hoaDon.getVoucher() != null ? hoaDon.getVoucher().getIdVoucher() : null);
            result.put("idPhuongThucThanhToan", hoaDon.getPhuongThucThanhToanHoaDon() != null ? hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon() : null);
            result.put("tenPhuongThucThanhToan", hoaDon.getPhuongThucThanhToanHoaDon() != null ? hoaDon.getPhuongThucThanhToanHoaDon().getPhuongThucThanhToan().getTenPhuongThuc() : null);

            // Thêm thông tin trạng thái hóa đơn
            TrangThaiHoaDon trangThaiHoaDon = trangThaiHoaDonService.getAllTrangThaiHoaDon().get(idHoaDon);
            if (trangThaiHoaDon != null) {
                result.put("trangThaiHoaDon", trangThaiHoaDon.getLoaiTrangThai().getTenLoaiTrangThai());
            } else {
                result.put("trangThaiHoaDon", "Chưa có trạng thái");
            }

            return ResponseEntity.ok(result);
        } else {
            // Trả về lỗi nếu không tìm thấy hóa đơn
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/loai_trang_thai")
    public ResponseEntity<List<LoaiTrangThai>> getAllLoaiTrangThai() {
        List<LoaiTrangThai> LoaiTrangThaiList = loaiTrangThaiService.getAllLoaiTrangThai();
        return new ResponseEntity<>(LoaiTrangThaiList, HttpStatus.OK);
    }

    @PostMapping("/updateLoaiTrangThai")
    public ResponseEntity<Map<String, Object>> saveTrangThaiHoaDon(@RequestParam Integer idHoaDon,
                                                                   @RequestParam Integer idLoaiTrangThai,
                                                                   @RequestParam Integer idNhanVien) {
        System.out.println("Nhận yêu cầu cập nhật trạng thái hóa đơn với ID Hóa đơn: " + idHoaDon + " và ID Loại trạng thái: " + idLoaiTrangThai);

        List<TrangThaiHoaDon> results = trangThaiHoaDonService.saveTrangThaiHoaDon(idHoaDon, idLoaiTrangThai, idNhanVien);

        Map<String, Object> response = new HashMap<>();
        if (results == null || results.isEmpty()) {
            System.out.println("Không thể tạo trạng thái vì nó đã tồn tại hoặc có lỗi xảy ra.");
            response.put("message", "Không thể tạo trạng thái hóa đơn.");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            System.out.println("Trạng thái hóa đơn đã được lưu thành công.");
            response.put("message", "Trạng thái hóa đơn đã được lưu thành công.");
            response.put("success", true);
            return ResponseEntity.ok(response);
        }
    }

    private Map<String, Object> mapTrangThaiHoaDon(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("idTrangThaiHoaDon", row[0]);
        map.put("moTa", row[0]);
        map.put("ngayTao", row[1]);
        map.put("ngayCapNhat", row[2]);
        map.put("tenLoaiTrangThai", row[3]);
        map.put("idHoaDon", row[4]);
        return map;
    }

    private List<Map<String, Object>> mapTrangThaiHoaDons(List<Object[]> results) {
        return results.stream().map(this::mapTrangThaiHoaDon).collect(Collectors.toList());
    }
    @GetMapping("/trang_thai_hoa_don/{idHoaDon}")
    public ResponseEntity<List<Map<String, Object>>> getAllTrangThaiHoaDon(@PathVariable Integer idHoaDon) {
        // Lấy danh sách các hóa đơn từ dịch vụ
        List<Object[]> trangThaiHoaDons = trangThaiHoaDonService.getAllTrangThaiHoaDonByidHoaDon(idHoaDon);

        // Chuyển đổi danh sách kết quả thành danh sách các Map
        List<Map<String, Object>> filteredProducts = mapTrangThaiHoaDons(trangThaiHoaDons);

        // Trả về ResponseEntity với dữ liệu là List<Map<String, Object>>
        return ResponseEntity.ok(filteredProducts);
    }




}
