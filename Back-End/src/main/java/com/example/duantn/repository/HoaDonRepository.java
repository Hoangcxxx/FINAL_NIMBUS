package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.query.HoaDonQuery;
import com.example.duantn.query.SanPhamChiTietQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

        Optional<HoaDon> findByMaHoaDon(String maHoaDon);

        List<HoaDon> findByNguoiDung_IdNguoiDung(Integer idNguoiDung);




}
