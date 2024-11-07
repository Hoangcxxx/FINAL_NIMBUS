package com.example.duantn.repository;

import com.example.duantn.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByNguoiDung_IdNguoiDungOrderByIdGioHang(Integer idNguoiDung);

}
