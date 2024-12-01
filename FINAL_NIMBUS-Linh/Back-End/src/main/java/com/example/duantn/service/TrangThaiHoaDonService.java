package com.example.duantn.service;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LoaiTrangThai;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrangThaiHoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;

    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    public TrangThaiHoaDon createTrangThaiHoaDon(Integer idHoaDon) {
        // Lấy hóa đơn từ cơ sở dữ liệu
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Lấy loại trạng thái 5
        LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(5)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));

        // Tạo trạng thái hóa đơn loại 5
        TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
        trangThaiHoaDon1.setMoTa("Đã thanh toán thành công");
        trangThaiHoaDon1.setNgayTao(new Date());
        trangThaiHoaDon1.setNgayCapNhat(new Date());
        trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);
        trangThaiHoaDon1.setHoaDon(hoaDon);

        // Lưu trạng thái hóa đơn loại 5
        trangThaiHoaDonRepository.save(trangThaiHoaDon1);

        // Lấy loại trạng thái 8
        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(8)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));

        // Tạo trạng thái hóa đơn loại 8
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Hoàn thành ");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());
        trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);
        trangThaiHoaDon2.setHoaDon(hoaDon);

        // Lưu trạng thái hóa đơn loại 8
        trangThaiHoaDonRepository.save(trangThaiHoaDon2);

        // Trả về trạng thái loại 5 (hoặc bất kỳ trạng thái nào bạn muốn phản hồi)
        return trangThaiHoaDon1;
    }
}