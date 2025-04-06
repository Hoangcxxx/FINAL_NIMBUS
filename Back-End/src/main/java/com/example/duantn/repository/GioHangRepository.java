package com.example.duantn.repository;

import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.query.GioHangQuery;
import com.example.duantn.query.SanPhamQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query(value = GioHangQuery.BASE_QUERY, nativeQuery = true)
    List<Object[]> getAllGioHang(Integer idNguoiDung);
    GioHang findByNguoiDung_IdNguoiDungOrderByIdGioHang(Integer idNguoiDung);
    GioHang findByNguoiDung_IdNguoiDung(Integer idNguoiDung);
    @Query("SELECT g FROM GioHang g WHERE g.nguoiDung.idNguoiDung = :idNguoiDung AND g.trangThai = true")
    GioHang findByIdNguoiDung(@Param("idNguoiDung") Integer idNguoiDung);


    @Query("SELECT gch FROM GioHangChiTiet gch WHERE gch.gioHang.idGioHang = :gioHangId")
    List<GioHangChiTiet> findGioHangChiTietByGioHangId(Integer gioHangId);

}
