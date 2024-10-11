package com.example.duantn.repository;

import com.example.duantn.entity.KichThuoc;
import com.example.duantn.query.KichThuocQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Integer> {

    @Query(value = KichThuocQuery.GET_ALL_KICH_THUOC, nativeQuery = true)
    List<Object[]> getAllKichThuoc();
}
