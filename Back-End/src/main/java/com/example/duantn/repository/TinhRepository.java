package com.example.duantn.repository;

import com.example.duantn.entity.Tinh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TinhRepository  extends JpaRepository<Tinh, Integer> {
    Tinh findByIdTinh(Integer idTinh);  // Phương thức tìm kiếm tỉnh theo tên
}
