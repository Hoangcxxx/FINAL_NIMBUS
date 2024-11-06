package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM HoaDon h WHERE h.idHoaDon = :id")
    void deleteByHoaDonId(@Param("id") Integer id);


}
