package com.example.duantn.repository;

import com.example.duantn.entity.HoaDonChiTiet;
import com.example.duantn.query.HoaDonChiTietQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
    @Query(value = HoaDonChiTietQuery.GET_THONG_KE, nativeQuery = true)
    List<Object[]> getAllThongKe();
    @Query(value = HoaDonChiTietQuery.GET_TONG_SO_LUONG_BAN_RA, nativeQuery = true)
    List<Object[]> getAllTongSoLuongBanRa();
    @Query(value = HoaDonChiTietQuery.GET_TONG_SO_LUONG_BAN_RA_HOM_NAY, nativeQuery = true)
    List<Object[]> getAllSanPhamBanRa();
    @Query(value = HoaDonChiTietQuery.GET_TONG_HOA_DON_THANG_NAY, nativeQuery = true)
    List<Object[]> getAllTongDoanhThu();
    @Query(value = HoaDonChiTietQuery.GET_ALL_TONG_HOA_DON_HOM_NAY, nativeQuery = true)
    List<Object[]> getAllTongHoaDonHomNay();
    @Query(value = HoaDonChiTietQuery.GET_ALL_TONG_SAN_PHAM_TRONG_THANG, nativeQuery = true)
    List<Object[]> getAllTongSanPhamTrongThang();
    @Query(value = HoaDonChiTietQuery.GET_ALL_SO_LUONG_LOAI_TRANG_THAI_HOA_DON, nativeQuery = true)
    List<Object[]> getAllSoluongLoaiTrangThaiHoaDon();
    @Query(value = "SELECT COUNT(*) FROM hoa_don hd JOIN trang_thai_hoa_don th ON hd.id_trang_thai_hoa_don = th.Id_trang_thai_hoa_don WHERE th.Id_trang_thai_hoa_don = 1 AND hd.ngay_tao BETWEEN :startDate AND :endDate", nativeQuery = true)
    Integer countDonHangCho(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.idHoaDon = :hoaDonId")
    List<HoaDonChiTiet> findByHoaDon_IdHoaDon(@Param("hoaDonId") Integer hoaDonId);




}