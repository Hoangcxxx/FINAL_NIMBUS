package com.example.duantn.repository;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT h.maHoaDon FROM HoaDon h ORDER BY h.maHoaDon DESC")
    List<String> findLastMaHoaDon();
    @Query("SELECT h.idHoaDon, h.maHoaDon, h.nguoiDung.tenNguoiDung, h.trangThai, h.ngayTao " +
            "FROM HoaDon h WHERE h.trangThai = false")
    List<Object[]> findHoaDonChuaThanhToan();
    List<HoaDon> findAllByTrangThaiFalse();
    HoaDon findTopByOrderByIdHoaDonDesc();

    @Query("SELECT new com.example.duantn.dto.HoaDonDTO( " +
            "hd.idHoaDon, " +

            "hd.maHoaDon, " +
            "nd.tenNguoiDung, " +
            "nd.sdtNguoiDung, " +
            "hd.ngayTao, " +
            "ptt.tenPhuongThuc, " +
            "SUM(hdct.tongTien), " +

            "hd.loai) " +
            "FROM HoaDon hd " +
            "JOIN hd.nguoiDung nd " +
            "LEFT JOIN hd.ptThanhToanHoaDon ptthd " +
            "LEFT JOIN ptthd.phuongThucThanhToan ptt " +
            "LEFT JOIN hd.hoaDonChiTietList hdct " +
            "GROUP BY  hd.idHoaDon, hd.maHoaDon, nd.tenNguoiDung, nd.sdtNguoiDung, hd.ngayTao, ptt.tenPhuongThuc, hd.loai")
    List<HoaDonDTO> getHoaDonWithDetails();

}
