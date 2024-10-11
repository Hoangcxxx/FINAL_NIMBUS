package com.example.duantn.repository;

import com.example.duantn.entity.HinhAnhSanPham;
import com.example.duantn.query.HinhAnhSanPhamQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {

    @Query(value = HinhAnhSanPhamQuery.GET_HINH_ANA_SAN_PHAM_BY_ID_SAN_PHAM, nativeQuery = true)
    List<Object[]> getHinhAnhSanPhamByIdSanPham(@Param("idSanPham") Integer idSanPham);
}
