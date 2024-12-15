package com.example.duantn.service;

import com.example.duantn.entity.ThongBao;
import com.example.duantn.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongBaoService {
    @Autowired
    private ThongBaoRepository thongBaoRepository;

    // Phương thức lấy thông báo của người dùng theo idNguoiDung
    public List<ThongBao> getThongBaosByNguoiDungId(Integer idNguoiDung) {
        return thongBaoRepository.findByNguoiDung_IdNguoiDungOrderByNgayGuiDesc(idNguoiDung);  // Giả sử phương thức này sẽ được định nghĩa trong repository
    }

}
