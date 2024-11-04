package com.example.duantn.repository;

import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByIdNguoiDung(int idNguoiDung);
    void deleteByIdNguoiDung(Integer idNguoiDung);
}
