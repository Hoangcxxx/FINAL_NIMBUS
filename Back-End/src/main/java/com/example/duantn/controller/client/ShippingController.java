//package com.example.duantn.controller.client;
//
//import com.example.duantn.Cty.GHNResponse;
//import com.example.duantn.service.GHNService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/nguoi_dung/shipping")
//public class ShippingController {
//
//    private final GHNService ghnService;
//
//    @Autowired
//    public ShippingController(GHNService ghnService) {
//        this.ghnService = ghnService;
//    }
//
//    // Lấy danh sách các dịch vụ vận chuyển có sẵn
//    @GetMapping("/available-services")
//    public ResponseEntity<List<GHNResponse.ShippingService>> getAvailableServices() {
//        int shopId = 5427817;  // ID shop thực tế
//        int fromDistrict = 1542;  // Quận Hà Đông (ID)
//        int toDistrict = 1442;  // Quận 1, TP.HCM (ID)
//
//        List<GHNResponse.ShippingService> availableServices = ghnService.getAvailableShippingServices(shopId, fromDistrict, toDistrict);
//
//        if (availableServices.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//        } else {
//            return ResponseEntity.ok(availableServices);
//        }
//    }
//
//    @PostMapping("/calculate-fee")
//    public ResponseEntity<Double> calculateShippingFee(
//            @RequestParam int shopId,
//            @RequestParam int fromDistrict,
//            @RequestParam int toDistrict,  // Đảm bảo tham số này được truyền đúng
//            @RequestParam int weight,      // Trọng lượng gói hàng
//            @RequestParam int serviceId) {
//
//        try {
//            // Kiểm tra giá trị của các tham số đầu vào
//            if (toDistrict <= 0 || fromDistrict <= 0 || shopId <= 0 || serviceId <= 0 || weight <= 0) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0.0); // Nếu có tham số không hợp lệ
//            }
//
//            // Gọi service để tính phí vận chuyển
//            double fee = ghnService.calculateShippingFee(shopId, fromDistrict, toDistrict, weight, serviceId);
//            return ResponseEntity.ok(fee);
//        } catch (Exception e) {
//            // Xử lý lỗi nếu có sự cố
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0.0);
//        }
//    }
//
//
//
//}
