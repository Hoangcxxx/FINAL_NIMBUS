package com.example.duantn.service;

import com.example.duantn.entity.VoucherSanPham;
import com.example.duantn.repository.VoucherSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherSanPhamService {
    @Autowired
    private VoucherSanPhamRepository voucherSanPhamRepository;

    public List<VoucherSanPham> addVoucherSanPhams(List<VoucherSanPham> voucherSanPhams) {
        return voucherSanPhamRepository.saveAll(voucherSanPhams);
    }
}
