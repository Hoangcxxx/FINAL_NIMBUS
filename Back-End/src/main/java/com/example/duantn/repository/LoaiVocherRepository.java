package com.example.duantn.repository;

import com.example.duantn.entity.LoaiVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiVocherRepository extends JpaRepository<LoaiVoucher, Integer> {
}
