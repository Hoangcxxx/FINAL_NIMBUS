package com.example.duantn.repository;

import com.example.duantn.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Integer> {

    @Query("SELECT lst FROM LichSuThanhToan lst WHERE lst.hoaDon.idHoaDon = :idHoaDon")
    List<LichSuThanhToan> findByHoaDon_IdHoaDon(@Param("idHoaDon") Integer idHoaDon);

}
