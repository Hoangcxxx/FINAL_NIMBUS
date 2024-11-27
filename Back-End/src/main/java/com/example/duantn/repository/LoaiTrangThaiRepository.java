package com.example.duantn.repository;

import com.example.duantn.entity.LoaiTrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiTrangThaiRepository extends JpaRepository<LoaiTrangThai, Integer> {
    // Các phương thức truy vấn có thể thêm vào đây
}
