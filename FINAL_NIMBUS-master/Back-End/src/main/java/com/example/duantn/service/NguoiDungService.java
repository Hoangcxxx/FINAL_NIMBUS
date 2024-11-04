package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findByVaiTro_IdVaiTro(2);
    }

    public List<NguoiDung> getAllNhanVienBanHang() {
        return nguoiDungRepository.findByVaiTro_IdVaiTro(3);
    }
    public NguoiDung addNguoiDung(NguoiDung nguoiDung) {
        nguoiDung.setMaNguoiDung("user" + System.currentTimeMillis());
        String email = nguoiDung.getTenNguoiDung().replaceAll(" ", "").toLowerCase() + "@gmail.com";
        nguoiDung.setEmail(email);
        nguoiDung.setMatKhau(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        return nguoiDungRepository.save(nguoiDung);
    }

    private String generatePassword() {
        return "password_" + System.currentTimeMillis();
    }
    private boolean isMaNguoiDungExist(String maNguoiDung) {

        return nguoiDungRepository.findByMaNguoiDung(maNguoiDung) != null;
    }

    public NguoiDung getNguoiDungById(Integer idNguoiDung) {
        return nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
    }

    public NguoiDung findById(long id) {
        return nguoiDungRepository.findById(id);
    }

}
