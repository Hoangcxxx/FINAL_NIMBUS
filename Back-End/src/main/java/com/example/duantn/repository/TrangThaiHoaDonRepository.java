package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon, Integer> {

    // Phương thức để tìm trạng thái hóa đơn theo idHoaDon và idLoaiTrangThai
    List<TrangThaiHoaDon> findByHoaDon_IdHoaDonAndLoaiTrangThai_IdLoaiTrangThai(Integer idHoaDon, Integer idLoaiTrangThai);
    @Modifying
    @Query("DELETE FROM TrangThaiHoaDon t WHERE t.hoaDon.idHoaDon = :hoaDonId")
    void deleteByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
}
