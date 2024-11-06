package com.example.duantn.repository;

import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.query.DotGiamGiaQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    @Query(value = DotGiamGiaQuery.GET_SAN_PHAM_CHUA_GIAM_GIA, nativeQuery = true)
    List<Object[]> getAllSanPhamChuaGiamGia();
    Optional<DotGiamGia> findByTenDotGiamGia(String tenDotGiamGia);
    Optional<DotGiamGia> findDotGiamGiaByKieuGiamGia(Boolean kieuGiamGia);
    @Query(value = DotGiamGiaQuery.GET_SAN_PHAM_CHUA_GIAM_GIA_BY_DANH_MUC, nativeQuery = true)
    List<Object[]> getAllSanPhamChuaGiamGiaByDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);
}
