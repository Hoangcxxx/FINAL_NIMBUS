package com.example.duantn.controller.client;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.service.HoaDonService;

import com.example.duantn.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RestController
@RequestMapping("/api/nguoi_dung/vnpays")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class vnpayController {


    @Autowired
    private VnpayService paymentService;

    @PostMapping("/create_payment")
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
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response, HoaDonDTO hoaDonDTO) throws IOException {
        String redirectUrl = paymentService.handleVnpayReturn(request,hoaDonDTO);
        response.sendRedirect(redirectUrl);
    }
}