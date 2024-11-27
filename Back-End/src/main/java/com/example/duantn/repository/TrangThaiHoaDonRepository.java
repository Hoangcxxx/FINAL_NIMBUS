package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon, Integer> {
}
