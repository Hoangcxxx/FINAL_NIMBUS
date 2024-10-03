package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }

    public SanPham getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public SanPham createSanPham(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public SanPham updateSanPham(Integer id, SanPham sanPham) {
        sanPham.setIdSanPham(id);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }
}
