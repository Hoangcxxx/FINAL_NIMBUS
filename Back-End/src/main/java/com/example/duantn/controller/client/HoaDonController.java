package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/nguoi_dung/hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HoaDonController {

	@Autowired
	private HoaDonService hoaDonService;


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
}
