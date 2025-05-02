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

    public List<HoaDonChiTietDTO> createMultipleHoaDonChiTiet(List<HoaDonChiTietDTO> dtoList, Integer userId) {
        List<HoaDonChiTietDTO> createdHoaDonChiTietList = new ArrayList<>();

        // Tìm người dùng 1 lần duy nhất
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new ExpressionException("Người dùng không tìm thấy"));

        for (HoaDonChiTietDTO dto : dtoList) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            // Lấy sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(dto.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new ExpressionException("Sản phẩm chi tiết không tìm thấy"));

            // Lấy hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(dto.getIdHoaDon())
                    .orElseThrow(() -> new ExpressionException("Hóa đơn không tìm thấy"));

            // Lấy giá bán và giá khuyến mãi
            GiamGiaSanPham giamGiaSanPham = giamGiaSanPhamRepository.findBySanPham(sanPhamChiTiet.getSanPham())
                    .orElse(null);

            BigDecimal giaKhuyenMai = giamGiaSanPham != null ? giamGiaSanPham.getGiaKhuyenMai() : null;
            BigDecimal giaBan = sanPhamChiTiet.getSanPham().getGiaBan();

            BigDecimal giaTinh = (giaKhuyenMai != null) ? giaKhuyenMai : giaBan;

            BigDecimal tongTien = giaTinh.multiply(BigDecimal.valueOf(dto.getSoLuong()));


            // Thiết lập thông tin hóa đơn chi tiết
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(dto.getSoLuong());
            hoaDonChiTiet.setTongTien(tongTien);
            hoaDonChiTiet.setSoTienThanhToan(dto.getSoTienThanhToan());
            hoaDonChiTiet.setTienSanPham(giaKhuyenMai != null ? giaKhuyenMai : giaBan); // Lưu giá đã áp dụng
            hoaDonChiTiet.setTienTraLai(dto.getTienTraLai());
            hoaDonChiTiet.setMoTa(dto.getMoTa());
            hoaDonChiTiet.setNgayTao(new Date());
            hoaDonChiTiet.setNgayCapNhat(new Date());
            hoaDonChiTiet.setTrangThai(true);

            // Lưu hóa đơn chi tiết trước
            hoaDonChiTietRepository.save(hoaDonChiTiet);

            // Tạo lịch sử hóa đơn
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setNgayGiaoDich(new Date());
            lichSuHoaDon.setSoTienThanhToan(dto.getSoTienThanhToan());
            lichSuHoaDon.setNguoiDung(nguoiDung);
            lichSuHoaDonRepository.save(lichSuHoaDon);

            // Gán lịch sử hóa đơn vào hóa đơn chi tiết và cập nhật lại
            hoaDonChiTiet.setLichSuHoaDon(lichSuHoaDon);
            hoaDonChiTietRepository.save(hoaDonChiTiet);

            // Tạo DTO trả về
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