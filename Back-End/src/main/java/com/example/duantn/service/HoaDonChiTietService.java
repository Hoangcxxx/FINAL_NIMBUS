package com.example.duantn.service;

import com.example.duantn.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

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

}
