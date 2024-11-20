package com.example.duantn.controller.client;

import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/nguoi_dung/payment")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HoaDonService hoaDonService;

    @PostMapping("/creat_payment")
    public void createPayment(
            @RequestParam(required = false, defaultValue = "0") long amount,
            @RequestParam String paymentMethod,
            HttpServletRequest req, HttpServletResponse response) throws IOException {

        String paymentUrl = paymentService.createPayment(amount, paymentMethod, req);

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><head><title>Redirecting...</title></head>" +
                    "<body><script type='text/javascript'>window.location.href='" + paymentUrl + "';</script>" +
                    "<p>If you are not redirected, <a href='" + paymentUrl + "'>click here</a>.</p></body></html>");
    }

    @RequestMapping("/vnpay_return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = paymentService.handleVnpayReturn(request);
        response.sendRedirect(redirectUrl);
    }
}
