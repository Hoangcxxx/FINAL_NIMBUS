package com.example.duantn.service;


import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public HoaDonChiTiet taoChiTietHoaDon(HoaDonChiTiet hoaDonChiTiet) {
        hoaDonChiTiet.setNgayTao(hoaDonChiTiet.getNgayTao());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    public Boolean layChiTietTheoHoaDon(Integer hoaDonId, Integer hoaDonChiTietId) {
        return hoaDonChiTietRepository.existsByHoaDonIdAndHoaDonChiTietId(hoaDonId,hoaDonChiTietId);
    }
}
