package com.example.duantn.repository;

import com.example.duantn.entity.DoiTra;
import com.example.duantn.query.DoiTraQuery;
import com.example.duantn.query.DotGiamGiaQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoiTraRepository extends JpaRepository<DoiTra, Integer> {

    List<DoiTra> findByHoaDonIdHoaDon(Integer idHoaDon);  // Tìm danh sách đổi trả theo hóa đơn
    @Query(value = DoiTraQuery.GET_SAN_PHAM_DOI_TRA_BY_ID_HOA_DON, nativeQuery = true)
    List<Object[]> getAllSanPhamDoiTraByIdHoaDon(Integer idHoaDon);

    List<DoiTra> findByHoaDon_IdHoaDon(Integer idHoaDon);
}

