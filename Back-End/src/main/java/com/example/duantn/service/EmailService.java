package com.example.duantn.service;


import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String recipientEmail, HoaDonDTO hoaDonDTO) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom("hieuntph33888@fpt.edu.vn","NimBus Shop");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("Thông Báo Xác Nhận Đơn Hàng");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html>")
                    .append("<head>")
                    .append("<link href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>")
                    .append("<style>")
                    .append("body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; }")
                    .append(".email-container { max-width: 650px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); }")
                    .append(".header { text-align: center; padding: 20px 0; background-color: #1a73e8; color: #fff; border-radius: 10px 10px 0 0; }")
                    .append(".header h1 { margin: 0; font-size: 28px; }")
                    .append(".content { padding: 20px; font-size: 16px; color: #333; line-height: 1.6; }")
                    .append(".content h2 { color: #1a73e8; font-size: 24px; }")
                    .append(".order-details { margin-top: 20px; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #fafafa; }")
                    .append(".order-item { margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 8px; background-color: #fff; }")
                    .append(".order-item span { display: block; margin-bottom: 6px; font-size: 16px; }")
                    .append(".footer { text-align: center; padding: 15px; font-size: 14px; color: #888888; background-color: #f7f7f7; border-radius: 0 0 10px 10px; }")
                    .append(".button { display: inline-block; padding: 12px 25px; margin-top: 20px; font-size: 16px; color: #fff; background-color: #1a73e8; text-decoration: none; border-radius: 5px; transition: background-color 0.3s ease; }")
                    .append(".button:hover { background-color: #0f59c8; }")
                    .append("@media only screen and (max-width: 600px) {")
                    .append(".email-container { padding: 20px; }")
                    .append(".button { font-size: 14px; padding: 10px 20px; }")
                    .append("}")
                    .append("</style>")
                    .append("</head>")
                    .append("<body>")
                    .append("<div class='email-container'>")
                    .append("<div class='header'><h1>Xác Nhận Đơn Hàng</h1></div>")
                    .append("<div class='content'>")
                    .append("<h2>Chào bạn,</h2>")
                    .append("<p>Cảm ơn bạn đã đặt hàng tại <strong>NimBus Shop</strong>! Đơn hàng của bạn đang được xử lý.</p>")
                    .append("<p><strong>Thông tin đơn hàng:</strong></p>")
                    .append("<div class='order-details'>");

            for (SanPhamChiTietDTO sanPham : hoaDonDTO.getListSanPhamChiTiet()) {
                emailContent.append("<div class='order-item'>")
                        .append("<span><strong>Sản phẩm:</strong> " + sanPham.getTenSanPham() + "</span>")
                        .append("<span><strong>Đơn giá:</strong> " + sanPham.getGiaTien() + " VND</span>")
                        .append("<span><strong>Số lượng:</strong> " + sanPham.getSoLuong() + "</span>")
                        .append("<span><strong>Thành tiền:</strong> " + sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong())) + " VND</span>")
                        .append("</div>");
            }

            emailContent.append("</div>")
                    .append("<p><strong>Tổng giá trị đơn hàng:</strong> " + hoaDonDTO.getThanhTien() + " VND</p>")
                    .append("<p>Chúng tôi sẽ thông báo cho bạn khi đơn hàng đã được gửi đi.</p>")
                    .append("<p>Mọi thắc mắc, xin vui lòng liên hệ: <strong>HOTLINE 0376941599</strong>.</p>")
                    .append("<a href='http://127.0.0.1:5500/#!/theogiodonhang' class='button'>Theo Dõi Đơn Hàng</a>")
                    .append("</div>")
                    .append("<div class='footer'>")
                    .append("<p>&copy; 2024 NimBus Shop. Mọi quyền được bảo lưu.</p>")
                    .append("</div>")
                    .append("</div>")
                    .append("</body>")
                    .append("</html>");

            mimeMessageHelper.setText(emailContent.toString(), true);
            javaMailSender.send(mimeMessage);
            System.out.println("Sent message successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
