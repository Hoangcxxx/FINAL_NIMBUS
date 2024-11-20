package com.example.duantn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    @Value("${PAYOS_API_KEY}")
    private String apiKey;

    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;

    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;

    private final RestTemplate restTemplate;

    public QRCodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Phương thức để xác minh thanh toán từ PayOS
    public boolean verifyPayment(String transactionId) {
        // Cấu trúc URL kiểm tra thanh toán của PayOS (Giả định có endpoint này)
        String verifyUrl = "https://api.payos.vn/v1/payment/verify";

        // Thông tin xác thực yêu cầu
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("transactionId", transactionId);
        requestBody.put("clientId", clientId);
        requestBody.put("apiKey", apiKey);
        requestBody.put("checksum", generateChecksum(transactionId));

        // Thực hiện yêu cầu HTTP POST để xác minh thanh toán
        try {
            Map<String, Object> response = restTemplate.postForObject(verifyUrl, requestBody, Map.class);

            // Kiểm tra kết quả phản hồi từ PayOS
            if (response != null && response.get("success") != null && (boolean) response.get("success")) {
                return true; // Thanh toán thành công
            } else {
                return false; // Thanh toán không thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trường hợp lỗi khi gọi API
        }
    }

    // Phương thức tạo checksum cho yêu cầu (Giả sử bạn sử dụng HMAC hoặc SHA256)
    private String generateChecksum(String transactionId) {
        // Cách tính checksum có thể thay đổi tùy vào yêu cầu của PayOS
        return transactionId + checksumKey; // Đây là ví dụ giả lập
    }
}
