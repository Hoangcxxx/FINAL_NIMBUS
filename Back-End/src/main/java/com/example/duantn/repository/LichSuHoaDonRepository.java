package com.example.duantn.repository;

import com.example.duantn.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    List<LichSuHoaDon> findByNguoiDung_IdNguoiDungOrderByIdLichSuHoaDon(Integer nguoiDungId);

}
