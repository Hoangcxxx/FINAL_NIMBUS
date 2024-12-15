package com.example.duantn.repository;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Integer> {

    List<KichThuoc> findByTenKichThuocContaining(String tenKichThuoc);
    List<KichThuoc> findAllByOrderByNgayCapNhatDesc();
}
