package com.example.duantn.repository;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.query.HoaDonQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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
            "LEFT JOIN HoaDonChiTiet hdct ON hdct.hoaDon.idHoaDon = hd.idHoaDon " + // Truy vấn trực tiếp HoaDonChiTiet
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

    @Query(value = HoaDonQuery.GET_PHI_SHIP, nativeQuery = true)
    BigDecimal findtestphiship();

    @Query(value = HoaDonQuery.GET_Voucher, nativeQuery = true)
    BigDecimal hoadonvoucher();



    @Query("SELECT sp.tenSanPham " +
            "FROM HoaDon hd " +
            "JOIN hd.hoaDonChiTiets hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "WHERE hd.maHoaDon = :maHoaDon " +
            "ORDER BY hd.idHoaDon DESC")
    String findTenSanPhamTheoMaHoaDon(@Param("maHoaDon") String maHoaDon);
    // Tìm kiếm hóa đơn theo mã hóa đơn
    @Query(value = "WITH LatestStatus AS ( " +
            "SELECT " +
            "h.Id_hoa_don, " +
            "h.ma_hoa_don, " +
            "u.ten_nguoi_dung, " +
            "h.sdt_nguoi_nhan, " +
            "h.thanh_tien, " +
            "h.loai, " +
            "t.ngay_tao, " +
            "l.ten_loai_trang_thai, " +
            "ROW_NUMBER() OVER (PARTITION BY h.Id_hoa_don ORDER BY t.id_loai_trang_thai DESC) AS rn " +
            "FROM hoa_don h " +
            "JOIN trang_thai_hoa_don t ON h.Id_hoa_don = t.id_hoa_don " +
            "JOIN loai_trang_thai l ON l.id_loai_trang_thai = t.id_loai_trang_thai " +
            "JOIN nguoi_dung u ON h.id_nguoi_dung = u.id_nguoi_dung " +
            ") " +
            "SELECT " +
            "ls.Id_hoa_don, " +
            "ls.ma_hoa_don, " +
            "ls.ten_nguoi_dung, " +
            "ls.sdt_nguoi_nhan, " +
            "ls.thanh_tien, " +
            "ls.loai, " +
            "ls.ngay_tao, " +
            "ls.ten_loai_trang_thai " +
            "FROM LatestStatus ls " +
            "WHERE ls.rn = 1 " +
            "AND ls.ma_hoa_don LIKE %:maHoaDon% " +
            "ORDER BY ls.ngay_tao DESC, ls.Id_hoa_don", nativeQuery = true)
    List<Object[]> searchHoaDonByMaHoaDon(String maHoaDon);
}