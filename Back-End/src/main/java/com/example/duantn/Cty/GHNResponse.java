package com.example.duantn.Cty;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GHNResponse {

    private int code; // Mã trạng thái của phản hồi API (200 nếu thành công)
    private String message; // Thông điệp từ API (nếu có lỗi)
    private List<ShippingService> data; // Danh sách các dịch vụ vận chuyển hoặc dữ liệu khác tùy vào yêu cầu

    // Lớp đại diện cho dịch vụ vận chuyển
    @Data
    public static class ShippingService {
        private int service_id; // Mã dịch vụ vận chuyển
        private String service_name; // Tên dịch vụ
        private String service_type; // Loại dịch vụ
        private double fee; // Phí vận chuyển
        private List<String> supported_zones; // Các khu vực hỗ trợ dịch vụ

        // Hàm khởi tạo để đảm bảo các thuộc tính không bị null và lấy dữ liệu từ API
        public ShippingService(int service_id, String service_name, String service_type, Double fee, List<String> supported_zones) {
            this.service_id = service_id;
            this.service_name = service_name != null ? service_name : "Không có tên dịch vụ";
            this.service_type = service_type != null ? service_type : "Không có loại dịch vụ";
            this.fee = fee != null ? fee : 0.0; // Nếu không có fee, mặc định là 0.0
            this.supported_zones = supported_zones != null ? supported_zones : List.of("Không có khu vực hỗ trợ");
        }
    }

    // Method to check and process the response body
    public static double processResponseBody(Map<String, Object> responseBody) {
        // Check if response body is valid and code is 200 (success)
        if (responseBody != null && responseBody.get("code").equals(200)) {
            // Thành công, xử lý tiếp
            // Process the data here if needed
            return 1.0; // Returning a positive result (for example)
        } else {
            // Log lỗi và trả về 0
            String errorMessage = responseBody != null ? responseBody.get("message").toString() : "Không có thông tin lỗi";
            // Log the error message (assumes you have a logger set up)
            System.err.println("Không thể tính cước phí: " + errorMessage);
            return 0.0; // Returning 0 to indicate failure
        }
    }
}
