package com.example.duantn.service;

import com.example.duantn.dto.HoaDonCTDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public HoaDonChiTiet taoChiTietHoaDon(HoaDonChiTiet hoaDonChiTiet) {
        hoaDonChiTiet.setNgayTao(hoaDonChiTiet.getNgayTao());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    public List<HoaDonChiTiet> layChiTietTheoHoaDon(Integer hoaDonId) {
        return hoaDonChiTietRepository.findByHoaDonId(hoaDonId);
    }
}
