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
    @Query(value = HoaDonQuery.GET_VOUCHER_HOA_DON_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getVoucherHoaDonByIdHoaDon(Integer idHoaDon);
    @Query(value = HoaDonQuery.GET_SAN_PHAM_CHI_TIET_HOA_DON_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getSanPhamCTHoaDonByIdHoaDon(Integer idHoaDon);
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



    @Query("SELECT h, tthd, ttLoai FROM HoaDon h " +
            "JOIN TrangThaiHoaDon tthd ON tthd.hoaDon.idHoaDon = h.idHoaDon " +
            "JOIN LoaiTrangThai ttLoai ON tthd.loaiTrangThai.idLoaiTrangThai = ttLoai.idLoaiTrangThai " +
            "WHERE h.nguoiDung.idNguoiDung = :idNguoiDung AND " +
            "tthd.idTrangThaiHoaDon = (SELECT MAX(t.idTrangThaiHoaDon) FROM TrangThaiHoaDon t WHERE t.hoaDon.idHoaDon = h.idHoaDon)")
    List<Object[]> findHoaDonWithTrangThaiAndLoai(@Param("idNguoiDung") Integer idNguoiDung);


    @Query("SELECT h, tthd, ttLoai FROM HoaDon h " +
            "JOIN TrangThaiHoaDon tthd ON tthd.hoaDon.idHoaDon = h.idHoaDon " +
            "JOIN LoaiTrangThai ttLoai ON tthd.loaiTrangThai.idLoaiTrangThai = ttLoai.idLoaiTrangThai " +
            "WHERE h.maHoaDon = :maHoaDon AND " +
            "tthd.idTrangThaiHoaDon = (SELECT MAX(t.idTrangThaiHoaDon) FROM TrangThaiHoaDon t WHERE t.hoaDon.idHoaDon = h.idHoaDon)")
    List<Object[]> findHoaDonWithTrangThaiAndLoaiByMaHoaDon(@Param("maHoaDon") String maHoaDon);


    @Query(value = HoaDonQuery.GET_MA_HOA_DON, nativeQuery = true)
    String findLatestHoaDon();


}
