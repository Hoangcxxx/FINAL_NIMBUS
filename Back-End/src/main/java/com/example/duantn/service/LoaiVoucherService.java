package com.example.duantn.service;

import com.example.duantn.entity.LoaiVoucher;
import com.example.duantn.repository.LoaiVocherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoaiVoucherService {
    @Autowired
    private LoaiVocherRepository loaiVocherRepository;
    public List<LoaiVoucher> getAllLoaiVouchers() {
        return loaiVocherRepository.findAll();
    }

    public LoaiVoucher addLoaiVoucher(LoaiVoucher loaiVoucher) {
        Date now = new Date();
        loaiVoucher.setNgayTao(now);
        loaiVoucher.setNgayCapNhat(now);
        System.out.println("Ngày tạo: " + loaiVoucher.getNgayTao());
        System.out.println("Ngày cập nhật: " + loaiVoucher.getNgayCapNhat());
        return loaiVocherRepository.save(loaiVoucher);
    }


}