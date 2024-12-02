package com.example.duantn.service;

import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.GioHangRepository;
import com.example.duantn.repository.NguoiDungRepository;
import com.example.duantn.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DangNhapService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Autowired
    private GioHangRepository gioHangRepository;


    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 5;
    private static final SecureRandom RANDOM = new SecureRandom();
    // Mã khôi phục (giả sử mã được lưu trong cơ sở dữ liệu hoặc trong bộ nhớ)
    private String recoveryCode;

    // Phương thức quên mật khẩu
    public String quenMatKhau(String email) {
        Optional<NguoiDung> nguoiDungOptional = nguoiDungRepository.findByEmail(email);
        if (!nguoiDungOptional.isPresent()) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        NguoiDung nguoiDung = nguoiDungOptional.get();
        recoveryCode = generateRecoveryCode();
        nguoiDungRepository.save(nguoiDung);

        // Gửi email cho người dùng với mã khôi phục
        List<String> emailList = new ArrayList<>();
        emailList.add(nguoiDung.getEmail()); // Thêm email của người dùng vào danh sách
        sendRecoveryEmail(emailList, recoveryCode);

        return "Mã khôi phục đã được gửi đến email của bạn.";
    }

    private String generateRecoveryCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    private void sendRecoveryEmail(List<String> emails, String recoveryCode) {
        for (String email : emails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Mã khôi phục mật khẩu");
            message.setText("Mã khôi phục của bạn là: " + recoveryCode);
            mailSender.send(message);
        }
    }


    // Phương thức xác nhận mã khôi phục
    public String xacnhandoimatkhau(String makhophuc) {
        if (makhophuc.equals(recoveryCode)) {
            return "Mã khôi phục hợp lệ. Bạn có thể đổi mật khẩu.";
        } else {
            throw new RuntimeException("Mã khôi phục không hợp lệ");
        }
    }

    // Phương thức đổi mật khẩu mới
    public String doiMatKhau(String email, String matKhauMoi) {
        Optional<NguoiDung> nguoiDungOptional = nguoiDungRepository.findByEmail(email);
        if (!nguoiDungOptional.isPresent()) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        NguoiDung nguoiDung = nguoiDungOptional.get();
        nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
        nguoiDungRepository.save(nguoiDung);

        return "Mật khẩu đã được đổi thành công";
    }

    // Phương thức tìm người dùng theo email
    public Optional<NguoiDung> findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    public NguoiDung dangKy(NguoiDung nguoiDung) {
        // Kiểm tra sự tồn tại của email
        if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Mã hóa mật khẩu và tạo mã khách hàng
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        nguoiDung.setMaNguoiDung(nguoiDung.getMaNguoiDung() == null ? generateUniqueCode() : nguoiDung.getMaNguoiDung());

        // Gán vai trò và thời gian tạo
        nguoiDung.setVaiTro(vaiTroRepository.findById(2).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại")));
        LocalDateTime now = LocalDateTime.now();
        nguoiDung.setNgayTao(new Date());
        nguoiDung.setNgayCapNhat(new Date());
        nguoiDung.setTrangThai(true);

        // Lưu người dùng vào cơ sở dữ liệu
        NguoiDung savedUser = nguoiDungRepository.save(nguoiDung);

        // Tạo giỏ hàng mới cho người dùng
        GioHang gioHang = new GioHang();
        gioHang.setNguoiDung(savedUser); // Gán người dùng vào giỏ hàng
        gioHang.setTrangThai(true); // Giỏ hàng có thể ở trạng thái "đang sử dụng"
        gioHang.setNgayTao(new Date()); // Ngày tạo giỏ hàng là ngày hiện tại
        gioHang.setNgayCapNhat(new Date()); // Ngày cập nhật giỏ hàng

        // Lưu giỏ hàng vào cơ sở dữ liệu
        gioHangRepository.save(gioHang);

        // Trả về người dùng đã lưu
        return savedUser;
    }


    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (nguoiDungRepository.existsByMaNguoiDung(code)); // Kiểm tra tính duy nhất
        return code;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    // Phương thức đăng nhập
    public NguoiDung dangNhap(String email, String matKhau) {
        // Tìm người dùng theo email
        Optional<NguoiDung> nguoiDungOptional = nguoiDungRepository.findByEmail(email);
        if (nguoiDungOptional.isPresent()) {
            NguoiDung nguoiDung = nguoiDungOptional.get();

            // Kiểm tra mật khẩu
            if (passwordEncoder.matches(matKhau, nguoiDung.getMatKhau())) {
                // Nếu mật khẩu đúng, trả về thông tin người dùng và vai trò của họ
                if (nguoiDung.getVaiTro() != null) {
                    return nguoiDung; // Trả về người dùng và vai trò của họ
                } else {
                    throw new RuntimeException("Vai trò người dùng không hợp lệ");
                }
            } else {
                throw new RuntimeException("Mật khẩu không đúng");
            }
        } else {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
    }




}
