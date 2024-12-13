package com.example.duantn.controller.client;

import com.example.duantn.entity.*;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import com.example.duantn.service.TestDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/test")
@CrossOrigin(origins = "http://127.0.0.1:5502") // Đảm bảo frontend có thể gọi được API từ domain này
public class LocationController {

    @Autowired
    private TestDemoService testDemoService;

    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    // API để lấy danh sách tỉnh thành
    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        try {
            List<?> cities = testDemoService.getCities();
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi lấy danh sách tỉnh thành");
        }
    }

    // API để lấy danh sách huyện theo mã tỉnh
    @GetMapping("/districts/{cityCode}")
    public ResponseEntity<?> getDistricts(@PathVariable String cityCode) {
        try {
            List<?> districts = testDemoService.getDistricts(cityCode);
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi lấy danh sách huyện");
        }
    }

    // API để lấy danh sách xã theo mã huyện
    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<?> getWards(@PathVariable String districtCode) {
        try {
            List<?> wards = testDemoService.getWards(districtCode);
            return ResponseEntity.ok(wards);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi lấy danh sách xã");
        }
    }

    // API để lưu địa chỉ vào DB và đặt làm địa chỉ mặc định
    @PostMapping("/saveAndSetDefault")
    public ResponseEntity<String> saveAndSetDefaultAddress(
            @RequestParam Integer userId,
            @RequestParam String cityCode,
            @RequestParam String districtCode,
            @RequestParam String wardCode,
            @RequestParam String specificAddress,
            @RequestParam Boolean setAsDefault) {

        try {
            testDemoService.saveAndSetDefaultAddress(userId, cityCode, districtCode, wardCode, specificAddress, setAsDefault);
            return ResponseEntity.ok("Địa chỉ đã được lưu thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi: " + e.getMessage());
        }
    }

    // API để lưu địa chỉ vào DB
    @PostMapping("/save-location")
    public ResponseEntity<String> saveLocationToDB(
            @RequestParam Integer userId,
            @RequestParam String cityCode,
            @RequestParam String districtCode,
            @RequestParam String wardCode) {

        try {
            testDemoService.saveCityDistrictWardToDB(userId, cityCode, districtCode, wardCode);
            return ResponseEntity.ok("Lưu địa chỉ thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi lưu địa chỉ");
        }
    }


    // API lấy danh sách các phương thức vận chuyển khả dụng
    @GetMapping("/available-services")
    public ResponseEntity<List<Map<String, Object>>> getShippingServices() {
        try {
            List<Map<String, Object>> services = testDemoService.getShippingServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // API tính phí vận chuyển
    @PostMapping("/fee")
    public ResponseEntity<Map<String, Object>> calculateShippingFee(
            @RequestParam Integer service_id,
            @RequestParam Integer districtId,  // Truyền mã quận huyện
            @RequestParam Integer wardId,
            @RequestParam Integer weight) {

        try {
            Map<String, Object> feeDetails = testDemoService.calculateShippingFee(service_id, districtId, wardId, weight);
            return ResponseEntity.ok(feeDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }



}