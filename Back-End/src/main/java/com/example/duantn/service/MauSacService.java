package com.example.duantn.service;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.MauSac;
import com.example.duantn.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;

    public List<com.example.duantn.entity.MauSac> getAllMauSac() {
        return mauSacRepository.findAll();
    }
    public List<MauSac> searchMauSacByTen(String tenMauSac) {
        return mauSacRepository.findByTenMauSacContaining(tenMauSac);
    }

    public MauSac createMauSac(MauSac mauSac) {
        // Thiết lập ngày tạo là thời điểm hiện tại
        mauSac.setNgayTao(new Date());
        mauSac.setNgayCapNhat(new Date());
        return mauSacRepository.save(mauSac);
    }

    public com.example.duantn.entity.MauSac updateMauSac(Integer id, com.example.duantn.entity.MauSac mauSacDetails) {
        com.example.duantn.entity.MauSac mauSac = mauSacRepository.findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        mauSac.setTenMauSac(mauSacDetails.getTenMauSac());
        mauSac.setMoTa(mauSacDetails.getMoTa());
        mauSac.setNgayCapNhat(new Date()); // Update timestamp
        return mauSacRepository.save(mauSac);
    }

    public void deleteMauSac(Integer id) {
        com.example.duantn.entity.MauSac mauSac = mauSacRepository.findById(id).orElseThrow(() -> new RuntimeException("Color not found"));
        mauSacRepository.delete(mauSac);
    }
}
