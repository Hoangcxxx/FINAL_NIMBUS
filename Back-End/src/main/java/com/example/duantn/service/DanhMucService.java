package com.example.duantn.service;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.DanhMuc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DanhMucService {
    @Autowired
    private com.example.duantn.repository.DanhMucRepository danhMucRepository;

    public List<DanhMuc> getAllDanhMuc() {
        // Lấy danh sách sắp xếp theo ngày cập nhật giảm dần
        return danhMucRepository.findAllByOrderByNgayCapNhatDesc();
    }
    public List<DanhMuc> searchDanhMucByTen(String tenDanhMuc) {
        return danhMucRepository.findByTenDanhMucContaining(tenDanhMuc);
    }
    public List<DanhMuc> getAllCategories() {
        return danhMucRepository.findAll();
    }

    public com.example.duantn.entity.DanhMuc createDanhMuc(com.example.duantn.entity.DanhMuc DanhMuc) {
        DanhMuc.setNgayTao(new Date());
        DanhMuc.setNgayCapNhat(new Date());
        return danhMucRepository.save(DanhMuc);
    }

    public com.example.duantn.entity.DanhMuc updateDanhMuc(Integer id, com.example.duantn.entity.DanhMuc DanhMucDetails) {
        com.example.duantn.entity.DanhMuc DanhMuc = danhMucRepository.findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        DanhMuc.setTenDanhMuc(DanhMucDetails.getTenDanhMuc());
        DanhMuc.setMoTa(DanhMucDetails.getMoTa());
        DanhMuc.setNgayCapNhat(new Date()); // Update timestamp
        return danhMucRepository.save(DanhMuc);
    }

    public void deleteDanhMuc(Integer id) {
        com.example.duantn.entity.DanhMuc DanhMuc = danhMucRepository.findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        danhMucRepository.delete(DanhMuc);
    }
}
