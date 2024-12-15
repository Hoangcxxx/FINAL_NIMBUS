package com.example.duantn.repository;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.MauSac;
import com.example.duantn.query.MauSacQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {

    List<MauSac> findByTenMauSacContaining(String tenMauSac);
    List<MauSac> findAllByOrderByNgayCapNhatDesc();
}
