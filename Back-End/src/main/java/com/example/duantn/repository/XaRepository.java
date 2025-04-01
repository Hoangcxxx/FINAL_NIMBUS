package com.example.duantn.repository;

import com.example.duantn.entity.Xa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface XaRepository extends JpaRepository<Xa, Integer> {
    // Tìm xã theo huyện
    List<Xa> findByHuyen_IdHuyen(Integer idHuyen);
    // Tìm kiếm Xa theo mã
    Optional<Xa> findByMaXa(String maXa);

    List<Xa> findAllByMaXa(String maXa);  // Trả về danh sách xã có mã trùng
}
