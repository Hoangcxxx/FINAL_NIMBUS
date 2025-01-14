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
                    .append("body { font-family: 'Arial', sans-serif; background-color: #fdf1d8; margin: 0; padding: 0; }")
                    .append(".email-container { max-width: 650px; margin: auto; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); }")
                    .append(".header { text-align: center; padding: 20px 0; background: linear-gradient(to right, #ff8c66, #ffd54f); border-radius: 10px 10px 0 0; }") // Màu đỏ Tết
                    .append(".header h1 { margin: 0; font-size: 28px; font-family: 'Times New Roman', serif; }") // Sử dụng font chữ truyền thống
                    .append(".content { padding: 20px; font-size: 16px; color: #333; line-height: 1.6; }")
                    .append(".content h2 { color: #e84d39; font-size: 24px; font-family: 'Times New Roman', serif; }") // Tiêu đề mừng Tết
                    .append(".order-details { margin-top: 20px; padding: 20px; border: 1px solid #f1c2b1; border-radius: 8px; background-color: #fff0e6; }") // Thêm màu sắc Tết cho phần chi tiết đơn hàng
                    .append(".order-item { margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 8px; background-color: #fff; }")
                    .append(".order-item span { display: block; margin-bottom: 6px; font-size: 16px; }")
                    .append(".footer { text-align: center; padding: 15px; font-size: 14px; color: #888888; background-color: #f7f7f7; border-radius: 0 0 10px 10px; }")
                    .append(".button { display: inline-block; padding: 12px 25px; margin-top: 20px; font-size: 16px; color: #fff; background-color: #e84d39; text-decoration: none; border-radius: 5px; transition: background-color 0.3s ease; }")
                    .append(".button:hover { background-color: #c94a3b; }") // Đổi màu nút bấm cho phù hợp với Tết
                    .append("</style>")
                    .append("</head>")
                    .append("<body>")
                    .append("<div class='container email-container'>")
                    .append("<div class='header'><h1>Xác Nhận Đơn Hàng</h1></div>")
                    .append("<div style='text-align: center;'>")
                    .append("<img src='https://mythuattanviet.com/wp-content/uploads/2024/09/ran-doi-non-2.jpg' alt='Logo NimBus Shop' style='width: 150px; height: 150px; border-radius: 50%; margin-top: 10px;'>")
                    .append("</div>")
                    .append("<div class='content'>")
                    .append("<h2>Chào bạn, " + hoaDonDTO.getTenNguoiNhan() + "</h2>")
                    .append("<p>Cảm ơn bạn đã tin tưởng và lựa chọn mua sắm tại <strong>NimBus Shop</strong> trong dịp cận Tết này!</p>")
                    .append("<p>Chúng tôi rất vui mừng được phục vụ bạn và hy vọng rằng những sản phẩm từ NimBus Shop sẽ mang đến niềm vui và sự hài lòng cho bạn trong năm mới.</p>")
                    .append("<p>Chúc bạn và gia đình một năm mới an khang thịnh vượng, sức khỏe dồi dào và luôn tràn ngập niềm vui!</p>")
                    .append("<p><strong>Thông tin đơn hàng:</strong></p>")
                    .append("<p><strong>Mã Đơn Hàng:</strong> " + latestMaHoaDon + "</p>")
                    .append("<p><strong>Phương Thức Thanh Toán:</strong> " + hoaDonDTO.getTenPhuongThucThanhToan() + "</p>")
                    .append("<div class='order-details'>");


// Tạo đối tượng DecimalFormat để định dạng số với dấu phân cách hàng nghìn
            DecimalFormat formatter = new DecimalFormat("#,###");

            for (SanPhamChiTietDTO sanPham : hoaDonDTO.getListSanPhamChiTiet()) {
                // Tính tổng tiền: Đơn giá × Số lượng
                BigDecimal tongTienSanPham = sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong()));

                // Khởi tạo biến cho số tiền giảm giá
                BigDecimal giamGia = BigDecimal.ZERO;

                // Kiểm tra kiểu giảm giá (true = VND, false = %)
                String voucherDisplay = "";
                if (GiaTrivoucher != null && GiaTrivoucher.compareTo(BigDecimal.ZERO) > 0) {
                    // Nếu có voucher và có giá trị hợp lệ
                    if (kieugiamgia) {
                        // Giảm giá theo VND
                        giamGia = GiaTrivoucher.multiply(BigDecimal.valueOf(sanPham.getSoLuong())); // Giảm giá = Voucher * Số lượng
                        voucherDisplay = "<span><strong>Voucher:</strong> " + formatter.format(GiaTrivoucher) + " VND</span>";
                    } else {
                        // Giảm giá theo %
                        giamGia = tongTienSanPham.multiply(GiaTrivoucher).divide(BigDecimal.valueOf(100)); // Giảm giá = Tổng tiền * phần trăm giảm
                        voucherDisplay = "<span><strong>Voucher:</strong> " + formatter.format(GiaTrivoucher) + " %</span>";
                    }
                } else {
                    // Nếu không có voucher, hiển thị là 0 VND
                    voucherDisplay = "<span><strong>Voucher:</strong> 0 VND</span>";
                }

                // Tính tổng tiền sau giảm giá
                BigDecimal tongTienSauGiamGia = tongTienSanPham.subtract(giamGia);

                // Thêm thông tin vào email content
                emailContent.append("<div class='order-item'>")
                        .append("<span><strong>Sản phẩm:</strong> " + tenSanPham + "</span>")
                        .append("<span><strong>Số lượng:</strong> " + sanPham.getSoLuong() + "</span>")
                        .append(voucherDisplay)
                        .append("<span><strong>Tổng tiền Sản Phẩm:</strong> " + formatter.format(tongTienSanPham) + " VND</span>")
                        .append("<span><strong>Số tiền giảm giá:</strong> " + formatter.format(giamGia) + " VND</span>")
                        .append("<span><strong>Tổng tiền sau giảm giá:</strong> " + formatter.format(tongTienSauGiamGia) + " VND</span>")
                        .append("<span><strong>Phi Vận Chuyển:</strong> " + formatter.format(phiship) + " VND</span>")
                        .append("</div>");
            }

            emailContent.append("</div>")
                    .append("<p><strong>Tổng giá trị đơn hàng:</strong> " + formatter.format(hoaDonDTO.getThanhTien()) + " VND</p>")
                    .append("<p>Chúng tôi rất vui mừng thông báo rằng đơn hàng của bạn đã được tiếp nhận và đang được xử lý. Đơn hàng của bạn sẽ được giao đến trong khoảng 3-5 ngày tới. Chúng tôi sẽ thông báo cho bạn ngay khi đơn hàng đã được gửi đi và trên đường đến tay bạn.</p>")
                    .append("<p>Mọi thắc mắc hay yêu cầu hỗ trợ, xin vui lòng liên hệ với chúng tôi qua số HOTLINE: <strong>0376941599</strong>. Chúng tôi luôn sẵn sàng hỗ trợ bạn.</p>")
                    .append("<a href='http://127.0.0.1:5502/#!/don_hang_cua_toi' class='button btn' style='background: linear-gradient(to right, #ff8c66, #ffd54f); padding: 10px 20px; border-radius: 5px; text-decoration: none;'>Tra Cứu Đơn Hàng</a>")

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
