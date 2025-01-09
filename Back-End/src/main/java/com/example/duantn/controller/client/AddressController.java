package com.example.duantn.controller.client;

import com.example.duantn.entity.Huyen;
import com.example.duantn.repository.HuyenRepository;
import com.example.duantn.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/address")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class AddressController {

    @Autowired
    private AddressService addressService; // Đã sửa tên biến từ addressSerice thành addressService

    @Autowired
    private HuyenRepository huyenRepository; // Đã sửa tên biến từ addressSerice thành addressService
    // Lấy danh sách các tỉnh
    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces() {
        try {
            // Gọi phương thức getProvinces từ addressService để lấy danh sách tỉnh
            List<Map<String, Object>> cities = addressService.getProvinces();

            // Đảm bảo rằng các tỉnh có id và name để gửi đến frontend
            List<Map<String, Object>> provinces = new ArrayList<>();
            for (Map<String, Object> city : cities) {
                Map<String, Object> province = new HashMap<>();
                province.put("id", city.get("ProvinceID")); // Đảm bảo id là ProvinceID
                province.put("name", city.get("ProvinceName")); // Đảm bảo name là ProvinceName
                provinces.add(province);
            }

            // Trả về danh sách tỉnh nếu thành công
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            // In ra lỗi nếu có ngoại lệ xảy ra
            e.printStackTrace();
            // Trả về lỗi 500 nếu có sự cố khi lấy danh sách tỉnh
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching provinces");
        }
    }


    // Lấy danh sách các huyện theo tỉnh
    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<?> getDistricts(@PathVariable int provinceId) {
        try {
            // Gọi service để lấy danh sách các huyện
            List<Map<String, Object>> districts = addressService.getDistricts(provinceId);

            // Đảm bảo danh sách huyện có cấu trúc phù hợp cho frontend (id và name)
            List<Map<String, Object>> districtList = new ArrayList<>();
            for (Map<String, Object> district : districts) {
                Map<String, Object> districtData = new HashMap<>();
                districtData.put("id", district.get("DistrictID"));  // Đảm bảo id là DistrictID
                districtData.put("name", district.get("DistrictName"));  // Đảm bảo name là DistrictName
                districtList.add(districtData);
            }

            // Trả về danh sách huyện nếu thành công
            return ResponseEntity.ok(districtList);
        } catch (Exception e) {
            // In ra lỗi nếu có ngoại lệ xảy ra
            e.printStackTrace();
            // Trả về lỗi 500 nếu có sự cố khi lấy danh sách huyện
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching districts");
        }
    }


    @GetMapping("/wards/{districtId}")
    public ResponseEntity<?> getWards(@PathVariable int districtId) {
        try {
            // Gọi service để lấy danh sách các xã
            List<Map<String, Object>> wards = addressService.getWards(districtId);

            // Đảm bảo danh sách xã có cấu trúc phù hợp cho frontend (id và name)
            List<Map<String, Object>> wardList = new ArrayList<>();
            for (Map<String, Object> ward : wards) {
                Map<String, Object> wardData = new HashMap<>();
                wardData.put("id", ward.get("WardCode"));  // Đảm bảo id là WardID
                wardData.put("name", ward.get("WardName"));  // Đảm bảo name là WardName
                wardList.add(wardData);
            }

            // Trả về danh sách xã nếu thành công
            return ResponseEntity.ok(wardList);
        } catch (Exception e) {
            // In ra lỗi nếu có ngoại lệ xảy ra
            e.printStackTrace();
            // Trả về lỗi 500 nếu có sự cố khi lấy danh sách xã
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching wards");
        }
    }


//    // Lưu danh sách tỉnh thành vào cơ sở dữ liệu
//    @PostMapping("/save-provinces")
//    public ResponseEntity<String> saveProvinces() {
//        try {
//            addressService.saveProvinces();
//            return ResponseEntity.ok("Danh sách tỉnh thành đã được lưu thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Lỗi khi lưu danh sách tỉnh thành: " + e.getMessage());
//        }
//    }

//    // Lưu danh sách huyện vào cơ sở dữ liệu theo provinceId
//    @PostMapping("/save-districts/{provinceId}")
//    public ResponseEntity<String> saveDistricts(@PathVariable int provinceId) {
//        try {
//            addressService.saveDistricts(provinceId);
//            return ResponseEntity.ok("Danh sách huyện của tỉnh " + provinceId + " đã được lưu thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Lỗi khi lưu danh sách huyện: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/save-wards/{districtId}")
//    public ResponseEntity<String> saveWards(@PathVariable int districtId) {
//        try {
//            addressService.saveWards(districtId);
//
//            return ResponseEntity.ok("Danh sách xã của huyện " + districtId + " đã được lưu thành công.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Lỗi khi lưu danh sách xã: " + e.getMessage());
//        }
//    }
//

    // Endpoint để lưu địa chỉ vận chuyển cho người dùng
    @PostMapping("/save/address")
    public ResponseEntity<String> saveAddress(@RequestParam Integer userId,
                                              @RequestParam String cityCode,
                                              @RequestParam String districtCode,
                                              @RequestParam String wardCode) {
        try {
            addressService.saveCityDistrictWardToDB(userId, cityCode, districtCode, wardCode);
            return ResponseEntity.ok("Lưu địa chỉ vận chuyển thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lưu địa chỉ: " + e.getMessage());
        }
    }
}
