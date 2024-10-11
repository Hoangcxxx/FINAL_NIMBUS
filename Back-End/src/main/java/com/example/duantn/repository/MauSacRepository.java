package com.example.duantn.repository;

import com.example.duantn.entity.MauSac;
import com.example.duantn.query.MauSacQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {

    @Query(value = MauSacQuery.GET_ALL_MAU_SAC, nativeQuery = true)
    List<Object[]> getAllMauSac();

    List<MauSac> findByTenMauSacContaining(String tenMauSac);
}
