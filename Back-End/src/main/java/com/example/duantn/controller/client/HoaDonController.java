package com.example.duantn.controller.client;

import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.DTO.SanphamchiTietDTO;
import com.example.duantn.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/hoa-don")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class HoaDonController {

	@Autowired
	private HoaDonService hoaDonService;

	// Đặt đơn hàng
	@PostMapping("/them_thong_tin_nhan_hang")
	public ResponseEntity<Map<String, String>> placeOrder(@RequestBody HoaDonDTO hoaDonDTO, HttpServletRequest request) {
		try {
			// Tạo đơn hàng và lấy mã đơn hàng mới tạo
			String maHoaDon = hoaDonService.createOrder(hoaDonDTO, request);

			// Tạo phản hồi trả về cho frontend
			Map<String, String> response = new HashMap<>();
			response.put("message", "Đơn hàng đã được đặt thành công!");
			response.put("maHoaDon", maHoaDon); // Thêm mã đơn hàng vào phản hồi

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Lỗi khi đặt hàng: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body(Map.of("error", "Không thể đặt đơn hàng!"));
		}
	}

	// Hiển thị thông tin đơn hàng
	@GetMapping("/{maHoaDon}")
	public ResponseEntity<Map<String, Object>> hienthi(@PathVariable(required = false) String maHoaDon) {
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
			log.error("Lỗi khi lấy thông tin hóa đơn: {}", e.getMessage(), e);
			response.put("error", "Lỗi khi lấy thông tin hóa đơn!");
			return ResponseEntity.badRequest().body(response);
		}
	}
}
