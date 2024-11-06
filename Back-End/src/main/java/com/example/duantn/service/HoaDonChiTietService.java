package com.example.duantn.service;

import com.example.duantn.dto.HoaDonChiTietDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.HoaDonChiTietRepository;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietService {

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public void createHoaDonChiTiet(HoaDonChiTietDTO hoaDonChiTietDTO) {

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTietDTO.getIdSanPhamChiTiet())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + hoaDonChiTietDTO.getIdSanPhamChiTiet()));


        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTietDTO.getIdHoaDon())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonChiTietDTO.getIdHoaDon()));


        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setSoLuong(hoaDonChiTietDTO.getSoLuong());
        hoaDonChiTiet.setTongTien(hoaDonChiTietDTO.getTongTien());


        hoaDonChiTietRepository.save(hoaDonChiTiet);
    }


}
