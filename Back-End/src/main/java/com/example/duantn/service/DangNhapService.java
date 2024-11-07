package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.VaiTro;
import com.example.duantn.repository.NguoiDungRepository;
import com.example.duantn.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // Phương thức đăng ký
    public NguoiDung dangKy(NguoiDung nguoiDung) {
        // Mã hóa mật khẩu
        String matKhauMãHóa = passwordEncoder.encode(nguoiDung.getMatKhau());
        nguoiDung.setMatKhau(matKhauMãHóa);

        // Lấy vai trò "Khách hàng" (id_vai_tro = 2)
        VaiTro vaiTroKhachHang = vaiTroRepository.findById(2).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
        nguoiDung.setVaiTro(vaiTroKhachHang);

        // Cập nhật thời gian tạo và cập nhật
        nguoiDung.setNgayTao(LocalDateTime.now());
        nguoiDung.setNgayCapNhat(LocalDateTime.now());

        // Lưu người dùng vào cơ sở dữ liệu
        return nguoiDungRepository.save(nguoiDung);
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
