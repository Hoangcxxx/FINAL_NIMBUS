package com.example.duantn.service;

import com.example.duantn.config.Config;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VnPaySservice {

    private static final String VNPAY_RETURN_URL = "http://localhost:8080/api/vnpay/payment-return";
    private static final String VNPAY_LOCALE = "vn";
    private static final String VNPAY_CURRENCY = "VND";
    private static final String VNPAY_ORDER_TYPE = "billpayment";
    private static final int PAYMENT_EXPIRATION_MINUTES = 15;

    // Method to create the payment URL
    public ResponseEntity<?> createPaymentUrl() throws Exception {
        long amount = 1000000; // Payment amount
        String vnp_TxnRef = Config.getRandomNumber(8);  // Generate random transaction reference
        String vnp_IpAddr = "27.73.209.243";  // Use a dynamic IP address in production

        Map<String, String> vnp_Params = buildPaymentParams(amount, vnp_TxnRef, vnp_IpAddr);

        // Build the query string and hash data
        StringBuilder query = buildQueryString(vnp_Params);
        String hashData = buildHashData(vnp_Params);

        // Generate secure hash
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData);
        query.append("vnp_SecureHash=").append(vnp_SecureHash);

        // Final payment URL
        String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();

        // Log and return the payment URL
        System.out.println("Payment URL: " + paymentUrl);
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Thành công");
        response.put("url", paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Helper method to build payment parameters
    private Map<String, String> buildPaymentParams(long amount, String vnp_TxnRef, String vnp_IpAddr) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.1");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", VNPAY_CURRENCY);
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", URLEncoder.encode("Thanh toán đơn hàng: " + vnp_TxnRef, StandardCharsets.UTF_8));
        vnp_Params.put("vnp_Locale", VNPAY_LOCALE);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_ReturnUrl", VNPAY_RETURN_URL);
        vnp_Params.put("vnp_OrderType", VNPAY_ORDER_TYPE);

        // Set creation and expiration times
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, PAYMENT_EXPIRATION_MINUTES);  // Payment expires in 15 minutes
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        return vnp_Params;
    }

    // Helper method to build the query string
    private StringBuilder buildQueryString(Map<String, String> vnp_Params) throws Exception {
        StringBuilder query = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                        .append("&");
            }
        }
        // Remove last "&"
        if (query.length() > 0) {
            query.setLength(query.length() - 1);
        }
        return query;
    }

    // Helper method to build hash data
    private String buildHashData(Map<String, String> vnp_Params) throws Exception {
        StringBuilder hashData = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                        .append("&");
            }
        }
        // Remove last "&"
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }
        return hashData.toString();
    }

    // Method to handle the payment return
    public ResponseEntity<?> handlePaymentReturn(@RequestParam Map<String, String> params) throws Exception {
        // Verify secure hash
        String vnp_SecureHash = params.get("vnp_SecureHash");
        String hashData = buildHashData(params);
        String calculatedHash = Config.hmacSHA512(Config.secretKey, hashData);

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
                return generateErrorResponse("Thanh toán không thành công, vui lòng thử lại.");
            }
        } else {
            // Invalid secure hash
            return generateErrorResponse("Dữ liệu bị giả mạo, giao dịch không hợp lệ.");
        }
    }

    // Helper method to generate error responses
    private ResponseEntity<?> generateErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
