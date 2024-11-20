package com.example.duantn.repository;

import com.example.duantn.entity.DiaChiVanChuyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaChiVanChuyenRepository extends JpaRepository<DiaChiVanChuyen, Integer> {
    List<DiaChiVanChuyen> findByTinh(String tinh);
    List<DiaChiVanChuyen> findByHuyen(String huyen);
    List<DiaChiVanChuyen> findByXa(String xa);
    Optional<DiaChiVanChuyen> findByTinhAndHuyenAndXa(String tinh, String huyen, String xa);
}
