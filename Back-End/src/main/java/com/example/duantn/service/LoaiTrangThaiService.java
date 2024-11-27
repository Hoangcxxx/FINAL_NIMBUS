package com.example.duantn.service;

import com.example.duantn.entity.LichSuHoaDon;
import com.example.duantn.entity.LoaiTrangThai;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiTrangThaiService {
    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;

    public List<LoaiTrangThai> getAllLoaiTrangThai() {
        return loaiTrangThaiRepository.findAll();
    }

}
