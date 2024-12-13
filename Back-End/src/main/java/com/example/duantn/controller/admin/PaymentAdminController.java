package com.example.duantn.controller.admin;

import com.example.duantn.config.Config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/admin/payment")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class PaymentAdminController {

    @PostMapping("/creat_payment")
    public void createPayment(
            @RequestParam(required = false, defaultValue = "0") long amount,
            @RequestParam String paymentMethod,
            HttpServletRequest req, HttpServletResponse response) throws IOException {

        String orderType = "other";

        // Lấy thông tin cần thiết
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Received amount: " + amount);

        // Tạo tham số VNPAY
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // VNPAY yêu cầu số tiền tính bằng đơn vị tiền tệ nhỏ nhất
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB"); // Hoặc mã ngân hàng khác nếu cần
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef + " - ");
        vnp_Params.put("vnp_OrderType", orderType);

        // Ngôn ngữ
        String locate = req.getParameter("language");
        vnp_Params.put("vnp_Locale", (locate != null && !locate.isEmpty()) ? locate : "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thời gian tạo và thời gian hết hạn
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Thời gian hết hạn 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Tạo hash data
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Tạo query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><head><title>Redirecting...</title></head>" +
                "<body><script type='text/javascript'>window.location.href='" + paymentUrl + "';</script>" +
                "<p>If you are not redirected, <a href='" + paymentUrl + "'>click here</a>.</p></body></html>");
    }


    @RequestMapping("/vnpay_return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String orderId = request.getParameter("vnp_TxnRef");
        if ("00".equals(transactionStatus)) {

            response.sendRedirect("http://127.0.0.1:5502/index.html");
        } else {
            response.sendRedirect("http://127.0.0.1:5500/admin.html#!/ban_hang?message=Thanh%20toan%20that%20bai");
        }
    }
}