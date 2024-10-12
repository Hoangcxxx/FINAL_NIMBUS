package com.example.duantn.service;
import com.example.duantn.entity.SanPham;
import com.example.duantn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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

    public SanPham updateSanPham(Integer id, SanPham sanPham) {
        sanPham.setIdSanPham(id);
        return sanPhamRepository.save(sanPham);
    }

    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }
    public void addSanPham(Integer idDanhMuc, String tenSanPham, String moTa, BigDecimal giaBan, Date ngayTao,Date ngayCapNhat, Boolean trangThai) {
        sanPhamRepository.addSanPham(idDanhMuc, tenSanPham, moTa, giaBan, ngayTao, ngayCapNhat, trangThai);
    }

}
