package com.example.duantn.repository;

import com.example.duantn.entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    List<PhuongThucThanhToan> findByTrangThai(Boolean trangThai);
    PhuongThucThanhToan findByMaThanhToan(String maThanhToan);
}
