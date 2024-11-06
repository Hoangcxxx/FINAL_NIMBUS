package com.example.duantn.repository;

import com.example.duantn.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    @Query(value = "SELECT * FROM nguoi_dung nd WHERE nd.id_vai_tro = :roleId", nativeQuery = true)
    List<NguoiDung> getSanPhamById(int roleId);
    Optional<NguoiDung> findByEmail(String email);

    Optional<NguoiDung> findBySdt(String sdt);

}
