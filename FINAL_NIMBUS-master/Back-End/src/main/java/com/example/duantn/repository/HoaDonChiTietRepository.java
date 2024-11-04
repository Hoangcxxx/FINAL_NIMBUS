package com.example.duantn.repository;

import com.example.duantn.dto.ThongKeDoanhThuDTO;
import com.example.duantn.dto.ThongKeTongDoanhThuDTO;
import com.example.duantn.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("SELECT new com.example.duantn.dto.ThongKeDoanhThuDTO(sp.tenSanPham, SUM(hdct.soLuong), SUM(hdct.tongTien)) " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "JOIN hdct.hoaDon hd " +
            "WHERE hd.trangThai = true " +
            "GROUP BY sp.tenSanPham " +
            "ORDER BY SUM(hdct.soLuong * hdct.tongTien) DESC")
    List<ThongKeDoanhThuDTO> thongKeDoanhThuSanPham();


    @Query("SELECT new com.example.duantn.dto.ThongKeTongDoanhThuDTO(" +
            "COUNT(DISTINCT sp.idSanPham), " +
            "SUM(hdct.soLuong), " +
            "SUM(hdct.tongTien)) " +
            "FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "JOIN hdct.hoaDon hd " +
            "WHERE hd.trangThai = true")
    ThongKeTongDoanhThuDTO thongKeTongDoanhThu();
    @Query("SELECT new com.example.duantn.dto.ThongKeDoanhThuDTO(sp.tenSanPham, SUM(hdct.soLuong), SUM(hdct.tongTien)) " +
            "FROM SanPham sp " +
            "JOIN HoaDonChiTiet hdct ON sp.idSanPham = hdct.sanPhamChiTiet.idSanPhamChiTiet " +
            "JOIN HoaDon hd ON hdct.hoaDon.idHoaDon = hd.idHoaDon " +
            "WHERE hd.trangThai = true " +
            "AND hd.ngayTao BETWEEN :fromDate AND :toDate " +
            "GROUP BY sp.tenSanPham")
    List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay(@Param("fromDate") Date fromDate,
                                                     @Param("toDate") Date toDate);
    @Query("SELECT new com.example.duantn.dto.ThongKeDoanhThuDTO(sp.tenSanPham, SUM(hdct.soLuong), SUM(hdct.tongTien)) " +
            "FROM SanPham sp " +
            "JOIN HoaDonChiTiet hdct ON sp.idSanPham = hdct.sanPhamChiTiet.idSanPhamChiTiet " +
            "JOIN HoaDon hd ON hdct.hoaDon.idHoaDon = hd.idHoaDon " +
            "WHERE hd.trangThai = true " +
            "AND sp.danhMuc.tenDanhMuc = :tenDanhMuc " +
            "GROUP BY sp.tenSanPham")
    List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoDanhMuc(@Param("tenDanhMuc") String tenDanhMuc);

}
