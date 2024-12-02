package com.example.duantn.controller.client;

import com.example.duantn.service.TestDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/test/")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LocationController {

    private final TestDemoService testDemoService;

    @Autowired
    public LocationController(TestDemoService testDemoService) {
        this.testDemoService = testDemoService;
    }

    // Lấy danh sách tỉnh thành
    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        try {
            List<Map<String, Object>> cities = testDemoService.getCities();
            if (cities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tỉnh thành");
            }
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return handleError("Có lỗi khi lấy danh sách tỉnh thành", e);
        }
    }

    // Lấy danh sách huyện theo mã tỉnh
    @GetMapping("/districts/{cityCode}")
    public ResponseEntity<?> getDistricts(@PathVariable String cityCode) {
        try {
            List<Map<String, Object>> districts = testDemoService.getDistricts(cityCode);
            if (districts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy huyện");
            }
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            return handleError("Có lỗi khi lấy danh sách huyện", e);
        }
    }

    // Lấy danh sách xã theo mã huyện
    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<?> getWards(@PathVariable String districtCode) {
        try {
            List<Map<String, Object>> wards = testDemoService.getWards(districtCode);
            if (wards.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy xã");
            }
            return ResponseEntity.ok(wards);
        } catch (Exception e) {
            return handleError("Có lỗi khi lấy danh sách xã", e);
        }
    }

    // Lưu địa chỉ vào DB
    @PostMapping("/save-location")
    public ResponseEntity<?> saveLocationToDB(
            @RequestParam Integer userId,
            @RequestParam String cityCode,
            @RequestParam String districtCode,
            @RequestParam String wardCode) {
        try {
            if (userId == null || userId <= 0 || cityCode == null || districtCode == null || wardCode == null) {
                return ResponseEntity.badRequest().body("Các tham số không hợp lệ");
            }
            testDemoService.saveCityDistrictWardToDB(userId, cityCode, districtCode, wardCode);
            return ResponseEntity.ok("Lưu địa chỉ thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            return handleError("Có lỗi khi lưu địa chỉ", e);
        }
    }

    // Tính phí ship
    @GetMapping("/calculate-fee")
    public ResponseEntity<?> calculateShippingFee(
            @RequestParam int toDistrictId,
            @RequestParam String toWardCode) {
        try {
            Map<String, Object> fee = testDemoService.calculateShippingFee(toDistrictId, toWardCode);
            if (fee == null || fee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không thể tính phí vận chuyển");
            }
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return handleError("Có lỗi khi tính phí vận chuyển", e);
        }
    }

    // Lấy danh sách dịch vụ vận chuyển khả dụng
    @GetMapping("/available-services")
    public ResponseEntity<?> getAvailableServices(
            @RequestParam int fromDistrictId,
            @RequestParam int toDistrictId) {
        try {
            Map<String, Object> services = testDemoService.getAvailableServices(fromDistrictId, toDistrictId);
            if (services == null || services.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có dịch vụ vận chuyển khả dụng");
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return handleError("Có lỗi khi lấy dịch vụ vận chuyển", e);
        }
    }

    // Xử lý lỗi và trả về phản hồi lỗi
    private ResponseEntity<?> handleError(String message, Exception e) {
        e.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        response.put("details", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
