package com.example.duantn.service;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.MauSac;
import com.example.duantn.entity.MauSacChiTiet;
import com.example.duantn.repository.MauSacChiTietRepository;
import com.example.duantn.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private MauSacChiTietRepository mauSacChiTietRepository;
    public List<com.example.duantn.entity.MauSac> getAllMauSac() {
        return mauSacRepository.findAllByOrderByNgayCapNhatDesc();
    }
    public List<MauSac> searchMauSacByTen(String tenMauSac) {
        return mauSacRepository.findByTenMauSacContaining(tenMauSac);
    }

    public MauSac createMauSac(MauSac mauSac) {
        // Thiết lập ngày tạo và ngày cập nhật cho MauSac
        Date currentDate = new Date();
        mauSac.setNgayTao(currentDate);
        mauSac.setNgayCapNhat(currentDate);

        // Lưu MauSac vào database
        MauSac savedMauSac = mauSacRepository.save(mauSac);

        // Tạo MauSacChiTiet liên kết với MauSac vừa lưu
        MauSacChiTiet mauSacChiTiet = new MauSacChiTiet();
        mauSacChiTiet.setMauSac(savedMauSac); // Gắn MauSac vào MauSacChiTiet
        mauSacChiTiet.setNgayTao(currentDate);
        mauSacChiTiet.setNgayCapNhat(currentDate);

        // Lưu MauSacChiTiet vào database
        mauSacChiTietRepository.save(mauSacChiTiet);

        return savedMauSac;
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
