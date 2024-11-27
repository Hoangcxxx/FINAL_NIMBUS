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

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;
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
}
