package com.example.duantn.service;

import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrangThaiHoaDonService {

    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    // Lấy tất cả trạng thái hóa đơn
    public List<TrangThaiHoaDon> getAllTrangThaiHoaDon() {
        return trangThaiHoaDonRepository.findAll();
    }

    // Lấy trạng thái hóa đơn theo ID
    public Optional<TrangThaiHoaDon> getTrangThaiHoaDonById(Integer id) {
        return trangThaiHoaDonRepository.findById(id);
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
}
