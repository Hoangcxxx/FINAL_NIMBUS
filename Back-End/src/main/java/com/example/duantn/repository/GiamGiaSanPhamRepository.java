package com.example.duantn.repository;

import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.GiamGiaSanPham;
import com.example.duantn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GiamGiaSanPhamRepository extends JpaRepository<GiamGiaSanPham, Integer> {
    List<GiamGiaSanPham> findByDotGiamGia(DotGiamGia dotGiamGia);
    @Query("SELECT g.giaKhuyenMai FROM GiamGiaSanPham g WHERE g.sanPham.idSanPham = :sanPhamId AND CURRENT_TIMESTAMP BETWEEN g.dotGiamGia.ngayBatDau AND g.dotGiamGia.ngayKetThuc")
    Optional<BigDecimal> findGiaKhuyenMaiHienTaiBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
    Optional<GiamGiaSanPham> findBySanPham(SanPham sanPham);
    @Query("SELECT g.giaKhuyenMai FROM GiamGiaSanPham g " +
            "WHERE g.sanPham.idSanPham = :idSanPham " +
            "ORDER BY g.ngayCapNhat DESC")
    BigDecimal findGiaKhuyenMaiBySanPhamId(@Param("idSanPham") Integer idSanPham);
}
