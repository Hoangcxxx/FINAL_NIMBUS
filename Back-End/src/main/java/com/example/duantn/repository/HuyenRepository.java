package com.example.duantn.repository;

import com.example.duantn.entity.Huyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HuyenRepository extends JpaRepository<Huyen, Integer> {
    // Tìm huyện theo tỉnh
    List<Huyen> findByTinh_IdTinh(Integer idTinh);
}
