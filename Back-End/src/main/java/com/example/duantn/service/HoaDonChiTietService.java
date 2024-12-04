package com.example.duantn.service;

import com.example.duantn.dto.HoaDonChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    public List<Object[]> getAllThongKe() {
        return hoaDonChiTietRepository.getAllThongKe();
    }

    public List<Object[]> getAllSoLuongBanRa() {
        return hoaDonChiTietRepository.getAllTongSoLuongBanRa();
    }

    public List<Object[]> getAllSanPhamBanRa() {
        return hoaDonChiTietRepository.getAllSanPhamBanRa();
    }

    public List<Object[]> getAllTongDoanhThu() {
        return hoaDonChiTietRepository.getAllTongDoanhThu();
    }

    public Integer getSoLuongDonHangCho(LocalDate startDate, LocalDate endDate) {
        return hoaDonChiTietRepository.countDonHangCho(startDate, endDate);
    }

    public Integer getSoLuongDonHangDangGiao(LocalDate startDate, LocalDate endDate) {
        return hoaDonChiTietRepository.countDonHangDangGiao(startDate, endDate);
    }

    public Integer getSoLuongDonHangHoanThanh(LocalDate startDate, LocalDate endDate) {
        return hoaDonChiTietRepository.countDonHangHoanThanh(startDate, endDate);
    }

    public Integer getSoLuongDonHangHuyBo(LocalDate startDate, LocalDate endDate) {
        return hoaDonChiTietRepository.countDonHangHuyBo(startDate, endDate);
    }
    public Double findDoanhThuByMonthAndYear(int month, int year) {
        return hoaDonChiTietRepository.findDoanhThuByMonthAndYear(month, year);
    }

    public Double findDoanhThuByYear(int year) {
        return hoaDonChiTietRepository.findDoanhThuByYear(year);
    }
    public Integer getSoLuongDhCho() {
        return hoaDonChiTietRepository.countDonHangCho();
    }

    public Integer getSoLuongDhDangGiao() {
        return hoaDonChiTietRepository.countDonHangDangGiao();
    }

    public Integer getSoLuongDhHoanThanh() {
        return hoaDonChiTietRepository.countDonHangHoanThanh();
    }

    public Integer getSoLuongDhHuyBo() {
        return hoaDonChiTietRepository.countDonHangHuyBo();
    }
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
