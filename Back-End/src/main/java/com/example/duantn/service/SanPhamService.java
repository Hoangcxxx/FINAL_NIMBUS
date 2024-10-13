package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    public List<Object[]> getAllSanPhams() {
        return sanPhamRepository.getAllSanPham();
    }
    public List<Object[]> getAllSanPhamAD() {
        return sanPhamRepository.getAllSanPhamAD();
    }
    public List<Object[]> getSanPhamById(String idSanPham) {
        return sanPhamRepository.getSanPhamById(idSanPham);
    }

    public List<Object[]> getSanPhamsByDanhMuc(Integer idDanhMuc) {
        return sanPhamRepository.getSanPhamByDanhMuc(idDanhMuc); // Trả về danh sách từ repository
    }

    public SanPham updateSanPham(Integer idSanPham, SanPham sanPham) {
        sanPham.setIdSanPham(idSanPham);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer idSanPham) {
        sanPhamRepository.deleteById(idSanPham);
    }
    public void addSanPham(Integer idDanhMuc, String tenSanPham, String moTa, Date ngayTao,Date ngayCapNhat, Boolean trangThai) {
        sanPhamRepository.addSanPham(idDanhMuc, tenSanPham, moTa, ngayTao, ngayCapNhat, trangThai);
    }

}
