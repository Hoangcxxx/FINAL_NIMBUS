package com.example.duantn.repository;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    List<DanhMuc> findByTenDanhMucContaining(String tenDanhMuc);
    List<DanhMuc> findAllByOrderByNgayCapNhatDesc();
}
