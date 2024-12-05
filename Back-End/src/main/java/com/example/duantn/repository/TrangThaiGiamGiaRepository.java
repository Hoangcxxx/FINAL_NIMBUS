package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrangThaiGiamGiaRepository extends JpaRepository<TrangThaiGiamGia, Integer> {
}
