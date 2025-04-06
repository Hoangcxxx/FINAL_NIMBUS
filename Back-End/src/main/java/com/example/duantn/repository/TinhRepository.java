package com.example.duantn.repository;

import com.example.duantn.entity.Tinh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TinhRepository  extends JpaRepository<Tinh, Integer> {
    Tinh findByIdTinh(Integer idTinh);  // Phương thức tìm kiếm tỉnh theo tên
    // Tìm kiếm Tinh theo mã
    Optional<Tinh> findByMaTinh(String maTinh);

    List<Tinh> findAllByMaTinh(String maTinh);  // Trả về danh sách tỉnh có mã trùng
}
