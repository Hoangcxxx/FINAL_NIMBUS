package com.example.duantn.repository;

import com.example.duantn.entity.DiaChiVanChuyen;
import com.example.duantn.entity.Huyen;
import com.example.duantn.entity.Tinh;
import com.example.duantn.entity.Xa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface DiaChiVanChuyenRepository extends JpaRepository<DiaChiVanChuyen, Integer> {

    // Tìm địa chỉ vận chuyển theo idNguoiDung và lấy ra thông tin tỉnh, huyện, xã kèm theo
    @Query("SELECT dcvc " +
            "FROM DiaChiVanChuyen dcvc " +
            "JOIN dcvc.tinh t " +
            "JOIN dcvc.huyen h " +
            "JOIN dcvc.xa x " +
            "WHERE dcvc.nguoiDung.idNguoiDung = :idNguoiDung")
    List<DiaChiVanChuyen> findDiaChiByNguoiDungId(@Param("idNguoiDung") Integer idNguoiDung);



        Optional<DiaChiVanChuyen> findByTinhAndHuyenAndXa(Tinh tinh, Huyen huyen, Xa xa);



}
