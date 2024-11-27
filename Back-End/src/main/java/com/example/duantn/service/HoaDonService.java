package com.example.duantn.service;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.SanPhamChiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    public List<Object[]> getAllHoaDon() {
        return hoaDonRepository.getAllHoaDon();
    }
    public List<Object[]> getTrangThaiHoaDonByIdHoaDon(Integer idHoaDon) {
        return hoaDonRepository.getTrangThaiHoaDonByIdHoaDon(idHoaDon);
    }
    public Optional<HoaDon> getHoaDonById(Integer idHoaDon) {
        return hoaDonRepository.findById(idHoaDon);
    }
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;



    public List<HoaDonDTO> hienthi(String maHoaDon) {
        List<HoaDon> hoaDonList;

        if (maHoaDon != null && !maHoaDon.isEmpty()) {
            hoaDonList = hoaDonRepository.findByMaHoaDon(maHoaDon)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else {
            hoaDonList = hoaDonRepository.findAll();
        }

        return hoaDonList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HoaDonDTO convertToDTO(HoaDon hoaDon) {
        HoaDonDTO dto = new HoaDonDTO();

        dto.setIdHoaDon(hoaDon.getIdHoaDon());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setNgayTao(hoaDon.getNgayTao());
        dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
        dto.setDiaChi(hoaDon.getDiaChi());
//        dto.setPhiShip(hoaDon.getPhiShip().doubleValue());
        dto.setThanhTien(hoaDon.getThanhTien());
        dto.setGhiChu(hoaDon.getMoTa());


        // Thông tin người dùng
        if (hoaDon.getNguoiDung() != null) {
            dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
            dto.setEmail(hoaDon.getNguoiDung().getEmail());
        }

        // Thông tin địa chỉ vận chuyển
        if (hoaDon.getDiaChiVanChuyen() != null) {
            dto.setIdDiaChiVanChuyen(hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen());
            dto.setTinh(hoaDon.getDiaChiVanChuyen().getTinh().getIdTinh());
            dto.setHuyen(hoaDon.getDiaChiVanChuyen().getHuyen().getIdHuyen());
            dto.setXa(hoaDon.getDiaChiVanChuyen().getXa().getIdXa());
        }


        // Thông tin phương thức thanh toán
        if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
            dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
            dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
                    .getPhuongThucThanhToan()
                    .getTenPhuongThuc());
        }

        // Danh sách sản phẩm chi tiết
        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
        dto.setListSanPhamChiTiet(chiTietList.stream()
                .map(chiTiet -> {
                    SanPhamChiTietDTO spDTO = new SanPhamChiTietDTO();
                    spDTO.setIdspct(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
                    spDTO.setSoLuong(chiTiet.getSoLuong());
                    spDTO.setGiaTien(chiTiet.getTongTien());
                    spDTO.setTenkichthuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
                    spDTO.setTenmausac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
                    spDTO.setTenchatlieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
                    spDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
                    spDTO.setMoTa(chiTiet.getSanPhamChiTiet().getSanPham().getMoTa());
                    spDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getIdSanPham());
                    return spDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }

}
