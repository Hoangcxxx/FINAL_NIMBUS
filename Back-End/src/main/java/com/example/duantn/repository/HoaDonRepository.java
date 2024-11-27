package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.query.HoaDonQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query(value = HoaDonQuery.GET_ALL_HOA_DON, nativeQuery = true)
    List<Object[]> getAllHoaDon();
    Optional<HoaDon> findByMaHoaDon(String maHoaDon);
    @Query(value = HoaDonQuery.GET_TRANG_THAI_HOA_DON_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getTrangThaiHoaDonByIdHoaDon(Integer idHoaDon);
}
