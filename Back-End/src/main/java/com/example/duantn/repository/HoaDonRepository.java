package com.example.duantn.repository;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.query.HoaDonQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value = HoaDonQuery.GET_ALL_HOA_DON, nativeQuery = true)
    List<Object[]> getAllHoaDon();
    Optional<HoaDon> findByMaHoaDon(String maHoaDon);
    @Query(value = HoaDonQuery.GET_TRANG_THAI_HOA_DON_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getTrangThaiHoaDonByIdHoaDon(Integer idHoaDon);
    List<HoaDon> findByNguoiDung_IdNguoiDung(Integer idNguoiDung);
    @Query("SELECT h.maHoaDon FROM HoaDon h ORDER BY h.maHoaDon DESC")
    List<String> findLastMaHoaDon();
    @Query("SELECT h.idHoaDon, h.maHoaDon, h.nguoiDung.tenNguoiDung, h.trangThai, h.ngayTao " +
            "FROM HoaDon h WHERE h.trangThai = false")
    List<Object[]> findHoaDonChuaThanhToan();
    List<HoaDon> findAllByTrangThaiFalse();

    @Query(value = "SELECT new com.example.duantn.dto.HoaDonBanHangDTO( " +
            "hd.idHoaDon, " +
            "hd.maHoaDon, " +
            "nd.tenNguoiDung, " +
            "nd.sdt, " +
            "hd.ngayTao, " +
            "ptt.tenPhuongThuc, " +
            "SUM(hdct.tongTien), " +
            "hd.loai) " +
            "FROM HoaDon hd " +
            "JOIN hd.nguoiDung nd " +
            "LEFT JOIN hd.phuongThucThanhToanHoaDon ptthd " +
            "LEFT JOIN ptthd.phuongThucThanhToan ptt " +
            "LEFT JOIN hd.hoaDonChiTietList hdct " +
            "GROUP BY  hd.idHoaDon, hd.maHoaDon, nd.tenNguoiDung, nd.sdt, hd.ngayTao, ptt.tenPhuongThuc, hd.loai")
    List<HoaDonDTO> getHoaDonWithDetails();

}
