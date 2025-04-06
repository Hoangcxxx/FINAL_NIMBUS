package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.TrangThaiHoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/nguoi_dung/hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;
    @PostMapping("/them_thong_tin_nhan_hang")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody HoaDonDTO hoaDonDTO, HttpServletRequest request, HttpServletResponse reso) {
        try {
            // Tạo đơn hàng và lấy mã đơn hàng mới tạo (giả sử bạn nhận được đối tượng hoặc thông tin từ service)
            HoaDon hoaDon = hoaDonService.createOrder(hoaDonDTO, request, reso);

            // Lấy mã đơn hàng và id đơn hàng từ đối tượng hoaDon (giả sử HoaDon có trường idHoaDon và maHoaDon)
            String maHoaDon = hoaDon.getMaHoaDon();
            Integer idHoaDon = hoaDon.getIdHoaDon();

            // Tạo phản hồi trả về cho frontend
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đơn hàng đã được đặt thành công!");
            response.put("maHoaDon", maHoaDon); // Thêm mã đơn hàng vào phản hồi
            response.put("idHoaDon", idHoaDon); // Thêm idHoaDon vào phản hồi

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi đặt hàng: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Không thể đặt đơn hàng!"));
        }
    }

    // Hiển thị thông tin đơn hàng
    @GetMapping("/{maHoaDon}")
    public ResponseEntity<Map<String, Object>> hienthi(@PathVariable String maHoaDon) {
        try {
            Map<String, Object> response = new HashMap<>();
            if (maHoaDon != null && !maHoaDon.isEmpty()) {
                // Nếu có mã đơn hàng, lấy chi tiết đơn hàng cụ thể
                List<HoaDonDTO> hoaDon = hoaDonService.hienthi(maHoaDon);
                if (hoaDon != null) {
                    response.put("hoaDon", hoaDon);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("error", "Không tìm thấy đơn hàng với mã " + maHoaDon);
                    return ResponseEntity.status(404).body(response); // Trả về 404 nếu không tìm thấy
                }
            } else {
                // Nếu không có mã đơn hàng, trả về tất cả đơn hàng
                List<HoaDonDTO> hoaDons = hoaDonService.hienthi(null);
                response.put("hoaDons", hoaDons);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin hóa đơn: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Lỗi khi lấy thông tin hóa đơn!"));
        }
    }

    @GetMapping("/user/{idNguoiDung}")
    public ResponseEntity<Map<String, Object>> hienthi(@PathVariable Integer idNguoiDung) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Lấy danh sách hóa đơn của người dùng
            List<HoaDonDTO> hoaDonList = hoaDonService.getHoaDonByUserId(idNguoiDung);

            if (hoaDonList != null && !hoaDonList.isEmpty()) {
                response.put("hoaDon", hoaDonList);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Không tìm thấy hóa đơn nào cho người dùng có ID: " + idNguoiDung);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("error", "Lỗi khi lấy thông tin hóa đơn!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/thanh_toan_vnpay")
    public ResponseEntity<Map<String, Object>> thanhToanVnPay(@RequestBody HoaDonDTO hoaDonDTO, HttpServletRequest request, HttpServletResponse response,String generatedMaHoaDon,HoaDon hoaDon) {
        try {
            // Gọi service để tạo và xử lý hóa đơn
            hoaDonService.apithanhtoanvnpay(hoaDonDTO, request, response,generatedMaHoaDon,hoaDon);

            // Chuẩn bị dữ liệu phản hồi
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Đơn hàng đã được đặt thành công!");
            responseBody.put("maHoaDon", hoaDon.getMaHoaDon());
            responseBody.put("idHoaDon", hoaDon.getIdHoaDon());

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi lỗi
            return ResponseEntity.badRequest().body(Map.of("error", "Không thể đặt đơn hàng: " + e.getMessage()));
        }
    }
    @PostMapping("/updateLoaiTrangThai")
    public ResponseEntity<Map<String, Object>> saveTrangThaiHoaDon(@RequestParam Integer idHoaDon,
                                                                   @RequestParam Integer idLoaiTrangThai,
                                                                   @RequestParam Integer idNhanVien) {
        System.out.println("Nhận yêu cầu cập nhật trạng thái hóa đơn với ID Hóa đơn: " + idHoaDon + " và ID Loại trạng thái: " + idLoaiTrangThai);

        // Gọi service để lưu trạng thái hóa đơn
        List<TrangThaiHoaDon> results = trangThaiHoaDonService.saveDeleteTrangThaiHoaDon(idHoaDon, idLoaiTrangThai, idNhanVien);

        Map<String, Object> response = new HashMap<>();
        if (results == null || results.isEmpty()) {
            // Nếu không có kết quả hợp lệ, trả về lỗi chung
            response.put("message", "Có lỗi xảy ra khi lưu trạng thái hóa đơn.");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            // Kiểm tra xem có thông báo lỗi hay không
            String errorMessage = results.get(0).getMoTa();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Kiểm tra nếu thông báo lỗi là "Đơn hàng đã hủy bỏ"
                if (errorMessage.equals("Đơn hàng bị hủy bỏ.") || errorMessage.equals("Đơn hàng đã hủy bỏ thành công.")) {
                    response.put("message", "Đơn hàng đã hủy bỏ thành công.");
                    response.put("success", true);  // Trả về success true khi đơn hàng đã hủy bỏ thành công
                    return ResponseEntity.ok(response);
                }

                // Trả về thông báo lỗi chi tiết nếu không phải thông báo hủy bỏ
                response.put("message", errorMessage);
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Trả về lỗi chi tiết
            } else {
                response.put("message", "Trạng thái hóa đơn đã được lưu thành công.");
                response.put("success", true);
                return ResponseEntity.ok(response);  // Trả về thành công
            }
        }
    }

    @PostMapping("/huyLoaiTrangThai")
    public ResponseEntity<Map<String, Object>> huyTrangThaiHoaDon(@RequestParam Integer idHoaDon,
                                                                   @RequestParam Integer idLoaiTrangThai,
                                                                   @RequestParam Integer idNhanVien) {
        System.out.println("Nhận yêu cầu cập nhật trạng thái hóa đơn với ID Hóa đơn: " + idHoaDon + " và ID Loại trạng thái: " + idLoaiTrangThai);

        // Gọi service để lưu trạng thái hóa đơn
        List<TrangThaiHoaDon> results = trangThaiHoaDonService.huyTrangThaiHoaDon(idHoaDon, idLoaiTrangThai, idNhanVien);

        Map<String, Object> response = new HashMap<>();
        if (results == null || results.isEmpty()) {
            // Nếu không có kết quả hợp lệ, trả về lỗi chung
            response.put("message", "Có lỗi xảy ra khi lưu trạng thái hóa đơn.");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            // Kiểm tra xem có thông báo lỗi hay không
            String errorMessage = results.get(0).getMoTa();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Kiểm tra nếu thông báo lỗi là "Đơn hàng đã hủy bỏ"
                if (errorMessage.equals("Đơn hàng bị hủy bỏ.") || errorMessage.equals("Đơn hàng đã hủy bỏ thành công.")) {
                    response.put("message", "Đơn hàng đã hủy bỏ thành công.");
                    response.put("success", true);  // Trả về success true khi đơn hàng đã hủy bỏ thành công
                    return ResponseEntity.ok(response);
                }

                // Trả về thông báo lỗi chi tiết nếu không phải thông báo hủy bỏ
                response.put("message", errorMessage);
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Trả về lỗi chi tiết
            } else {
                response.put("message", "Trạng thái hóa đơn đã được lưu thành công.");
                response.put("success", true);
                return ResponseEntity.ok(response);  // Trả về thành công
            }
        }
    }
}
