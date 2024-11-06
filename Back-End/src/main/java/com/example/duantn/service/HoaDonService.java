package com.example.duantn.service;

import com.example.duantn.dto.DiaChiVanChuyenDTO;
import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
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
    private VoucherRepository voucherRepository;

    @Autowired
    private PhuongThucThanhToanHoaDonRepository phuongthucthanhtoanrp;

    @Autowired
    private TrangThaiHoaDonRepository trangthaihoadonrp;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HoaDonDTO> getAllHoaDons() {
        return fetchHoaDonsWithDetails();
    }

    private List<HoaDonDTO> fetchHoaDonsWithDetails() {
        String sql = "SELECT h.id_hoa_don, h.ma_hoa_don, h.ten_nguoi_nhan, h.phi_ship, " +
                "h.dia_chi, h.sdt_nguoi_nhan, d.tinh, d.huyen, d.xa, u.email " +
                "FROM hoa_don h " +
                "JOIN dia_chi_van_chuyen d ON h.id_dia_chi_van_chuyen = d.id_dia_chi_van_chuyen " +
                "JOIN nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung " +
                "WHERE h.trang_thai = 1";

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

    public void createOrder(HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = new HoaDon();


        long currentCount = hoaDonRepository.count();
        String generatedMaHoaDon = "HD00" + (currentCount + 1);
        hoaDon.setMaHoaDon(generatedMaHoaDon);

        hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
        hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
        hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());


        DiaChiVanChuyen diaChi = diaChiVanChuyenRepository.findById(hoaDonDTO.getIdDiaChiVanChuyen())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        hoaDon.setDiaChiVanChuyen(diaChi);

        NguoiDung nguoidung =  nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
        hoaDon.setNguoiDung(nguoidung);

        Voucher voucher =   voucherRepository.findById(hoaDonDTO.getIdvocher())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
        hoaDon.setVoucher(voucher);


        TrangThaiHoaDon trangthaihoadon =    trangthaihoadonrp.findById(hoaDonDTO.getIdtrangthaihoadon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trangthai"));
        hoaDon.setTrangThaiHoaDon(trangthaihoadon);



        hoaDon.setTrangThai(true);

        hoaDonRepository.save(hoaDon);
    }

    public List<HoaDonDTO> getHoaDonsByMaHoaDon(String maHoaDon) {
        String sql = "SELECT h.id_hoa_don, h.ma_hoa_don, h.ten_nguoi_nhan, h.phi_ship, " +
                "h.dia_chi, h.sdt_nguoi_nhan, d.tinh, d.huyen, d.xa, u.email " +
                "FROM hoa_don h " +
                "JOIN dia_chi_van_chuyen d ON h.id_dia_chi_van_chuyen = d.id_dia_chi_van_chuyen " +
                "JOIN nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung " +
                "WHERE h.trang_thai = 1 AND h.ma_hoa_don = ?";

        return jdbcTemplate.query(sql, new Object[]{maHoaDon}, (rs, rowNum) -> {
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

    public void delete(Integer id) {
        hoaDonRepository.deleteById(id);
    }
}
