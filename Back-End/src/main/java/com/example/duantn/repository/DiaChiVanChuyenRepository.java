package com.example.duantn.repository;

import com.example.duantn.entity.DiaChiVanChuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DiaChiVanChuyenRepository extends JpaRepository<DiaChiVanChuyen, Integer> {

    // Tìm địa chỉ vận chuyển theo idNguoiDung và lấy ra thông tin tỉnh, huyện, xã kèm theo
    @Query("SELECT dcvc " +
            "FROM DiaChiVanChuyen dcvc " +
            "JOIN dcvc.tinh t " +
            "JOIN dcvc.huyen h " +
            "JOIN dcvc.xa x " +
            "WHERE dcvc.nguoiDung.idNguoiDung = :idNguoiDung")
    List<DiaChiVanChuyen> findDiaChiByNguoiDungId(@Param("idNguoiDung") Integer idNguoiDung);

}
