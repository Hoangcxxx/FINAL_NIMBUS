package com.example.duantn.service;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LoaiTrangThai;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrangThaiHoaDonService {
    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;

    // Lấy tất cả trạng thái hóa đơn
    public List<TrangThaiHoaDon> getAllTrangThaiHoaDon() {
        return trangThaiHoaDonRepository.findAll();
    }

    // Lấy trạng thái hóa đơn theo ID của hóa đơn
    public TrangThaiHoaDon getTrangThaiHoaDonByHoaDonId(Integer hoaDonId) {
        return trangThaiHoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái của đơn hàng"));
    }

    // Tạo trạng thái hóa đơn mới
    public TrangThaiHoaDon createTrangThaiHoaDon(TrangThaiHoaDon trangThaiHoaDon) {
        return trangThaiHoaDonRepository.save(trangThaiHoaDon);
    }

    // Cập nhật trạng thái hóa đơn
    public TrangThaiHoaDon updateTrangThaiHoaDon(Integer id, TrangThaiHoaDon trangThaiHoaDon) {
        trangThaiHoaDon.setIdTrangThaiHoaDon(id);
        return trangThaiHoaDonRepository.save(trangThaiHoaDon);
    }

    // Xóa trạng thái hóa đơn
    public void deleteTrangThaiHoaDon(Integer id) {
        trangThaiHoaDonRepository.deleteById(id);
    }

    // Cập nhật trạng thái của đơn hàng
    public TrangThaiHoaDon capNhatTrangThai(HoaDon hoaDon, Integer loaiTrangThaiId) {
        // Lấy loại trạng thái từ ID
        LoaiTrangThai loaiTrangThai = loaiTrangThaiRepository.findById(loaiTrangThaiId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại trạng thái"));

        // Tạo đối tượng TrangThaiHoaDon mới
        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        trangThaiHoaDon.setHoaDon(hoaDon);
        trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
        trangThaiHoaDon.setMoTa("Đơn hàng đang được xử lý"); // Ví dụ mô tả
        trangThaiHoaDon.setNgayTao(new Date());
        trangThaiHoaDon.setNgayCapNhat(new Date());

        // Lưu vào cơ sở dữ liệu
        return trangThaiHoaDonRepository.save(trangThaiHoaDon);
    }
}
