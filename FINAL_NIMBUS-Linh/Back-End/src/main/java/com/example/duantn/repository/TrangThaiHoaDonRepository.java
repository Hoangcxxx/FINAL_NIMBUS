package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon, Integer> {
    @Modifying
    @Query("DELETE FROM TrangThaiHoaDon t WHERE t.hoaDon.id = :hoaDonId")
    void deleteByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
}
