//package com.example.duantn.service;
//
//import com.example.duantn.config.Config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class VNPAYService {
//
//    @Autowired
//    private Config vnpayConfig;
//
//    public String createPaymentUrl(String amount, String orderInfo) throws Exception {
//        long amountLong = Long.parseLong(amount) * 100; // Chuyển đổi sang VND
//        String vnpTxnRef = getRandomNumber(8); // Mã giao dịch ngẫu nhiên
//        String vnpIpAddr = "127.0.0.1"; // Địa chỉ IP (có thể lấy từ request)
//
//        Map<String, String> vnpParams = new HashMap<>();
//        vnpParams.put("vnp_Version", "2.1.0");
//        vnpParams.put("vnp_Command", "pay");
//        vnpParams.put("vnp_TmnCode", vnpayConfig.tmnCode);
//        vnpParams.put("vnp_Amount", String.valueOf(amountLong));
//        vnpParams.put("vnp_CurrCode", "VND");
//        vnpParams.put("vnp_TxnRef", vnpTxnRef);
//        vnpParams.put("vnp_OrderInfo", orderInfo);
//        vnpParams.put("vnp_ReturnUrl", vnpayConfig.returnUrl);
//        vnpParams.put("vnp_IpAddr", vnpIpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        String vnpCreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
//        vnpParams.put("vnp_CreateDate", vnpCreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnpExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
//        vnpParams.put("vnp_ExpireDate", vnpExpireDate);
//
//        StringBuilder hashData = new StringBuilder();
//        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
//        Collections.sort(fieldNames);
//
//        for (String fieldName : fieldNames) {
//            String fieldValue = vnpParams.get(fieldName);
//            if (fieldValue != null && !fieldValue.isEmpty()) {
//                hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append("&");
//            }
//        }
//
//        String queryUrl = hashData.toString();
//        String secureHash = hmacSHA512(vnpayConfig.secretKey, queryUrl);
//        queryUrl += "vnp_SecureHash=" + secureHash;
//
//        return vnpayConfig.payUrl + "?" + queryUrl; // URL thanh toán
//    }
//
//    private String hmacSHA512(String key, String data) throws Exception {
//        Mac hmac512 = Mac.getInstance("HmacSHA512");
//        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//        hmac512.init(secretKey);
//        byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
//
//        StringBuilder sb = new StringBuilder(2 * result.length);
//        for (byte b : result) {
//            sb.append(String.format("%02x", b & 0xff));
//        }
//        return sb.toString();
//    }
//
//    private String getRandomNumber(int length) {
//        StringBuilder sb = new StringBuilder(length);
//        Random rnd = new Random();
//        for (int i = 0; i < length; i++) {
//            sb.append(rnd.nextInt(10)); // Tạo số ngẫu nhiên
//        }
//        return sb.toString();
//    }
//}
