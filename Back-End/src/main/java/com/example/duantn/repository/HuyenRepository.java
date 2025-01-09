package com.example.duantn.repository;

import com.example.duantn.entity.Huyen;
import com.example.duantn.entity.Xa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HuyenRepository extends JpaRepository<Huyen, Integer> {
    // Tìm huyện theo tỉnh
    List<Huyen> findByTinh_IdTinh(Integer idTinh);
    // Tìm kiếm Huyen theo mã
    Optional<Huyen> findByMaHuyen(String maHuyen);

    @Query("SELECT h.idHuyen FROM Huyen h WHERE h.idHuyen = " +
            "(SELECT x.huyen.idHuyen FROM Xa x WHERE x.maXa = :wardCode)")
    Optional<String> findDistrictCodeByWardCode(@Param("wardCode") String wardCode);
}
