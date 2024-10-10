package com.example.duantn.service;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository repository;

    public List<SanPhamChiTiet> getAll() {
        return repository.findAll();
    }

    public List<Object[]> getById(Integer idSanPhamCT) {
        return repository.getSanPhamById(idSanPhamCT);
    }
    public List<Object[]> getMauSacById(Integer idSanPhamCT) {
        return repository.getMauSacByIdSanPham(idSanPhamCT);
    }
    public List<Object[]> getKichThuocById(Integer idSanPhamCT) {
        return repository.getKichThuocByIdSanPham(idSanPhamCT);
    }
    public List<Object[]> getChatLieuById(Integer idSanPhamCT) {
        return repository.getChatLieuByIdSanPham(idSanPhamCT);
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
