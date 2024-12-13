package com.example.duantn.repository;

import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.Voucher;
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
    @Query(value = DotGiamGiaQuery.GET_SAN_PHAM_DA_GIAM_GIA_BY_ID_DOT_GIAM_GIA, nativeQuery = true)
    List<Object[]> getAllSanPhamDaGiamGia(Integer idDotGiamGia);
    Optional<DotGiamGia> findByTenDotGiamGia(String tenDotGiamGia);
    Optional<DotGiamGia> findDotGiamGiaByKieuGiamGia(Boolean kieuGiamGia);
    @Query(value = DotGiamGiaQuery.GET_SAN_PHAM_CHUA_GIAM_GIA_BY_DANH_MUC, nativeQuery = true)
    List<Object[]> getAllSanPhamChuaGiamGiaByDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);
    // Tìm kiếm theo tên voucher
    @Query("SELECT d FROM DotGiamGia d WHERE d.tenDotGiamGia LIKE %?1%")
    List<DotGiamGia> findByTenDotGiamGiaContaining(String tenVoucher);

    // Tìm kiếm theo kiểu giảm giá
    List<DotGiamGia> findByKieuGiamGia(Boolean kieuGiamGia);
}
