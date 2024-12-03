package com.example.duantn.repository;

import com.example.duantn.entity.TrangThaiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon, Integer> {

    // Phương thức để tìm trạng thái hóa đơn theo idHoaDon và idLoaiTrangThai
    List<TrangThaiHoaDon> findByHoaDon_IdHoaDonAndLoaiTrangThai_IdLoaiTrangThai(Integer idHoaDon, Integer idLoaiTrangThai);
}
