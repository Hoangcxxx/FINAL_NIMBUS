package com.example.duantn.service;

import com.example.duantn.entity.KichThuoc;
import com.example.duantn.entity.KichThuocChiTiet;
import com.example.duantn.repository.KichThuocChiTietRepository;
import com.example.duantn.repository.KichThuocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class KichThuocService {
    @Autowired
    private KichThuocRepository kichThuocRepository;
    @Autowired
    private KichThuocChiTietRepository kichThuocChiTietRepository;
    public List<com.example.duantn.entity.KichThuoc> getAllKichThuoc() {
        return kichThuocRepository.findAllByOrderByNgayCapNhatDesc();
    }
    public List<KichThuoc> searchKichThuocByTen(String tenKichThuoc) {
        return kichThuocRepository.findByTenKichThuocContaining(tenKichThuoc);
    }
    public KichThuoc createKichThuoc(KichThuoc kichThuoc) {
        // Thiết lập ngày tạo và ngày cập nhật cho KichThuoc
        Date currentDate = new Date();
        kichThuoc.setNgayTao(currentDate);
        kichThuoc.setNgayCapNhat(currentDate);

        // Lưu KichThuoc vào database
        KichThuoc savedKichThuoc = kichThuocRepository.save(kichThuoc);

        // Tạo KichThuocChiTiet liên kết với KichThuoc vừa lưu
        KichThuocChiTiet kichThuocChiTiet = new KichThuocChiTiet();
        kichThuocChiTiet.setKichThuoc(savedKichThuoc); // Gắn KichThuoc vào KichThuocChiTiet
        kichThuocChiTiet.setNgayTao(currentDate);
        kichThuocChiTiet.setNgayCapNhat(currentDate);

        // Lưu KichThuocChiTiet vào database
        kichThuocChiTietRepository.save(kichThuocChiTiet);

        return savedKichThuoc;
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
