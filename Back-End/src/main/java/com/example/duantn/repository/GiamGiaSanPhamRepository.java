package com.example.duantn.repository;

import com.example.duantn.entity.DotGiamGia;
import com.example.duantn.entity.GiamGiaSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiamGiaSanPhamRepository extends JpaRepository<GiamGiaSanPham, Integer> {
    List<GiamGiaSanPham> findByDotGiamGia(DotGiamGia dotGiamGia);

}
