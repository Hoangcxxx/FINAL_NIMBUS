package com.example.duantn.service;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LichSuThanhToan;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.LichSuThanhToanRepository;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class LichSuThanhToanService {

    @Autowired
    private LichSuThanhToanRepository lichSuThanhToanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public LichSuThanhToan createLichSuThanhToan(Integer idHoaDon, Integer idNguoiDung, BigDecimal soTienThanhToan) {
        // Tìm hóa đơn từ ID
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + idHoaDon));

        // Tìm người dùng từ ID
        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + idNguoiDung));
        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setNguoiDung(nguoiDung);
        lichSuThanhToan.setSoTienThanhToan(soTienThanhToan);
        lichSuThanhToan.setNgayTao(new Date());
        lichSuThanhToan.setNgayCapNhat(new Date());
        lichSuThanhToan.setNgayGiaoDich(new Date());
        lichSuThanhToan.setTrangThaiThanhToan(true);
        return lichSuThanhToanRepository.save(lichSuThanhToan);
    }
    public List<Object[]> getlichSuHoaDonByidHoaDon(Integer idHoaDon) {
        return lichSuThanhToanRepository.getLichSuThanhToanByidHoaDon(idHoaDon);
    }

}