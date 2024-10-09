package com.example.duantn.service;

import com.example.duantn.entity.KichThuoc;
import com.example.duantn.repository.KichThuocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KichThuocService {
    @Autowired
    private KichThuocRepository kichThuocRepository;

    public List<KichThuoc> getAllKichThuoc() {
        return kichThuocRepository.findAll();
    }

    public KichThuoc createKichThuoc(KichThuoc kichThuoc) {
        kichThuoc.setNgayTao(new Date());
        kichThuoc.setNgayCapNhat(new Date());
        return kichThuocRepository.save(kichThuoc);
    }

    public KichThuoc updateKichThuoc(Integer id, KichThuoc kichThuocDetails) {
        KichThuoc kichThuoc = kichThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found"));
        kichThuoc.setTenKichThuoc(kichThuocDetails.getTenKichThuoc());
        kichThuoc.setMoTa(kichThuocDetails.getMoTa());
        kichThuoc.setNgayCapNhat(new Date());
        return kichThuocRepository.save(kichThuoc);
    }

    public void deleteKichThuoc(Integer id) {
        KichThuoc kichThuoc = kichThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found"));
        kichThuocRepository.delete(kichThuoc);
    }

    // Thêm phương thức lấy chi tiết kích thước
    public KichThuoc getKichThuocById(Integer id) {
        return kichThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found"));
    }
}
