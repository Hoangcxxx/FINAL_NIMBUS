package com.example.duantn.service;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository repository;

    public List<SanPhamChiTiet> getAll() {
        return repository.findAll();
    }

    public Optional<SanPhamChiTiet> getById(Integer id) {
        return repository.findById(id);
    }

    public SanPhamChiTiet create(SanPhamChiTiet sanPhamChiTiet) {
        return repository.save(sanPhamChiTiet);
    }

    public SanPhamChiTiet update(Integer id, SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setIdSanPhamChiTiet(id);
        return repository.save(sanPhamChiTiet);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
