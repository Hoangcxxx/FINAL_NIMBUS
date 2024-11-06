    package com.example.duantn.controller.client;

    import com.example.duantn.dto.HoaDonDTO;
    import com.example.duantn.service.HoaDonService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/api/hoa-don")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public class HoaDonController {

        @Autowired
        private HoaDonService hoaDonService;


        @PostMapping("/them_thong_tin_nhan_hang")
        public ResponseEntity<String> placeOrder(@RequestBody HoaDonDTO hoaDonDTO) {
            hoaDonService.createOrder(hoaDonDTO);
            return ResponseEntity.ok("Đơn hàng đã được đặt thành công!");
        }

        @DeleteMapping("/delete")
        public ResponseEntity<String> deleteOrder(@RequestParam Integer hoaDonId) {
            try {
                hoaDonService.delete(hoaDonId);
                return ResponseEntity.ok("Xóa thành công!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa thất bại!");
            }
        }
        @GetMapping("/{maHoaDon}")
        public ResponseEntity<List<HoaDonDTO>> getAllHoaDons(@PathVariable String maHoaDon) {
            List<HoaDonDTO> hoaDons;
            if (maHoaDon != null && !maHoaDon.trim().isEmpty()) {
                hoaDons = hoaDonService.getHoaDonsByMaHoaDon(maHoaDon);
            } else {
                hoaDons = hoaDonService.getAllHoaDons();
            }
            return ResponseEntity.ok(hoaDons);
        }



    }