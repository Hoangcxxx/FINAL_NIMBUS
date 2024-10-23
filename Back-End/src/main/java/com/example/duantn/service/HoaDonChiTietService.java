package com.example.duantn.service;

import com.example.duantn.dto.HoaDonChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HoaDonChiTietService {

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    public List<HoaDonChiTietDTO> createMultipleHoaDonChiTiet(List<HoaDonChiTietDTO> dtoList,Integer userId) {
        List<HoaDonChiTietDTO> createdHoaDonChiTietList = new ArrayList<>();
        for (HoaDonChiTietDTO dto : dtoList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            // Tìm kiếm người dùng
            NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                    .orElseThrow(() -> new ExpressionException("Người dùng không tìm thấy"));
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(dto.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new ExpressionException("Sản phẩm chi tiết không tìm thấy"));
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            HoaDon hoaDon = hoaDonRepository.findById(dto.getIdHoaDon())
                    .orElseThrow(() -> new ExpressionException("Hóa đơn không tìm thấy"));
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(dto.getSoLuong());
            hoaDonChiTiet.setTongTien(dto.getTongTien());
            hoaDonChiTiet.setSoTienThanhToan(dto.getSoTienThanhToan());
            hoaDonChiTiet.setTienTraLai(dto.getTienTraLai());
            hoaDonChiTiet.setMoTa(dto.getMoTa());
            Date now = new Date();
            hoaDonChiTiet.setNgayTao(now);
            hoaDonChiTiet.setNgayCapNhat(now);
            hoaDonChiTiet.setTrangThai(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNgayGiaoDich(now);
            lichSuHoaDon.setSoTienThanhToan(dto.getSoTienThanhToan());
            lichSuHoaDon.setNguoiDung(nguoiDung);
            lichSuHoaDonRepository.save(lichSuHoaDon);
            hoaDonChiTiet.setLichSuHoaDon(lichSuHoaDon);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            HoaDonChiTietDTO createdDto = new HoaDonChiTietDTO(
                    sanPhamChiTiet.getIdSanPhamChiTiet(),
                    hoaDon.getIdHoaDon(),
                    hoaDon.getMaHoaDon(),
                    hoaDonChiTiet.getSoLuong(),
                    hoaDonChiTiet.getTongTien(),
                    hoaDonChiTiet.getSoTienThanhToan(),
                    hoaDonChiTiet.getTienTraLai(),
                    hoaDonChiTiet.getMoTa()
            );
            createdHoaDonChiTietList.add(createdDto);
        }
        return createdHoaDonChiTietList;
    }
}
