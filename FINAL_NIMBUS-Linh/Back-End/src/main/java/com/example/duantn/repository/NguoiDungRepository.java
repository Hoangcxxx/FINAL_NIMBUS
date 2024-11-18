package com.example.duantn.repository;

import com.example.duantn.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findById(long id);
    List<NguoiDung> findByVaiTro_IdVaiTro(Integer idVaiTro);
    NguoiDung findByMaNguoiDung(String maNguoiDung);
    boolean existsByMaNguoiDung(String maNguoiDung);
}
