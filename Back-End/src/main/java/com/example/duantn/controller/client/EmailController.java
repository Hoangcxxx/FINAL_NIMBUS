package com.example.duantn.controller.client;

import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String recipientEmail, @RequestBody HoaDonDTO hoaDonDTO) {
        try {
            // Send email by passing both recipient and HoaDonDTO
            emailService.sendEmail(recipientEmail, hoaDonDTO);
            return "Email sent successfully to " + recipientEmail;
        } catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }
}
