package com.example.duantn.repository;

import com.example.duantn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

//    // Tìm kiếm hóa đơn theo ID người dùng
//    @Query("SELECT h FROM HoaDon h WHERE h.nguoiDung.idNguoiDung = :idNguoiDung")
//    List<HoaDon> findByNguoiDungId(@Param("idNguoiDung") Integer idNguoiDung);
}
