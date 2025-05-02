package com.example.duantn.service;


import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.HoaDonChiTietRepository;
import com.example.duantn.repository.HoaDonRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String recipientEmail, HoaDonDTO hoaDonDTO) throws MessagingException {
        try {

            // Lấy mã hóa đơn mới nhất
            String latestMaHoaDon = hoaDonRepository.findLatestHoaDon();
            if (latestMaHoaDon != null) {
                hoaDonDTO.setMaHoaDon(latestMaHoaDon);
            }

            BigDecimal phiship = hoaDonRepository.findtestphiship();
            if (phiship != null) {
                hoaDonDTO.setPhiShip(phiship);
            }
            String tenSanPham = hoaDonRepository.findTenSanPhamTheoMaHoaDon(latestMaHoaDon);
            System.out.println("Tên sản phẩm theo mã hóa đơn: " + tenSanPham);

            BigDecimal GiaTrivoucher = hoaDonRepository.hoadonvoucher();
            BigDecimal giaTriGiảmGia = BigDecimal.ZERO; // Mặc định giá trị giảm giá là 0 VND
            boolean kieugiamgia = false; // Mặc định kiểu giảm giá là false (giảm theo phần trăm)


            if (GiaTrivoucher != null && GiaTrivoucher.compareTo(BigDecimal.ZERO) > 0) {
                // Kiểm tra nếu voucher là giá tiền
                if (GiaTrivoucher.compareTo(BigDecimal.valueOf(100)) > 0) {  // Ví dụ, nếu giá trị voucher lớn hơn 100 VND thì coi như là giảm giá bằng tiền
                    kieugiamgia = true;
                    giaTriGiảmGia = GiaTrivoucher; // Cập nhật giá trị giảm giá là số tiền voucher
                } else {
                    kieugiamgia = false; // Voucher là kiểu giảm giá theo phần trăm
                    giaTriGiảmGia = GiaTrivoucher; // Cập nhật giá trị giảm giá là phần trăm
                }
            }

// Cập nhật giá trị voucher và kiểu giảm giá vào DTO
            hoaDonDTO.setGiaTriMavoucher(giaTriGiảmGia);
            hoaDonDTO.setKieuGiamGia(kieugiamgia);

//            FileSystemResource file = new FileSystemResource(new File("D:/FINAL_NIMBUS-Linh/FINAL_NIMBUS-Linh/FINAL_NIMBUS-Linh/Fond-End/admin/view-admin/assets/image/favicon1.png"));
//            if (file.exists()) {
//                mimeMessageHelper.addInline("logoImage", file);
//            } else {
//                System.out.println("File không tồn tại: " + file.getPath());
//            }


            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom("hieuntph33888@fpt.edu.vn", "NimBus Shop");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("Thông Báo Xác Nhận Đơn Hàng");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<html>")
                    .append("<head>")
                    .append("<link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>")
                    .append("<style>")
                    .append("body { font-family: 'Arial', sans-serif; background-color: #f2f4f6; margin: 0; padding: 0; }")
                    .append(".email-container { max-width: 650px; margin: auto; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); }")
                    .append(".header { text-align: center; padding: 20px 0; background: linear-gradient(to right, #2193b0, #6dd5ed); border-radius: 10px 10px 0 0; }")
                    .append(".header h1 { margin: 0; font-size: 28px; font-weight: bold; color: #ffffff; }")
                    .append(".content { padding: 20px; font-size: 16px; color: #333; line-height: 1.6; }")
                    .append(".content h2 { color: #2b5876; font-size: 24px; }")
                    .append(".order-details { margin-top: 20px; padding: 20px; border: 1px solid #d1ecf1; border-radius: 8px; background-color: #f1f9ff; }")
                    .append(".order-item { margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 8px; background-color: #fff; }")
                    .append(".order-item span { display: block; margin-bottom: 6px; font-size: 16px; }")
                    .append(".footer { text-align: center; padding: 15px; font-size: 14px; color: #888888; background-color: #f7f7f7; border-radius: 0 0 10px 10px; }")
                    .append(".button { display: inline-block; padding: 12px 25px; margin-top: 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px; transition: background-color 0.3s ease; }")
                    .append(".button:hover { background-color: #0056b3; }")
                    .append("</style>")
                    .append("</head>")
                    .append("<body>")
                    .append("<div class='container email-container'>")
                    .append("<div class='header'><h1>Xác Nhận Đơn Hàng</h1></div>")
                    .append("<div style='text-align: center;'>")
                    .append("<img src='https://mythuattanviet.com/wp-content/uploads/2024/09/ran-doi-non-2.jpg' alt='Logo NimBus Shop' style='width: 150px; height: 150px; border-radius: 50%; margin-top: 10px;'>")
                    .append("</div>")
                    .append("<div class='content'>")
                    .append("<h2>Xin chào, " + hoaDonDTO.getTenNguoiNhan() + "</h2>")
                    .append("<p>Cảm ơn bạn đã đặt hàng tại <strong>NimBus Shop</strong>.</p>")
                    .append("<p>Chúng tôi rất vinh dự được đồng hành cùng bạn trong hành trình mua sắm và cam kết mang đến sản phẩm chất lượng cùng trải nghiệm dịch vụ tốt nhất.</p>")
                    .append("<p>Dưới đây là thông tin đơn hàng của bạn:</p>")
                    .append("<p><strong>Mã Đơn Hàng:</strong> " + latestMaHoaDon + "</p>")
                    .append("<p><strong>Phương Thức Thanh Toán:</strong> " + hoaDonDTO.getTenPhuongThucThanhToan() + "</p>")
                    .append("<div class='order-details'>");



// Tạo đối tượng DecimalFormat để định dạng số với dấu phân cách hàng nghìn
            DecimalFormat formatter = new DecimalFormat("#,###");

            for (SanPhamChiTietDTO sanPham : hoaDonDTO.getListSanPhamChiTiet()) {
                // Tính tổng tiền sản phẩm
                BigDecimal tongTienSanPham = sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong()));

// Khởi tạo biến cho số tiền giảm giá
                BigDecimal giamGia = BigDecimal.ZERO;

// Kiểm tra kiểu giảm giá (true = VND, false = %)
                String voucherDisplay = "0";
                if (GiaTrivoucher != null && GiaTrivoucher.compareTo(BigDecimal.ZERO) > 0) {
                    if (kieugiamgia) {
                        // Giảm giá theo VND
                        giamGia = GiaTrivoucher.multiply(BigDecimal.valueOf(sanPham.getSoLuong()));
                        if (giamGia.compareTo(tongTienSanPham) > 0) {
                            giamGia = tongTienSanPham;
                        }
                        voucherDisplay = formatter.format(GiaTrivoucher) + " VND";
                    } else {
                        // Giảm giá theo %
                        giamGia = tongTienSanPham.multiply(GiaTrivoucher).divide(BigDecimal.valueOf(100));
                        voucherDisplay = GiaTrivoucher.stripTrailingZeros().toPlainString() + "%";
                    }
                }


// Tính tổng tiền sau giảm giá
                BigDecimal tongTienSauGiamGia = tongTienSanPham.subtract(giamGia);

// Nếu tổng tiền sau giảm giá nhỏ hơn 0 thì đặt lại là 0 VND
                if (tongTienSauGiamGia.compareTo(BigDecimal.ZERO) < 0) {
                    tongTienSauGiamGia = BigDecimal.ZERO;
                }

// Tính phí vận chuyển
                BigDecimal phiVanChuyen = phiship != null ? phiship : BigDecimal.ZERO;

// Tính tổng thanh toán (sau khi giảm giá + phí vận chuyển)
                BigDecimal tongThanhToan = tongTienSauGiamGia.add(phiVanChuyen);



// Thêm thông tin vào email content
                emailContent.append("<div class='order-item'>")
                        .append("<span><strong>Sản phẩm:</strong> " + tenSanPham + "</span>")
                        .append("<span><strong>Số lượng:</strong> " + sanPham.getSoLuong() + "</span>")
                        .append("<span><strong>Tổng tiền Sản Phẩm:</strong> " + formatter.format(tongTienSanPham) + " VND</span>")
                        .append("<span><strong>Voucher:</strong> " + voucherDisplay + "</span>")
                        .append("<span><strong>Tổng tiền sau giảm giá:</strong> " + formatter.format(tongTienSauGiamGia) + " VND (Giảm " + formatter.format(giamGia) + " VND)</span>")  // Hiển thị thông tin giảm giá tại đây
                        .append("<span><strong>Phí giao hàng:</strong> " + formatter.format(phiVanChuyen) + " VND</span>")
                        .append("<span><strong>Tổng thanh toán:</strong> " + formatter.format(tongThanhToan) + " VND</span>")
                        .append("</div>");




            }

            emailContent.append("</div>")
                    .append("<p><strong>Tổng giá trị đơn hàng:</strong> " + formatter.format(hoaDonDTO.getThanhTien()) + " VND</p>")
                    .append("<p>Chúng tôi rất vui mừng thông báo rằng đơn hàng của bạn đã được tiếp nhận và đang được xử lý. Đơn hàng của bạn sẽ được giao đến trong khoảng 3-5 ngày tới. Chúng tôi sẽ thông báo cho bạn ngay khi đơn hàng đã được gửi đi và trên đường đến tay bạn.</p>")
                    .append("<p>Mọi thắc mắc hay yêu cầu hỗ trợ, xin vui lòng liên hệ với chúng tôi qua số HOTLINE: <strong>0376941599</strong>. Chúng tôi luôn sẵn sàng hỗ trợ bạn.</p>")
                    .append("<a href='http://127.0.0.1:5502/#!/don_hang_cua_toi' class='button btn' style='background: linear-gradient(to right, #2193b0, #6dd5ed); padding: 10px 20px; border-radius: 5px; text-decoration: none; color: white;'>Tra Cứu Đơn Hàng</a>")
                    .append("</div>")
                    .append("<div class='footer'>")
                    .append("<p>&copy; 2024 NimBus Shop. Cảm ơn bạn đã lựa chọn chúng tôi. Mọi quyền được bảo lưu.</p>")
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
