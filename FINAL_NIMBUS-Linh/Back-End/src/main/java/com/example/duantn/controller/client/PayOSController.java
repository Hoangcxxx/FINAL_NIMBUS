package com.example.duantn.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/payos")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PayOSController {
    private final PayOS payOS;

    public PayOSController(PayOS payOS) {
        this.payOS = payOS;
    }

    // API tạo liên kết thanh toán
    @PostMapping("/create-payment-link")
    public ObjectNode createPaymentLink(@RequestBody Map<String, Object> request) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        try {
            String description = (String) request.get("description");
            String returnUrl = (String) request.get("returnUrl");
            String cancelUrl = (String) request.get("cancelUrl");
            int price = (int) request.get("price");

            String orderCode = String.valueOf(new Date().getTime()).substring(6);

            // Tạo PaymentData mà không sử dụng accountNumber
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(Long.parseLong(orderCode))
                    .amount(price)
                    .description(description)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            // Tạo liên kết thanh toán
            CheckoutResponseData checkoutData = payOS.createPaymentLink(paymentData);

            // Nếu bạn cần lấy accountNumber từ API trả về, có thể làm như sau:
            String accountNumber = checkoutData.getAccountNumber();  // Ví dụ lấy từ kết quả trả về nếu có

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", mapper.valueToTree(checkoutData));

            // Nếu bạn muốn gửi accountNumber trả về trong response
            response.put("accountNumber", accountNumber); // Đưa accountNumber vào response nếu cần
        } catch (Exception e) {
            response.put("error", -1);
            response.put("message", e.getMessage());
        }
        return response;
    }


    // API lấy thông tin đơn hàng
//    @GetMapping("/order/{orderId}")
//    public ObjectNode getOrderById(@PathVariable long orderId) {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode response = mapper.createObjectNode();
//        try {
//            PaymentLinkData orderData = payOS.getPaymentLinkInformation(orderId);
//            response.put("error", 0);
//            response.put("message", "success");
//            response.set("data", mapper.valueToTree(orderData));
//        } catch (Exception e) {
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//        }
//        return response;
//    }
//
//    // API hủy đơn hàng
//    @PutMapping("/order/{orderId}/cancel")
//    public ObjectNode cancelOrder(@PathVariable long orderId) {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode response = mapper.createObjectNode();
//        try {
//            PaymentLinkData canceledOrder = payOS.cancelPaymentLink(orderId, null);
//            response.put("error", 0);
//            response.put("message", "success");
//            response.set("data", mapper.valueToTree(canceledOrder));
//        } catch (Exception e) {
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//        }
//        return response;
//    }
//
//    // API xử lý webhook
//    @PostMapping("/webhook")
//    public ObjectNode handleWebhook(@RequestBody Webhook webhook) {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode response = mapper.createObjectNode();
//        try {
//            WebhookData webhookData = payOS.verifyPaymentWebhookData(webhook);
//
//            // Cập nhật accountNumber ở đây
//            String updatedAccountNumber = "555520112004";  // Cố định accountNumber mới
//            webhookData.setAccountNumber(updatedAccountNumber);  // Cập nhật giá trị accountNumber
//
//            response.put("error", 0);
//            response.put("message", "Webhook handled successfully");
//            response.set("data", mapper.valueToTree(webhookData));
//        } catch (Exception e) {
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//        }
//        return response;
////    }

}
