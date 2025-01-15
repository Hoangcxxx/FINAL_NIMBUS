package com.example.duantn.controller.client;


import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/email")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam String recipientEmail, @RequestBody HoaDonDTO hoaDonDTO ) {
        try {
            // Gửi email
            emailService.sendEmail(recipientEmail, hoaDonDTO);

            // Trả về JSON object
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Email sent successfully to " + recipientEmail
            ));
        } catch (MessagingException e) {
            // Trả về lỗi trong JSON object
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error while sending email: " + e.getMessage()
            ));
        }
    }
}
