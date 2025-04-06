package com.example.duantn.service;

import com.example.duantn.dto.HoaDonChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private GiamGiaSanPhamRepository giamGiaSanPhamRepository;
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
    public List<Object[]> getAllTongHoaDonHomNay() {
        return hoaDonChiTietRepository.getAllTongHoaDonHomNay();
    }
    public List<Object[]> getAllTongSanPhamTrongThang() {
        return hoaDonChiTietRepository.getAllTongSanPhamTrongThang();
    }

    public List<Object[]> getAllSoluongLoaiTrangThaiHoaDon() {
        return hoaDonChiTietRepository.getAllSoluongLoaiTrangThaiHoaDon();
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

            GiamGiaSanPham giamGiaSanPham = giamGiaSanPhamRepository.findBySanPham(sanPhamChiTiet.getSanPham())
                    .orElse(null);

            BigDecimal giaKhuyenMai = giamGiaSanPham != null ? giamGiaSanPham.getGiaKhuyenMai() : null;
            BigDecimal giaBan = sanPhamChiTiet.getSanPham().getGiaBan();

            // Tính giá thực tế
            BigDecimal giaTinh = (giaKhuyenMai != null && giaKhuyenMai.compareTo(BigDecimal.ZERO) > 0)
                    ? giaKhuyenMai
                    : giaBan;

            // Tính tổng tiền
            BigDecimal tongTien = giaTinh.multiply(BigDecimal.valueOf(dto.getSoLuong()));
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(dto.getSoLuong());
            hoaDonChiTiet.setTongTien(tongTien);
            hoaDonChiTiet.setSoTienThanhToan(dto.getSoTienThanhToan());
            hoaDonChiTiet.setTienSanPham(giaBan);
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
                    hoaDonChiTiet.getTienSanPham(),
                    hoaDonChiTiet.getMoTa()
            );
            createdHoaDonChiTietList.add(createdDto);
        }
        return createdHoaDonChiTietList;
    }

}