package com.example.duantn.service;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.repository.DiaChiVanChuyenRepository;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to retrieve all invoices
    public List<HoaDonDTO> getAllHoaDons() {
        return fetchHoaDonsWithDetails();
    }

    // Method to execute SQL query and map results to DTO
    private List<HoaDonDTO> fetchHoaDonsWithDetails() {
        String sql = "SELECT h.id_hoa_don, h.ma_hoa_don, h.ten_nguoi_nhan, h.phi_ship, " +
                "h.dia_chi, h.sdt_nguoi_nhan, d.tinh, d.huyen, d.xa, u.email " +
                "FROM hoa_don h " +
                "JOIN dia_chi_van_chuyen d ON h.id_dia_chi_van_chuyen = d.id_dia_chi_van_chuyen " +
                "JOIN nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung " +
                "WHERE h.trang_thai = 1";

        // Execute query and map results to DTO
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            HoaDonDTO dto = new HoaDonDTO();
            dto.setIdHoaDon(rs.getInt("id_hoa_don"));
            dto.setMaHoaDon(rs.getString("ma_hoa_don"));
            dto.setTenNguoiNhan(rs.getString("ten_nguoi_nhan"));
            dto.setPhiShip(rs.getDouble("phi_ship"));
            dto.setDiaChi(rs.getString("dia_chi"));
            dto.setSdtNguoiNhan(rs.getString("sdt_nguoi_nhan"));
            dto.setTinh(rs.getString("tinh"));
            dto.setHuyen(rs.getString("huyen"));
            dto.setXa(rs.getString("xa"));
            dto.setEmail(rs.getString("email"));
            return dto;
        });
    }
    public List<HoaDonDTO> getHoaDonsByMaHoaDon(String maHoaDon) {
        String sql = "SELECT h.Id_hoa_don, h.ma_hoa_don, h.ten_nguoi_nhan, h.phi_ship, " +
                "h.dia_chi, h.sdt_nguoi_nhan, d.tinh, d.huyen, d.xa, u.email " +
                "FROM hoa_don h " +
                "JOIN dia_chi_van_chuyen d ON h.id_dia_chi_van_chuyen = d.Id_dia_chi_van_chuyen " +
                "JOIN nguoi_dung u ON h.id_nguoi_dung = u.Id_nguoi_dung " +
                "WHERE h.trang_thai = 1 AND h.ma_hoa_don = ?";

        return jdbcTemplate.query(sql, new Object[]{maHoaDon}, (rs, rowNum) -> {
            HoaDonDTO dto = new HoaDonDTO();
            dto.setIdHoaDon(rs.getInt("Id_hoa_don"));
            dto.setMaHoaDon(rs.getString("ma_hoa_don"));
            dto.setTenNguoiNhan(rs.getString("ten_nguoi_nhan"));
            dto.setPhiShip(rs.getDouble("phi_ship"));
            dto.setDiaChi(rs.getString("dia_chi"));
            dto.setSdtNguoiNhan(rs.getString("sdt_nguoi_nhan"));
            dto.setTinh(rs.getString("tinh"));
            dto.setHuyen(rs.getString("huyen"));
            dto.setXa(rs.getString("xa"));
            dto.setEmail(rs.getString("email"));
            return dto;
        });
    }

    public void createOrder(HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = new HoaDon();

        // Generate auto-incremented maHoaDon
        long currentCount = hoaDonRepository.count();
        String generatedMaHoaDon = "HD0" + (currentCount + 1);
        hoaDon.setMaHoaDon(generatedMaHoaDon);

        // Convert DTO to Entity
        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());

        // Set DiaChiVanChuyen and NguoiDung entities if needed
        diaChiVanChuyenRepository.findById(hoaDonDTO.getIdDiaChiVanChuyen()).ifPresent(hoaDon::setDiaChiVanChuyen);
        nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung()).ifPresent(hoaDon::setNguoiDung);

        hoaDon.setTrangThai(true); // Set default status to true (active)

        // Save HoaDon to database
        hoaDonRepository.save(hoaDon);
    }

}
