package com.example.duantn.repository;

import com.example.duantn.entity.Xa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XaRepository extends JpaRepository<Xa, Integer> {
    // Tìm xã theo huyện
    List<Xa> findByHuyen_IdHuyen(Integer idHuyen);

}
