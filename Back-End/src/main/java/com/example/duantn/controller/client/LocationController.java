package com.example.duantn.controller.client;

import com.example.duantn.service.TestDemoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nguoi_dung/test/")
@CrossOrigin(origins = "http://127.0.0.1:5500") // Đảm bảo frontend có thể gọi được API từ domain này
public class LocationController {

    private final TestDemoService testDemoSevice;

    public LocationController(TestDemoService testDemoSevice) {
        this.testDemoSevice = testDemoSevice;
    }

    // API để lấy danh sách tỉnh thành
    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        try {
            return ResponseEntity.ok(testDemoSevice.getCities());  // Trả về danh sách tỉnh/thành phố
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi khi lấy tỉnh thành");
        }
    }

    // API để lấy danh sách huyện theo mã tỉnh
    @GetMapping("/districts/{cityCode}")
    public ResponseEntity<?> getDistricts(@PathVariable String cityCode) {
        try {
            return ResponseEntity.ok(testDemoSevice.getDistricts(cityCode)); // Trả về danh sách huyện của tỉnh
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi khi lấy huyện");
        }
    }

    // API để lấy danh sách xã theo mã huyện
    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<?> getWards(@PathVariable String districtCode) {
        try {
            return ResponseEntity.ok(testDemoSevice.getWards(districtCode)); // Trả về danh sách xã của huyện
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi khi lấy xã");
        }
    }

    @PostMapping("/save-location")
    public ResponseEntity<String> saveLocationToDB(
            @RequestParam Integer userId,
            @RequestParam String cityCode,
            @RequestParam String districtCode,
            @RequestParam String wardCode) {
        try {
            // Gọi service để lưu tỉnh, huyện, xã vào DB
            testDemoSevice.saveCityDistrictWardToDB(userId, cityCode, districtCode, wardCode);
            return ResponseEntity.ok("Lưu địa chỉ thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi khi lưu địa chỉ");
        }
    }


}
