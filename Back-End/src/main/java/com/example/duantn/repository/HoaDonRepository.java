package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.query.HoaDonQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value = HoaDonQuery.GET_ALL_HOA_DON, nativeQuery = true)
    List<Object[]> getAllHoaDon();
    Optional<HoaDon> findByMaHoaDon(String maHoaDon);


    List<HoaDon> findByNguoiDung_IdNguoiDung(Integer idNguoiDung);
}
