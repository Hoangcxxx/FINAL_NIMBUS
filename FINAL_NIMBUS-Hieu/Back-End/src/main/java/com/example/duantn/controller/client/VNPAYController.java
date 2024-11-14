package com.example.duantn.controller.client;

import com.example.duantn.config.Config;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
public class VNPAYController {

    @GetMapping("/payment-url")
    public ResponseEntity<?> createPaymentUrl() throws Exception {
        long amount = 1000000; // Payment amount
        String vnp_TxnRef = Config.getRandomNumber(8);  // Generate random transaction reference
        String vnp_IpAddr = "27.73.209.243";  // Use a dynamic IP address in production

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.1");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", URLEncoder.encode("Thanh toán đơn hàng: " + vnp_TxnRef, StandardCharsets.UTF_8));  // URL encoding for order info
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/api/vnpay/payment-return");  // Return URL after payment
        vnp_Params.put("vnp_OrderType", "billpayment");

        // Set creation and expiration times
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);  // Payment expires in 15 minutes
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Build the hash data and query string
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append("&");
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append("&");
            }
        }

        // Remove last "&"
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }

        // Generate secure hash
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("vnp_SecureHash=").append(vnp_SecureHash);

        // Final payment URL
        String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();

        // Return payment URL in response
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Thành công");
        response.put("url", paymentUrl);

        System.out.println("Payment URL: " + paymentUrl);  // Log for debugging

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/payment-return")
    public ResponseEntity<?> handlePaymentReturn(@RequestParam Map<String, String> params) throws Exception {
        // Verify secure hash
        String vnp_SecureHash = params.get("vnp_SecureHash");
        StringBuilder hashData = new StringBuilder();
        params.forEach((key, value) -> {
            if (!key.equals("vnp_SecureHash") && value != null && !value.isEmpty()) {
                hashData.append(key).append("=").append(value).append("&");
            }
        });

        // Remove last "&"
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }

        // Calculate hash
        String calculatedHash = Config.hmacSHA512(Config.secretKey, hashData.toString());

        if (calculatedHash.equals(vnp_SecureHash)) {
            // Hash is valid, check transaction status
            String transactionStatus = params.get("vnp_ResponseCode");
            if ("00".equals(transactionStatus)) {
                // Successful transaction
                String txnRef = params.get("vnp_TxnRef");
                String amount = params.get("vnp_Amount");

                Map<String, String> response = new HashMap<>();
                response.put("status", "ok");
                response.put("message", "Thanh toán thành công!");
                response.put("transactionRef", txnRef);
                response.put("amount", amount);

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // Failed transaction
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Thanh toán không thành công, vui lòng thử lại.");

                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } else {
            // Invalid secure hash
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Dữ liệu bị giả mạo, giao dịch không hợp lệ.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
