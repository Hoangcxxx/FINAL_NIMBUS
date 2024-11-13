package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.VaiTro;
import com.example.duantn.repository.NguoiDungRepository;
import com.example.duantn.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DangNhapService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

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
        nguoiDung.setNgayTao(now);
        nguoiDung.setNgayCapNhat(now);
        nguoiDung.setTrangThai(true);
        return nguoiDungRepository.save(nguoiDung);
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
        Optional<NguoiDung> nguoiDungOptional = nguoiDungRepository.findByEmail(email);
        if (nguoiDungOptional.isPresent()) {
            NguoiDung nguoiDung = nguoiDungOptional.get();
            if (passwordEncoder.matches(matKhau, nguoiDung.getMatKhau())) {
                return nguoiDung; // Trả về người dùng nếu đăng nhập thành công
            } else {
                throw new RuntimeException("Mật khẩu không đúng");
            }
        } else {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
    }

}
