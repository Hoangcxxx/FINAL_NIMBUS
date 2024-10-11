package com.example.duantn.repository;

import com.example.duantn.entity.SanPham;
import com.example.duantn.query.SanPhamQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = SanPhamQuery.BASE_QUERY, nativeQuery = true)
    List<Object[]> getAllSanPham();

    @Query(value = SanPhamQuery.GET_SAN_PHAM_BY_DANH_MUC, nativeQuery = true)
    List<Object[]> getSanPhamByDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);

    @Query(value = SanPhamQuery.GET_SAN_PHAM_BY_ID, nativeQuery = true)
    List<Object[]> getSanPhamById(@Param("idSanPham") String idSanPham);
}
