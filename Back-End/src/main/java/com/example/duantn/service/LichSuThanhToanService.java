package com.example.duantn.service;

import com.example.duantn.dto.LichSuThanhToanRequest;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LichSuThanhToan;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.LichSuThanhToanRepository;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LichSuThanhToanService {

    @Autowired
    private LichSuThanhToanRepository lichSuThanhToanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public LichSuThanhToan createLichSuThanhToan(Integer idHoaDon, Integer idNguoiDung, Integer idNhanVien, BigDecimal soTienThanhToan) {
        // Tìm hóa đơn từ ID
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + idHoaDon));

        // Tìm người dùng từ ID
        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + idNguoiDung));
        hoaDon.setTrangThai(true);
        // Tạo mới Lịch sử thanh toán
        LichSuThanhToan lichSuThanhToan = new LichSuThanhToan();
        lichSuThanhToan.setHoaDon(hoaDon);
        lichSuThanhToan.setNguoiDung(nguoiDung);
        lichSuThanhToan.setIdNhanVien(idNhanVien); // Gán ID nhân viên
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
    public List<LichSuThanhToan> updateLichSuThanhToanByHoaDon(Integer idHoaDon, LichSuThanhToanRequest lichSuThanhToanRequest) {
        // Tìm tất cả lịch sử thanh toán của hóa đơn theo idHoaDon
        List<LichSuThanhToan> lichSuThanhToans = lichSuThanhToanRepository.findByHoaDon_IdHoaDon(idHoaDon);

        // Nếu không có lịch sử thanh toán cho hóa đơn này, trả về danh sách rỗng
        if (lichSuThanhToans.isEmpty()) {
            return List.of();  // Trả về danh sách rỗng
        }

        // Cập nhật các bản ghi đã tìm thấy
        for (LichSuThanhToan lichSuThanhToan : lichSuThanhToans) {
            lichSuThanhToan.setSoTienThanhToan(lichSuThanhToanRequest.getSoTienThanhToan());
            lichSuThanhToan.setNgayGiaoDich(lichSuThanhToanRequest.getNgayGiaoDich());
            lichSuThanhToan.setTrangThaiThanhToan(lichSuThanhToanRequest.getTrangThaiThanhToan());
            lichSuThanhToan.setIdNhanVien(lichSuThanhToanRequest.getIdNhanVien());
            lichSuThanhToan.setMoTa(lichSuThanhToanRequest.getMoTa());
            lichSuThanhToan.setNgayCapNhat(new Date()); // Cập nhật thời gian cập nhật
        }

        // Lưu lại tất cả các bản ghi đã cập nhật
        return lichSuThanhToanRepository.saveAll(lichSuThanhToans);
    }


}