package com.example.duantn.repository;

import com.example.duantn.entity.GioHang;
import com.example.duantn.query.GioHangQuery;
import com.example.duantn.query.SanPhamQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Query(value = GioHangQuery.BASE_QUERY, nativeQuery = true)
    List<Object[]> getAllGioHang(Integer idNguoiDung);
    GioHang findByNguoiDung_IdNguoiDungOrderByIdGioHang(Integer idNguoiDung);
    GioHang findByNguoiDung_IdNguoiDung(Integer idNguoiDung);
}
