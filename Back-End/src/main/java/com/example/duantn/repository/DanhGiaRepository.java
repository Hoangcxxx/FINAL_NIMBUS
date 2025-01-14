package com.example.duantn.repository;

import com.example.duantn.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    // Xóa tất cả các đánh giá liên quan đến sản phẩm
    @Transactional
    void deleteBySanPhamIdSanPham(Integer idSanPham);
}
