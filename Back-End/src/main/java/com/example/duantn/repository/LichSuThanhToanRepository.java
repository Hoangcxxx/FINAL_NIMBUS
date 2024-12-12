package com.example.duantn.repository;

import com.example.duantn.entity.LichSuThanhToan;
import com.example.duantn.query.LichSuThanhToanQuery;
import com.example.duantn.query.SanPhamQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan,Integer> {
    @Query(value = LichSuThanhToanQuery.GET_LICH_SU_THANH_TOAN_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getLichSuThanhToanByidHoaDon(Integer idHoaDon);

}
