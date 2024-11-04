package com.example.duantn.repository;

import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.GioHangChiTiet;
import com.example.duantn.entity.SanPhamChiTiet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> {
    List<GioHangChiTiet> findByGioHang(GioHang gioHang);
    void deleteByGioHang(GioHang gioHang);
    Optional<GioHangChiTiet> findByGioHangAndSanPhamChiTiet(GioHang gioHang, SanPhamChiTiet sanPhamChiTiet);
    @Modifying
    @Transactional
    @Query("DELETE FROM GioHangChiTiet g WHERE g.gioHang.idNguoiDung = :userId")
    void deleteByUserId(Long userId);
}
