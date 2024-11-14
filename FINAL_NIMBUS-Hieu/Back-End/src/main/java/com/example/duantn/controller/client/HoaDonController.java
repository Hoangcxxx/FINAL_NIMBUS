	package com.example.duantn.controller.client;

	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

	import com.example.duantn.entity.HoaDon;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;
	import lombok.extern.slf4j.Slf4j;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.CrossOrigin;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;

	import com.example.duantn.DTO.HoaDonDTO;
	import com.example.duantn.service.HoaDonService;

	@Slf4j
	@Controller
	@RequestMapping("/api/hoa-don")
	@CrossOrigin(origins = "http://127.0.0.1:5501")
	public class HoaDonController {

		@Autowired
		private HoaDonService hoaDonService;

		@PostMapping("/them_thong_tin_nhan_hang")
		public ResponseEntity<Map<String, String>> placeOrder(@RequestBody HoaDonDTO hoaDonDTO,HttpServletRequest request) {
			hoaDonService.createOrder(hoaDonDTO,request);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Đơn hàng đã được đặt thành công!");
			return ResponseEntity.ok(response);
		}

	//	@DeleteMapping("/delete")
	//	public ResponseEntity<String> deleteOrder(@RequestParam Integer hoaDonId) {
	//		try {
	//			hoaDonService.delete(hoaDonId);
	//			return ResponseEntity.ok("Xóa thành công!");
	//		} catch (Exception e) {
	//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa thất bại!");
	//		}
	//	}
	//	@GetMapping("/{maHoaDon}")
	//	public ResponseEntity<List<HoaDonDTO>> getAllHoaDons(@PathVariable String maHoaDon) {
	//		List<HoaDonDTO> hoaDons;
	//		if (maHoaDon != null && !maHoaDon.trim().isEmpty()) {
	//			hoaDons = hoaDonService.getHoaDonsByMaHoaDon(maHoaDon);
	//		} else {
	//			hoaDons = hoaDonService.getAllHoaDons();
	//		}
	//		return ResponseEntity.ok(hoaDons);
	//	}
	@GetMapping("/{maHoaDon}")
	public ResponseEntity<List<HoaDonDTO>> hienthi(@PathVariable(required = false) String maHoaDon) {
		List<HoaDonDTO> hoaDons = hoaDonService.hienthi(maHoaDon);
		return ResponseEntity.ok(hoaDons);
	}



	}