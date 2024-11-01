package com.example.duantn.repository;


import com.example.duantn.entity.HoaDonChiTiet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {

//        List<HoaDonChiTiet> findByHoaDon_IdAndId(Integer hoaDonId, Integer hoadonctId);

        @Query("SELECT CASE WHEN COUNT(h) > 0 THEN TRUE ELSE FALSE END "
                + "FROM HoaDonChiTiet h WHERE h.hoaDon.idHoaDon = :hoaDonId "
                + "AND h.idHoaDonChiTiet = :hoaDonChiTietId")
        Boolean existsByHoaDonIdAndHoaDonChiTietId(@Param("hoaDonId") Integer hoaDonId,
                                                   @Param("hoaDonChiTietId") Integer hoaDonChiTietId);
    }





